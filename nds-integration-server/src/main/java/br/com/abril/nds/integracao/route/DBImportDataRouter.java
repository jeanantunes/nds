package br.com.abril.nds.integracao.route;


import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.log.NdsServerLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class DBImportDataRouter extends AbstractRepository implements BaseRouter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBImportDataRouter.class);

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsServerLoggerFactory ndsiLoggerFactory;
	
	@SuppressWarnings("unchecked")
	public <T extends RouteTemplate> void routeData(final T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();
	
		final AtomicReference<Object> tempVar = new AtomicReference<Object>();

		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallback<Void>() {
			
			@Override
			public Void doInTransaction(TransactionStatus status) {
				// Processamento a ser executado ANTES do processamento principal:
				messageProcessor.preProcess(tempVar);
		
				for (Object o : (List<Object>)tempVar.get() ) {
						
					final Message message = new Message();
					message.getHeader().put(MessageHeaderProperties.URI.getValue(), inputModel.getRouteInterface().getName());
					message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), inputModel.getUserName());
					message.getHeader().put(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue(), inputModel.getCodigoDistribuidor());
					message.setTempVar(tempVar);
									
					message.setBody(o);
					
					try {
												
						messageProcessor.processMessage(message);
						
					} catch(Exception e) {
						LOGGER.error(e.getMessage(), e);
						ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());
					}

				}		
                // Processamento a ser executado APÓS o processamento principal:
				messageProcessor.posProcess(tempVar);
				return null;
			}
		});

	}
		
}