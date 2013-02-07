package br.com.abril.nds.integracao.ems0108.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.integracao.ems0108.processor.EMS0108MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0108Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0108MessageProcessor messageProcessor;
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0108Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0108";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0108_IN_FILEMASK");
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0108;
	}
}
