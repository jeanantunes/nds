package br.com.abril.nds.integracao.ems3100.processor;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.abril.nds.integracao.dto.Extratificacao;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Header;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS03100MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS03100MessageProcessor.class);
	
	@Autowired
	private DistribuidorService distribuidorService;
		
	@Autowired
	private PlatformTransactionManager transactionManagerIcd;
	
	@Autowired
	private SessionFactory sessionFactoryIcd;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	private String diretorio;
	
	private String pastaInterna;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);

		tempVar.set(objs);
	}
	
	@Override
	@SuppressWarnings("unused")
	public void processMessage(Message message) {
		
		
		// ajustar a chamada para capturar todos os distribuidores
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-ndsi-cli.xml");

		DataSource ds = (DataSource) applicationContext.getBean("dataSourceIcd");
		
		String database = "";
		String username = "";
		try {
			Connection c = ds.getConnection();
			String[] s = c.getMetaData().getURL().split(":");
			database = s[s.length - 1];
			username = c.getMetaData().getUserName();
            c.close();
        } catch (SQLException sql) {
            LOGGER.error(sql.getMessage(), sql);
		}
		
		CouchDbClient cdbc = null;

		this.diretorio = parametroSistemaRepository.getParametro("INBOUND_DIR");
		this.pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");
		List<String> distribuidores = this.getDistribuidores(this.diretorio, null);
		
		for(String distribuidor : distribuidores) {
		
			if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {
		
				Session session = getSessionIcd();
				List<Extratificacao> mov = obterMovimentoEstoqueCota(distribuidor, session);
	
				for(Extratificacao extratificacao : mov) {
					
					try {
						extratificacao.setTipoDocumento("EMS3100");
						
						cdbc = this.getCouchDBClient(extratificacao.getCodigoDistribuidor());
						cdbc.save(extratificacao);
					} catch(Exception e) {
	                    LOGGER.error("Erro executando importação do Extificado do movimento estoque cota.", e);
					} finally {
						if (cdbc != null) {
							cdbc.shutdown();
						}			
					}
				}
				this.closeSessionIcd(session);
			}
		}
		
	}

	@Override
	public void posProcess(Object tempVar) {

	}
	
	private Query queryMovimentoEstoque() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT me ");
		sql.append("FROM LancamentoDiferenca lancamentoDiferenca  ");
		sql.append("JOIN lancamentoDiferenca.movimentoEstoque me ");
		sql.append("JOIN FETCH me.tipoMovimento tm ");
		sql.append("JOIN FETCH me.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto pr ");		
		sql.append("WHERE tm.grupoMovimentoEstoque in (:grupoMovimentoEstoque) ");
		sql.append("	and me.statusIntegracao = :statusIntegracao ");
		sql.append("	and me.status = :status ");
		sql.append("	and lancamentoDiferenca.status in (:statusLancamentoDiferenca) ");
		sql.append("	and pr.origem in (:origemProduto) ");
		
		Query query = getSession().createQuery(sql.toString());

		query.setParameterList("grupoMovimentoEstoque", (new GrupoMovimentoEstoque[]{ 
				GrupoMovimentoEstoque.SOBRA_EM
				, GrupoMovimentoEstoque.SOBRA_DE
				, GrupoMovimentoEstoque.FALTA_EM_DIRECIONADA_PARA_COTA
				, GrupoMovimentoEstoque.FALTA_EM
				, GrupoMovimentoEstoque.FALTA_DE
				, GrupoMovimentoEstoque.SOBRA_DE_DIRECIONADA_PARA_COTA
				, GrupoMovimentoEstoque.SOBRA_EM_DIRECIONADA_PARA_COTA
		}) );
		
		query.setParameterList("origemProduto", (new Origem[]{ Origem.INTERFACE, Origem.PRODUTO_SEM_CADASTRO }) );
		
		query.setParameter("statusIntegracao", StatusIntegracao.NAO_INTEGRADO);
		query.setParameter("status", StatusAprovacao.APROVADO);
		query.setParameterList("statusLancamentoDiferenca", new StatusAprovacao[] {StatusAprovacao.APROVADO});
		
		return query;
	}

	protected Session getSessionIcd() {

		Session session = null;
		try {
			session = sessionFactoryIcd.getCurrentSession();
		} catch(Exception e) {
            LOGGER.error("Erro ao obter sessão do Hibernate.", e);
		}

		if(session == null) {
			session = sessionFactoryIcd.openSession();
		}

		return session;

	}
	
	/**
	 * Recupera distribuidores a serem processados.
	 */
	private List<String> getDistribuidores(String diretorio, Long codigoDistribuidor) {
		
		List<String> distribuidores = new ArrayList<String>();
		
		if (codigoDistribuidor == null) {
			
			FilenameFilter numericFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					 return name.matches("\\d+");  
				}
			};
			
			File dirDistribs = new File(diretorio);
			distribuidores.addAll(Arrays.asList(dirDistribs.list( numericFilter )));
			
		} else {
			
			distribuidores.add(codigoDistribuidor.toString());
		}
		
		return distribuidores;
	}
	
	@SuppressWarnings("unchecked")
	private List<Extratificacao> obterMovimentoEstoqueCota(String distribuidor, Session session) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select ce ")
			.append("from IcdChamadaEncalhe ce ")
			.append("where ce.tipoStatus in (:status) ")
			.append("and ce.codigoDistribuidor = :distribuidor ")
			.append("and ce.indiceCEProvisoria = :nao ")
			.append("and ce.dataAnoReferencia = :ano");
		
		Query query = session.createQuery(hql.toString());
		
		query.setParameter("distribuidor", Long.parseLong(distribuidor));
		query.setParameter("nao", "N");
		query.setParameter("ano", cal.get(Calendar.YEAR));

		query.setResultTransformer(new AliasToBeanResultTransformer(Extratificacao.class));
		
		return query.list();
	}
	
	protected boolean closeSessionIcd(Session session) {

		try {
			if(session != null && session.isOpen()) {
				session.close();
				return true;
			}
		} catch(Exception e) {
            LOGGER.error("Erro ao obter sessão do Hibernate.", e);
		}

		return false;

	}
}