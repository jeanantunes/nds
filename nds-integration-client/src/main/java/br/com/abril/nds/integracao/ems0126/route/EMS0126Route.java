package br.com.abril.nds.integracao.ems0126.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0126.processor.EMS0126MessageProcessor;
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
public class EMS0126Route extends CouchDBImportRouteTemplate{
	
	@Autowired
	private EMS0126MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0126";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0126;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0126;
	}
	
	
}
