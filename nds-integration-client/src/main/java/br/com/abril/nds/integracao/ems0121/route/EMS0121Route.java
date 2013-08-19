package br.com.abril.nds.integracao.ems0121.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0121.processor.EMS0121MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0121Route extends FileOutputRoute{
	
	@Autowired
	private EMS0121MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0121";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0121;
	}
	
	
}
