package br.com.abril.nds.integracao.ems0112.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0112.processor.EMS0112MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;


@Component
@Scope("prototype")
public class EMS0112Route extends CouchDBImportRouteTemplate{
	
	@Autowired
	private EMS0112MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0112";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0112;
	}

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS0112;
	}
	
}
