package br.com.abril.nds.integracao.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component("dbImportDataRouter")
public class DBImportDataRouter extends AbstractRepository implements ContentBasedRouter {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	@Autowired
	private DistribuidorService distribuidorService;	
		
	@SuppressWarnings("unchecked")
	@Override	
	public <T extends RouteTemplate> void routeData(final T inputModel) {

		final MessageProcessor messageProcessor = inputModel.getMessageProcessor();

		List<String> codDistribuidores = new ArrayList<>();
		
		codDistribuidores.add(this.distribuidorService.obter().getCodigoDistribuidorDinap());
		codDistribuidores.add(this.distribuidorService.obter().getCodigoDistribuidorFC());
		
		for (final String codDistribuidor : codDistribuidores) {
		
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
						} catch(Exception e) {
							ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());
						}
											
					}		
                    // Processamento a ser executado APÓS o processamento
                    // principal:
					messageProcessor.posProcess(tempVar);
					return null;
				}
			});
		}
	}
		
}