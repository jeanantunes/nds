package br.com.abril.nds.integracao.ems0119.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0119.processor.EMS0119MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0119Route extends FileOutputRoute{
	
	@Autowired
	private EMS0119MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0119";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0119;
	}
	
	
}
