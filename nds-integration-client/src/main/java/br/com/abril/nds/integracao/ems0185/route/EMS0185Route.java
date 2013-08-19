package br.com.abril.nds.integracao.ems0185.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0185.processor.EMS0185MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0185Route extends FileOutputRoute {
	
	@Autowired
	private EMS0185MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0185";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0185;
	}
	
	
}
