package br.com.abril.nds.integracao.ems0131.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0131.processor.EMS0131MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0131Route extends FileOutputRoute{
	
	@Autowired
	private EMS0131MessageProcessor messageProcessor;
	
	@Override
	public String getUri() {
		return "EMS0131";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0131;
	}
	
	

}
