package br.com.abril.nds.integracao.icd;


import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.ContentBasedRouter;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.AbstractRepository;

@Component

public class DBImportDataRouterIcd extends AbstractRepository implements ContentBasedRouter {
	

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	@SuppressWarnings("unchecked")
	@Override	
	public <T extends RouteTemplate> void routeData(final T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();
	
		final AtomicReference<Object> tempVar = new AtomicReference<Object>();
		// Processamento a ser executado ANTES do processamento principal:
		
		TransactionTemplate template = new TransactionTemplate(transactionManager);		
		template.execute(new TransactionCallback<Void>() {
			
			@Override
			public Void doInTransaction(TransactionStatus status) {
				messageProcessor.preProcess(tempVar);
				
		
				for (Object o : (List<Object>)tempVar.get() ) {
						
					final Message message = new Message();
					message.getHeader().put(MessageHeaderProperties.URI.getValue(), inputModel.getRouteInterface().getName());
					message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), inputModel.getUserName());
					message.setTempVar(tempVar);
									
					message.setBody(o);
					
					try {
												
							messageProcessor.processMessage(message);						
					} catch(Throwable e) {
						ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());
						e.printStackTrace();
					}
					
					//String erro = (String) message.getHeader().get(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue()); 
										
				}		
				// Processamento a ser executado APÓS o processamento principal:
				messageProcessor.posProcess(tempVar);
				return null;
			}
		});
	
				
	}
		
}
