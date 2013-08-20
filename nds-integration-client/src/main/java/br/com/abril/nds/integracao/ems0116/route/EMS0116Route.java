package br.com.abril.nds.integracao.ems0116.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0116.inbound.EMS0116Input;
import br.com.abril.nds.integracao.ems0116.processor.EMS0116MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0116Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0116MessageProcessor messageProcessor;
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0116Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0116";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0116_IN_FILEMASK");
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0116;
	}
}
