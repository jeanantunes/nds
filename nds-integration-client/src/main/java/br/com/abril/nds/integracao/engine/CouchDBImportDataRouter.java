package br.com.abril.nds.integracao.engine;


import java.util.concurrent.atomic.AtomicReference;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.model.canonic.TipoInterfaceEnum;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.AbstractRepository;


@Component("couchDBImportDataRouter")
public class CouchDBImportDataRouter extends AbstractRepository implements ContentBasedRouter {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	@Override
	public <T extends RouteTemplate> void routeData(T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();
		Class<?> classByTipoInterfaceEnum = null;
		if(((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getTipoInterfaceEnum() == TipoInterfaceEnum.SIMPLES ) {
			classByTipoInterfaceEnum = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getClasseLinha();
		} else if (((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getTipoInterfaceEnum() == TipoInterfaceEnum.DETALHE_INLINE ) {
			classByTipoInterfaceEnum = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getClasseMaster();
		}
		
		String codigoDistribuidor =
			((CouchDBImportRouteTemplate) inputModel).getCodigoDistribuidor();
		
		CouchDbClient couchDbClient = this.getCouchDBClient(codigoDistribuidor);
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
		
		view.key(inputModel.getRouteInterface().getName());
		view.limit(couchDbProperties.getBachSize());
		view.includeDocs(true);
		ViewResult<String, Void, ?> result = null;
		
		try{
			result = view.queryView(String.class, Void.class, classByTipoInterfaceEnum);
		}catch(org.lightcouch.NoDocumentException e){
			//Nao ha informacoes a serem processadas
            ndsiLoggerFactory.getLogger().setStatusProcesso(StatusExecucaoEnum.VAZIO);
			return;
		}
		
		AtomicReference<Object> tempVar = new AtomicReference<Object>();
		// Processamento a ser executado ANTES do processamento principal:
		messageProcessor.preProcess(tempVar);

		//
		  ndsiLoggerFactory.getLogger().logInfo(null, EventoExecucaoEnum.SEM_DOMINIO,
				  "Qtd documentos a processar : "+result.getRows().size());

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
				message.getHeader().put(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue(), codigoDistribuidor);
				message.setTempVar(tempVar);
				
				message.setBody(doc);
				
				try {
					
					 TransactionTemplate template = new TransactionTemplate(transactionManager);
                      
                     template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
                     template.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
                     template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
					
					template.execute(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(TransactionStatus status) {
						
							messageProcessor.processMessage(message);
							
							getSession().flush();
							getSession().clear();
	
							return null;
						}
					});
				} catch(Exception e) {
                    ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA,
                            e.getMessage());

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
			view.includeDocs(true);
			
			try{
				result = view.queryView(String.class, Void.class, classByTipoInterfaceEnum);
			}catch(org.lightcouch.NoDocumentException e){
				//Nao ha mais informacoes a serem processadas
				break;
			}
		} while(!result.getRows().isEmpty());
		
        // Processamento a ser executado APÓS o processamento principal:
		messageProcessor.posProcess(tempVar);
		
		couchDbClient.shutdown();
	}
	
}