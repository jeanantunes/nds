package br.com.abril.nds.integracao.ems0127.processor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.model.integracao.icd.IcdChamadaEncalhe;
import br.com.abril.nds.model.integracao.icd.IcdChamadaEncalheItem;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0127MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0127MessageProcessor.class);

	@Autowired
	private SessionFactory sessionFactoryIcd;

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

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);

		tempVar.set(objs);

	}

	@Override
	public void processMessage(Message message) {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-ndsi-cli.xml");

		DataSource ds = (DataSource) applicationContext.getBean("dataSourceIcd");
		
		String database = "";
		String username = "";
		try {
			Connection c = ds.getConnection();
			String[] s = c.getMetaData().getURL().split(":");
			database = s[s.length - 1];
			username = c.getMetaData().getUserName();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		
		CouchDbClient cdbc = null;

		List<String> distribuidores = obterDistribuidores();

		for(String distribuidor : distribuidores) {

			List<IcdChamadaEncalhe> chamadasEncalhe = obterChamadasEncalhe(distribuidor);

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
		}

	}

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

	@SuppressWarnings("unchecked")
	private List<IcdChamadaEncalhe> obterChamadasEncalhe(String distribuidor) {

		StringBuilder hql = new StringBuilder();
		hql.append(" select ce ")
			.append("from IcdChamadaEncalhe ce join fetch ce.chamadaEncalheItens cei join fetch cei.lancamentoEdicaoPublicacao l ")
			.append("where ce.tipoStatus in (:status) ")
			.append("and ce.codigoDistribuidor = :distribuidor ")
			.append("and ce.indiceCEProvisoria = :nao ")
			.append("and ce.dataAnoReferencia > 2012");
		
		Query query = this.getSessionIcd().createQuery(hql.toString());

        query.setParameterList("status", new String[] { "A" }); // FIXME:
                                                                // Sérgio: deve
                                                                // buscar status
                                                                // 'A'
		query.setParameter("distribuidor", Long.parseLong(distribuidor));
		query.setParameter("nao", "N");

		return query.list();
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}

}