package br.com.abril.nds.integracao.ems0111.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0111.processor.EMS0111MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

/**
 * @author Jones.Costa
 * @version 1.0
 */

@Component
@Scope("prototype")
public class EMS0111Route extends CouchDBImportRouteTemplate{
	
	@Autowired
	private EMS0111MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0111";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0111;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0111;
	}
	
	
}
