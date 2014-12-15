package br.com.abril.nds.integracao.ems0127.processor;

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
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.model.integracao.icd.IcdChamadaEncalhe;
import br.com.abril.nds.model.integracao.icd.IcdChamadaEncalheItem;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Component
public class EMS0127MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0127MessageProcessor.class);

	@Autowired
	private PlatformTransactionManager transactionManagerIcd;
	
	@Autowired
	private SessionFactory sessionFactoryIcd;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	private String diretorio;
	
	private String pastaInterna;

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

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);

		tempVar.set(objs);

	}

	@Override
	public void processMessage(Message message) {

		/*
		TransactionTemplate template = new TransactionTemplate(transactionManagerIcd);		
		template.execute(new TransactionCallback<Void>() {
			
			@Override
			public Void doInTransaction(TransactionStatus status) {
				return null;
				
			}
		});
		*/
		
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
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		CouchDbClient cdbc = null;

		this.diretorio = parametroSistemaRepository.getParametro("INBOUND_DIR");
		this.pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");
		List<String> distribuidores = this.getDistribuidores(this.diretorio, null);
		
		for(String distribuidor : distribuidores) {
		
			if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {
		
				Session session = getSessionIcd();
				List<IcdChamadaEncalhe> chamadasEncalhe = obterChamadasEncalhe(distribuidor, session);
	
				for(IcdChamadaEncalhe ce : chamadasEncalhe) {
					
					ce.setValorTotalMargemApurado(null);
					ce.setValorTotalMargemInformado(null);
					ce.setValorTotalCreditoApurado(null);
					ce.setValorTotalCreditoInformado(null);
					ce.setValorTotalVendaApurada(null);
					ce.setValorTotalVendaInformada(null);
					
					for(IcdChamadaEncalheItem icei : ce.getChamadaEncalheItens()) {
						icei.setQuantidadeDevolucaoApurada(null);
						icei.setQuantidadeDevolucaoInformada(null);
						icei.setQuantidadeDevolucaoParcial(null);
						icei.setQuantidadeVendaApurada(null);
						icei.setQuantidadeVendaInformada(null);
						icei.setValorVendaInformada(null);
						icei.setValorVendaApurada(null);
						icei.setValorMargemInformado(null);
						icei.setValorMargemApurado(null);
					}
					
					try {
						ce.setTipoDocumento("EMS0127");
						ce.setBaseDeDados(database);
						ce.setUsuarioBaseDeDados(username);
	
						cdbc = this.getCouchDBClient(ce.getCodigoDistribuidor().toString());
						cdbc.save(ce);
					} catch(Exception e) {
	                    LOGGER.error("Erro executando importação de Chamada Encalhe Prodin.", e);
					} finally {
						if (cdbc != null) {
							cdbc.shutdown();
						}			
					}
				}
				closeSessionIcd(session);
			}
		}

	}

	/*
	@SuppressWarnings("unchecked")
	private List<String> obterDistribuidores() {

		StringBuilder hql = new StringBuilder();
		hql.append(" select distinct cod_distribuidor ")
			.append("from chamada_encalhe ")
			.append("where cod_distribuidor = '6248116' "); //FIXME: remover esta condicao

		Query query = this.getSessionIcd().createSQLQuery(hql.toString());

		List<BigDecimal> codigos = query.list();
		List<String> codigosConvertidos = new ArrayList<String>();

		for(BigDecimal c : codigos) {
			codigosConvertidos.add(c.toString());
		}

		return codigosConvertidos;

	}
	*/

	@SuppressWarnings("unchecked")
	private List<IcdChamadaEncalhe> obterChamadasEncalhe(String distribuidor, Session session) {

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

        query.setParameterList("status", new String[] { "A" }); // FIXME:
                                                                // Sérgio: deve
                                                                // buscar status
                                                                // 'A'
		query.setParameter("distribuidor", Long.parseLong(distribuidor));
		query.setParameter("nao", "N");
		query.setParameter("ano", cal.get(Calendar.YEAR));

		return query.list();
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
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

}