package br.com.abril.nds.integracao.engine;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

@Component
public class CouchDBImportDataRouter implements ContentBasedRouter {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	private Integer codDistribuidor = null;
	
	
	private void setCodDistribuidor(Integer codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}
	
	
	@Override
	public <T extends RouteTemplate> void routeData(T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();
		Class<?> classLinha = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getClasseLinha();
		
		this.consultaCodigoDistribuidor();
		CouchDbClient couchDbClient = this.getCouchDBClient();
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
		view.key(inputModel.getRouteInterface().getName());
		ViewResult<String, ?, Void> result = view.queryView(String.class, classLinha, Void.class);
		
		for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {
			
			IntegracaoDocument doc = (IntegracaoDocument) row.getValue(); 
			
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
						
						entityManager.flush();
						entityManager.clear();

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
		
		couchDbClient.shutdown();
	}
	
	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * 
	 * @return client
	 */
	private CouchDbClient getCouchDBClient() {
		
		return new CouchDbClient(
				"db_" + StringUtils.leftPad(this.codDistribuidor.toString(), 7, "0"),
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
				
				String sql = "SELECT a.codigo FROM Distribuidor a";
				Query query = entityManager.createQuery(sql);
				
				setCodDistribuidor((Integer) query.getSingleResult());
				
				return null;
			}
		});
	}
}
