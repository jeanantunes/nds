package br.com.abril.nds.integracao.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;

public class CouchDBImportDataRouter implements ContentBasedRouter {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	private Properties couchDbProperties;
	private Long codDistribuidor = null;
	
	
	private void setCodDistribuidor(Long codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}
	
	
	@Override
	public <T extends RouteTemplate> void routeData(T inputModel) {

		MessageProcessor messageProcessor = inputModel.getMessageProcessor();
		Class<?> classLinha = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getClasseLinha();
		
		CouchDbClient couchDbClient = this.getCouchDBClient();
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
		view.key(inputModel.getRouteInterface().getName());
		view.includeDocs(true);
		ViewResult<String, Void, ?> result = view.queryView(String.class, Void.class, classLinha);
		
		for (Rows row: result.getRows()) {
			
			IntegracaoDocument doc = (IntegracaoDocument) row.getDoc(); 
			
			Message message = new Message();
			message.getHeader().put(MessageHeaderProperties.URI.getValue(), inputModel.getRouteInterface().getName());
			message.getHeader().put(MessageHeaderProperties.PAYLOAD.getValue(), doc);
			message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), doc.getNomeArquivo());
			message.getHeader().put(MessageHeaderProperties.FILE_CREATION_DATE.getValue(), doc.getDataHoraExtracao());
			message.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), doc.getLinhaArquivo());
			message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), inputModel.getUserName());
			
			message.setBody(doc);
			
			messageProcessor.processMessage(message);
		}
		
		couchDbClient.shutdown();
	}
	
	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * 
	 * @return client
	 */
	private CouchDbClient getCouchDBClient() {
		
		// Recuperando código deste distribuidor (há apenas um registro na tabela)
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				
				String sql = "SELECT a.id FROM Distribuidor a";
				Query query = entityManager.createQuery(sql);
				
				setCodDistribuidor((Long) query.getSingleResult());
				
				return null;
			}
		});
	
		// Criando client
		this.carregaCouchDbProperties();
		
		return new CouchDbClient(
				"db_" + this.codDistribuidor,
				true,
				this.couchDbProperties.getProperty("couchdb.protocol"),
				this.couchDbProperties.getProperty("couchdb.host"),
				Integer.valueOf(this.couchDbProperties.getProperty("couchdb.port")),
				this.couchDbProperties.getProperty("couchdb.username"),
				this.couchDbProperties.getProperty("couchdb.password")
		);
	}
	
	/**
	 * Carrega os dados do arquivo couchdb.properties
	 */
	private void carregaCouchDbProperties() {
		
		try {
			couchDbProperties = new Properties();
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("couchdb.properties");
			couchDbProperties.load(inputStream);
		} catch (IOException e) {
			// TODO: parar execução
			e.printStackTrace();
		}
	}
}
