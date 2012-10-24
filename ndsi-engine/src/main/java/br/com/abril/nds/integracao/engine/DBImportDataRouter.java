package br.com.abril.nds.integracao.engine;


import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component

public class DBImportDataRouter extends AbstractRepository implements ContentBasedRouter {
	

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	@Autowired
	private DistribuidorService distribuidorService;


	private String codDistribuidor = null;
	
		
	@SuppressWarnings("unchecked")
	@Override	
	public <T extends RouteTemplate> void routeData(final T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();
		Class<?> classByTipoInterfaceEnum = null;
	
		this.codDistribuidor = this.distribuidorService.obter().getCodigoDistribuidorDinap();
		

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
					message.getHeader().put(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue(), codDistribuidor);
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
