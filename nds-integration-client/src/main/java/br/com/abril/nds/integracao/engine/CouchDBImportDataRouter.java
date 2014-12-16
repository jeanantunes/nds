package br.com.abril.nds.integracao.engine;


import java.util.concurrent.atomic.AtomicReference;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.com.abril.nds.integracao.model.canonic.EMS0110FilialInput;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.model.canonic.TipoInterfaceEnum;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.AbstractRepository;

@Component("couchDBImportDataRouter")
public class CouchDBImportDataRouter extends AbstractRepository implements ContentBasedRouter {

	private static Logger LOGGER = LoggerFactory.getLogger(CouchDBImportDataRouter.class);
	
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
			
			InterfaceEnum interfaceEnum  = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum();
			
			if(InterfaceEnum.EMS0110.equals(interfaceEnum)){
				interfaceEnum = InterfaceEnum.EMS0110.getInterfaceEnum(EMS0110FilialInput.class);
			}
			
			classByTipoInterfaceEnum = interfaceEnum.getClasseLinha();
			
		} else if (((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getTipoInterfaceEnum() == TipoInterfaceEnum.DETALHE_INLINE ) {
			
			classByTipoInterfaceEnum = ((CouchDBImportRouteTemplate) inputModel).getInterfaceEnum().getClasseMaster();
		}
		
		String codigoDistribuidor = ((CouchDBImportRouteTemplate) inputModel).getCodigoDistribuidor();
		
		CouchDbClient couchDbClient = this.getCouchDBClient(codigoDistribuidor);
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
		
		view.startKey(inputModel.getRouteInterface().getName(), null);
		view.endKey(inputModel.getRouteInterface().getName(), "");

		view.includeDocs(true);
		ViewResult<Object[], Void, ?> result = null;
		
		try {
			
			result = view.queryView(Object[].class, Void.class, classByTipoInterfaceEnum);
			
		} catch(org.lightcouch.NoDocumentException e){
			//Nao ha informacoes a serem processadas
            ndsiLoggerFactory.getLogger().setStatusProcesso(StatusExecucaoEnum.VAZIO);
            couchDbClient.shutdown();
			return;
		}
		
		AtomicReference<Object> tempVar = new AtomicReference<Object>();
		// Processamento a ser executado ANTES do processamento principal:
		messageProcessor.preProcess(tempVar);

		//
		final Message messageAux = new Message();
		
		if(result!=null && result.getRows()!=null && result.getRows().size()>0){
		
			IntegracaoDocument doc = (IntegracaoDocument) result.getRows().get(0).getDoc(); 
			
			messageAux.getHeader().put(MessageHeaderProperties.URI.getValue(), inputModel.getRouteInterface().getName());
			messageAux.getHeader().put(MessageHeaderProperties.PAYLOAD.getValue(), doc);
			messageAux.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), doc.getNomeArquivo() != null ? doc.getNomeArquivo() : "");
			messageAux.getHeader().put(MessageHeaderProperties.FILE_CREATION_DATE.getValue(), doc.getDataHoraExtracao());
			messageAux.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), doc.getLinhaArquivo());
			messageAux.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), inputModel.getUserName());
			messageAux.getHeader().put(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue(), codigoDistribuidor);
		    messageAux.setTempVar(tempVar);
		}
		
		ndsiLoggerFactory.getLogger().logInfo(messageAux, EventoExecucaoEnum.INF_DADO_ALTERADO,
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
					
					if(e.getMessage() != null)
						ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());
                    LOGGER.error("",e);
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
			
			try {
				result = view.queryView(Object[].class, Void.class, classByTipoInterfaceEnum);
			} catch(org.lightcouch.NoDocumentException e) {
				//Nao ha mais informacoes a serem processadas
				break;
			}
		} while(!result.getRows().isEmpty());
		
        // Processamento a ser executado APÓS o processamento principal:
		messageProcessor.posProcess(tempVar);
		
		couchDbClient.shutdown();
	}
	
}