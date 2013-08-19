package br.com.abril.nds.integracao.ems0132.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0132.processor.EMS0132MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

/**
 * 
 * 
 * @author Discover Technology
 */
@Component
@Scope("prototype")
public class EMS0132Route extends FileOutputRoute {

	@Autowired
	private EMS0132MessageProcessor messageProcessor;
	
	@Override
	public String getUri() {
		return "EMS0132";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0132;
	}

}
