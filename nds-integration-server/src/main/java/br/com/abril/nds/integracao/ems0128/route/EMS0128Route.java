package br.com.abril.nds.integracao.ems0128.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0128.processor.EMS0128MessageProcessor;
import br.com.abril.nds.integracao.enums.RouteInterface;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.route.DBImportRouteTemplate;
import br.com.abril.nds.model.integracao.MessageProcessor;

@Component
@Scope("prototype")
public class EMS0128Route extends DBImportRouteTemplate {
	
	@Autowired
	private EMS0128MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0128";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0128;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0128;
	}
	
	
}