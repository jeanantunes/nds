package br.com.abril.nds.integracao.ems3100.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems3100.processor.EMS3100MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.DBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

@Component
@Scope("prototype")
public class EMS3100Route extends DBImportRouteTemplate {
	
	@Autowired
	private EMS3100MessageProcessor messageProcessor;

	@Override
	public String getUri() { 
		return "EMS3100";
	}
	
	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS3100;
	}
	
	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS3100;
	}
}