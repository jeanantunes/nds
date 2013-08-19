package br.com.abril.nds.integracao.ems0135.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0135.processor.EMS0135MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

@Component
@Scope("prototype")
public class EMS0135Route extends CouchDBImportRouteTemplate {
	
	@Autowired
	private EMS0135MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0135";
	}
	
	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}
	
	@Override
	public RouteInterface getRouteInterface() {		 
		return RouteInterface.EMS0135;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {		 
		return InterfaceEnum.EMS0135;
	}	

}
