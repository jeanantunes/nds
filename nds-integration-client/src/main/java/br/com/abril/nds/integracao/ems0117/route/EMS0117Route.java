package br.com.abril.nds.integracao.ems0117.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0117.inbound.EMS0117Input;
import br.com.abril.nds.integracao.ems0117.processor.EMS0117MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0117Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0117MessageProcessor messageProcessor;
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0117Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0117";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0117_IN_FILEMASK");
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0117;
	}
}
