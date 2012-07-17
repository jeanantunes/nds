package br.com.abril.nds.integracao.engine;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component

public class CouchDBImportDataRouter extends AbstractRepository implements ContentBasedRouter {
	

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	private Long codDistribuidor = null;
	
	
	private void setCodDistribuidor(Long codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}
	
	
	@Override
	
	public <T extends RouteTemplate> void routeData(T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();
		Class<?> classLinha = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getClasseLinha();
		
		this.consultaCodigoDistribuidor();
		CouchDbClient couchDbClient = this.getCouchDBClient();
			
		View view = couchDbClient.view("importacao/porTipoDocumento");
		
		//verifyViewUpdate();
		
		view.key(inputModel.getRouteInterface().getName());
		view.limit(couchDbProperties.getBachSize());
		view.includeDocs(true);
		ViewResult<String, ?, Void> result = view.queryView(String.class, classLinha, Void.class);
		do {	
			
			
			for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {
				
				IntegracaoDocument doc = (IntegracaoDocument) row.getDoc(); 
				
				final Message message = new Message();
				message.getHeader().put(MessageHeaderProperties.URI.getValue(), inputModel.getRouteInterface().getName());
				message.getHeader().put(MessageHeaderProperties.PAYLOAD.getValue(), doc);
				message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), doc.getNomeArquivo());
				message.getHeader().put(MessageHeaderProperties.FILE_CREATION_DATE.getValue(), doc.getDataHoraExtracao());
				message.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), doc.getLinhaArquivo());
				message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), inputModel.getUserName());
				message.getHeader().put(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue(), this.codDistribuidor);
				
				message.setBody(doc);
				
				try {
					
					TransactionTemplate template = new TransactionTemplate(transactionManager);
					template.execute(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(TransactionStatus status) {
						
							messageProcessor.processMessage(message);
							
							getSession().flush();
							getSession().clear();
	
							return null;
						}
					});
				} catch(Throwable e) {
					ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());
					e.printStackTrace();
				}
				
				String erro = (String) message.getHeader().get(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue()); 
				
				if (erro == null) {
					couchDbClient.remove(doc);
				} else {
					doc.setErro(erro);
					couchDbClient.update(doc);
				}
			}
			
			view = couchDbClient.view("importacao/porTipoDocumento");
			view.key(inputModel.getRouteInterface().getName());
			view.limit(couchDbProperties.getBachSize());
			result = view.queryView(String.class, classLinha, Void.class);
		} while(!result.getRows().isEmpty());
		
		couchDbClient.shutdown();
	}
	
	private void verifyViewUpdate() {
		
		String couchURL = String.format("%s://%s:%d",
				couchDbProperties.getProtocol(),				
				couchDbProperties.getHost(), 
				couchDbProperties.getPort());

		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String line;
		try {
			url = new URL(couchURL + "/_active_tasks");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
 
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * 
	 * @return client
	 */
	private CouchDbClient getCouchDBClient() {
		
		return new CouchDbClient(
				"db_" + StringUtils.leftPad(this.codDistribuidor.toString(), 8, "0"),
				true,
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
		);
	}
	
	/**
	 * Busca o código deste distribuidor e seta no atributo da classe.
	 */
	
	private void consultaCodigoDistribuidor() {
		
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				
				String hql = "SELECT p.valor from ParametroSistema p where p.tipoParametroSistema = :tipoParametroSistema";
				
				Query query = getSession().createQuery(hql);
				query.setParameter("tipoParametroSistema", TipoParametroSistema.CODIGO_DISTRIBUIDOR_DINAP);
				query.setMaxResults(1);
			
				setCodDistribuidor(Long.parseLong( query.uniqueResult().toString() ));
				
				return null;
			}
		});
	}
}
