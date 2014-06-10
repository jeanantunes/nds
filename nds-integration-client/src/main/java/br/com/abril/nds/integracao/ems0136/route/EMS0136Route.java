package br.com.abril.nds.integracao.ems0136.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0136.processor.EMS0136MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

@Component
@Scope("prototype")
public class EMS0136Route extends CouchDBImportRouteTemplate {

	@Autowired
	private EMS0136MessageProcessor messageProcessor;
	
	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0136;
	}

	@Override
	public String getUri() {
		return "EMS0136";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0136;
	}

}
