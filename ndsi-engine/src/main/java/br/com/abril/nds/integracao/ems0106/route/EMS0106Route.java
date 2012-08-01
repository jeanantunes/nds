package br.com.abril.nds.integracao.ems0106.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0106.inbound.EMS0106Input;
import br.com.abril.nds.integracao.ems0106.processor.EMS0106MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0106Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0106MessageProcessor messageProcessor;
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0106Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0106";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	} 

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0106_IN_FILEMASK");
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0106;
	}
	
}