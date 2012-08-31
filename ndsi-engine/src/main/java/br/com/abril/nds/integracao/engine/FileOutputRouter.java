package br.com.abril.nds.integracao.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;


@Component
public class FileOutputRouter extends AbstractRepository implements ContentBasedRouter {
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	public <T extends RouteTemplate> void routeData(T route) {
		final Message message = new Message();
		
		FileOutputRoute fileOutputRoute = (FileOutputRoute) route;
		final MessageProcessor messageProcessor = fileOutputRoute.getMessageProcessor();
		
		// FAZ MERGE COM OS PARAMETROS
		message.getHeader().putAll(fileOutputRoute.getParameters());
		
		// ADICIONA OUTRAS INFORMACOES NO HEADER (METADATA)
		message.getHeader().put(MessageHeaderProperties.URI.getValue(), fileOutputRoute.getUri());
		message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), fileOutputRoute.getUserName());
		
		// PASTA DE SAIDA
		message.getHeader().put(MessageHeaderProperties.OUTBOUND_FOLDER.getValue(), fileOutputRoute.getOutboundFolder());
		
		
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
				
	}
}