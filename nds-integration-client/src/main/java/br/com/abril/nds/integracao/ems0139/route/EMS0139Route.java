package br.com.abril.nds.integracao.ems0139.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0138.processor.EMS0138MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.DBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

@Component
@Scope("prototype")
public class EMS0139Route extends DBImportRouteTemplate {

	@Autowired
	private EMS0138MessageProcessor messageProcessor;
	
	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0139;
	}

	@Override
	public String getUri() {
		return "EMS0139";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0139;
	}

}
