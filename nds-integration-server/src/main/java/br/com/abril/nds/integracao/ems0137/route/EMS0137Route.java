package br.com.abril.nds.integracao.ems0137.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0137.processor.EMS0137MessageProcessor;
import br.com.abril.nds.integracao.enums.RouteInterface;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.route.DBImportRouteTemplate;
import br.com.abril.nds.model.integracao.MessageProcessor;

@Component
@Scope("prototype")
public class EMS0137Route extends DBImportRouteTemplate {
	
	@Autowired
	private EMS0137MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0137";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0137;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0137;
	}
	
	
}