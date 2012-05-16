package br.com.abril.nds.integracao.engine;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;


@Component
public class FileOutputRouter implements ContentBasedRouter {
	public <T extends RouteTemplate> void routeData(T route) {
		final Message message = new Message();
		
		// FAZ MERGE COM OS PARAMETROS
		message.getHeader().putAll(route.getParameters());
		
		// ADICIONA OUTRAS INFORMACOES NO HEADER (METADATA)
		message.getHeader().put(MessageHeaderProperties.URI.getValue(), route.getUri());
		message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), route.getUserName());
		
		route.getMessageProcessor().processMessage(message);
	}
}