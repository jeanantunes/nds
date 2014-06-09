package br.com.abril.nds.integracao.ems0140.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0140.processor.EMS0140MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

@Component
@Scope("prototype")
public class EMS0140Route extends CouchDBImportRouteTemplate {
	
	@Autowired
	private EMS0140MessageProcessor messageProcessor;
	
	@Override
    public String getUri() {
        return "EMS0140";
    }
	
	@Override
    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }
	
	@Override
    public RouteInterface getRouteInterface() {
        return RouteInterface.EMS0140;
    }
	
	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0140;
	}

}
