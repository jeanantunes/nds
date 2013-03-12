package br.com.abril.nds.integracao.ems2021.route;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems2021.processor.EMS2021MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

public class EMS2021Route extends CouchDBImportRouteTemplate {
	
	@Autowired
	private EMS2021MessageProcessor messageProcessor;

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS2021;
	}

	@Override
	public String getUri() { 
		return "EMS2021";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS2021;
	}

}
