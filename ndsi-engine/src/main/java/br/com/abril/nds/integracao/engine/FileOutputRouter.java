package br.com.abril.nds.integracao.engine;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;


@Component
public class FileOutputRouter implements ContentBasedRouter {
	public <T extends RouteTemplate> void routeData(T route) {
		final Message message = new Message();
		
		FileOutputRoute fileOutputRoute = (FileOutputRoute) route;
		
		// FAZ MERGE COM OS PARAMETROS
		message.getHeader().putAll(fileOutputRoute.getParameters());
		
		// ADICIONA OUTRAS INFORMACOES NO HEADER (METADATA)
		message.getHeader().put(MessageHeaderProperties.URI.getValue(), fileOutputRoute.getUri());
		message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), fileOutputRoute.getUserName());
		
		// PASTA DE SAIDA
		message.getHeader().put(MessageHeaderProperties.OUTBOUND_FOLDER.getValue(), fileOutputRoute.getOutboundFolder());
		
		// Processamento a ser executado ANTES do processamento principal:
		route.getMessageProcessor().preProcess();
		
		// Processamento principal:
		route.getMessageProcessor().processMessage(message);
		
		// Processamento a ser executado APÓS o processamento principal:
		route.getMessageProcessor().posProcess();
	}
}