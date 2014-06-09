package br.com.abril.nds.integracao.ems0127.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0127.processor.EMS0127MessageProcessor;
import br.com.abril.nds.integracao.enums.RouteInterface;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.route.DBImportRouteTemplate;
import br.com.abril.nds.model.integracao.MessageProcessor;

@Component
@Scope("prototype")
public class EMS0127Route extends DBImportRouteTemplate {
	
	@Autowired
	private EMS0127MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0127";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0127;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0127;
	}
	
	
}