package br.com.abril.nds.integracao.ems0140.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0140.processor.EMS0140MessageProcessor;
import br.com.abril.nds.integracao.enums.RouteInterface;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.route.DBImportRouteTemplate;
import br.com.abril.nds.model.integracao.MessageProcessor;

@Component
@Scope("prototype")
public class EMS0140Route extends DBImportRouteTemplate {
	
	@Autowired
	private EMS0140MessageProcessor ems140MessageProcessor;
	
	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0140;
	}

	@Override
	public String getUri() {
		return "EMS0140";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.ems140MessageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0140;
	}


}
