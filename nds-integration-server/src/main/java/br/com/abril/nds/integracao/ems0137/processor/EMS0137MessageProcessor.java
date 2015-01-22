package br.com.abril.nds.integracao.ems0137.processor;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.model.integracao.icd.BaseCalculo;
import br.com.abril.nds.model.integracao.icd.ComposicaoBaseCalculo;
import br.com.abril.nds.model.integracao.icd.EstrategiaCluster;
import br.com.abril.nds.model.integracao.icd.EstrategiaLanctoPraca;
import br.com.abril.nds.model.integracao.icd.EstrategiaMicroDistribuicao;
import br.com.abril.nds.model.integracao.icd.IcdLancamentoEdicaoPublicacao;
import br.com.abril.nds.model.integracao.icd.Praca;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Component
public class EMS0137MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0137MessageProcessor.class);
	
	
	
	//final private String tipoDocumento = "EMS0137";
	private String database = null;
	private String username = null;
	
	CouchDbClient cdbc = null;
	String codigoDistribuidor = null;
	
	private String diretorio;
	private String pastaInterna;

	@Autowired
	private SessionFactory sessionFactoryIcd;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;

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

	/*
	 * estrategia_lancto_praca
	 * estrategia_micro_distbcao
	 * estrategia_cluster
	 * composicao_base_calculo
	 * base_calculo
	 * 
	 */
	@Override
	public void processMessage(Message message) {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-ndsi-cli.xml");

		DataSource ds = (DataSource) applicationContext.getBean("dataSourceIcd");
		
		try {
			Connection c = ds.getConnection();
			String[] s = c.getMetaData().getURL().split(":");
			String database = s[s.length - 1];
			String username = c.getMetaData().getUserName();
            c.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		
		this.diretorio = parametroSistemaRepository.getParametro("INBOUND_DIR");
		this.pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");
		List<String> distribuidores = this.getDistribuidores(this.diretorio, null);
		

		for(String distribuidor : distribuidores) {
			
		  couchEstrategiaMicroDistbcao(getTabelaEstrategiaMicroDistbcao(),distribuidor);
		  
		}


	}

		
	@SuppressWarnings("unchecked")
	private List<EstrategiaLanctoPraca> getTabelaEstrategiaLanctoPraca() {

		StringBuilder hql = new StringBuilder();
		hql.append("from EstrategiaLanctoPraca "); 

		Query query = this.getSessionIcd().createQuery(hql.toString());
		query.setMaxResults(10);
		
		return (List<EstrategiaLanctoPraca>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<EstrategiaMicroDistribuicao> getTabelaEstrategiaMicroDistbcao() {

		StringBuilder hql = new StringBuilder();
		 hql.append("from EstrategiaMicroDistribuicao "); 

		Query query = this.getSessionIcd().createQuery(hql.toString());
		query.setMaxResults(10);

		return (List<EstrategiaMicroDistribuicao>)query.list();
	}

	@SuppressWarnings("unchecked")
	private List<EstrategiaCluster> getTabelaEstrategiaCluster() {

		StringBuilder hql = new StringBuilder();
		 hql.append("from EstrategiaCluster ");

		Query query = this.getSessionIcd().createQuery(hql.toString());
		query.setMaxResults(10);
		
		return (List<EstrategiaCluster>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ComposicaoBaseCalculo> getTabelaComposicaoBaseCalculo() {

		StringBuilder hql = new StringBuilder();
		hql.append("from ComposicaoBaseCalculo ");

		Query query = this.getSessionIcd().createQuery(hql.toString());
		query.setMaxResults(10);

		return (List<ComposicaoBaseCalculo>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<BaseCalculo> getTabelaBaseCalculo() {

		StringBuilder hql = new StringBuilder();
		hql.append("from BaseCalculo "); 

		Query query = this.getSessionIcd().createQuery(hql.toString());
		query.setMaxResults(10);

		return (List<BaseCalculo>)query.list();
	}

	private void couchEstrategiaLanctoPraca(List<EstrategiaLanctoPraca> lista,String distribuidor) {
		
		try {
      
			cdbc = this.getCouchDBClient(distribuidor, true);
			
			for (EstrategiaLanctoPraca item : lista){
				
				try {
					item.setTipoDocumento("EMS0137_EstrategiaLanctoPraca");
					item.setBaseDeDados(database);
					item.setUsuarioBaseDeDados(username);
				
					cdbc.save(item);
				
				} catch(Exception e) {
					LOGGER.error("Erro executando importação do EstrategiaLanctoPraca do item .", e);
					
				}
			}
		} catch(Exception e) {
			LOGGER.error("Erro ao obter conexao com o conchDB", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}
		}
	}
	
	private void couchEstrategiaMicroDistbcao(List<EstrategiaMicroDistribuicao> lista, String distribuidor) {
		
		try {
      
			cdbc = this.getCouchDBClient(distribuidor, true);
			
			for (EstrategiaMicroDistribuicao item : lista){
				
		 
				try {
					
					List<Praca> pracas = item.getPracas();
					List<IcdLancamentoEdicaoPublicacao> edicoes = item.getEdicoes();
					
					item.setTipoDocumento("EMS0137_EstrategiaMicroDistribuicao");
					item.setBaseDeDados(database);
					item.setUsuarioBaseDeDados(username);
				
					cdbc.save(item);
				
				} catch(Exception e) {
					LOGGER.error("Erro executando importação do EstrategiaMicroDistbcao do item .", e);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Erro ao obter conexao com o conchDB", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}
		}
	}
	
	private void couchEstrategiaCluster(List<EstrategiaCluster> lista,String distribuidor) {
		
		try {
			
			cdbc = this.getCouchDBClient(distribuidor, true);
      
			for (EstrategiaCluster item : lista){
				
				try {
					item.setTipoDocumento("EMS0137_EstrategiaCluster");
					item.setBaseDeDados(database);
					item.setUsuarioBaseDeDados(username);
				
					cdbc.save(item);
				
				} catch(Exception e) {
					LOGGER.error("Erro executando importação do EstrategiaCluster do item .", e);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Erro ao obter conexao com o conchDB", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}
		}
	}
	
	private void couchComposicaoBaseCalculo(List<ComposicaoBaseCalculo> lista,String distribuidor) {
		
		try {
      
			cdbc = this.getCouchDBClient(distribuidor, true);
			
			for (ComposicaoBaseCalculo item : lista){
				
				try {
					item.setTipoDocumento("EMS0137_ComposicaoBaseCalculo");
					item.setBaseDeDados(database);
					item.setUsuarioBaseDeDados(username);

					cdbc.save(item);
				
				} catch(Exception e) {
					LOGGER.error("Erro executando importação do ComposicaoBaseCalculo do item .", e);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Erro ao obter conexao com o conchDB", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}
		}
	}
	
	private void couchBaseCalculo(List<BaseCalculo> lista,String distribuidor) {
		
		try {
      
			cdbc = this.getCouchDBClient(distribuidor, true);
			
			for (BaseCalculo item : lista){
				
				try {
					item.setTipoDocumento("EMS0137_BaseCalculo");
					item.setBaseDeDados(database);
					item.setUsuarioBaseDeDados(username);
				
					cdbc.save(item);
				
				} catch(Exception e) {
					LOGGER.error("Erro executando importação do BaseCalculo do item .", e);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Erro ao obter conexao com o conchDB", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}
		}
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
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}

}