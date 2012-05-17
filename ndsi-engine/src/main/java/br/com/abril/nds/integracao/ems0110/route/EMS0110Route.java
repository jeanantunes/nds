package br.com.abril.nds.integracao.ems0110.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.integracao.ems0110.processor.EMS0110MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0110Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0110MessageProcessor messageProcessor;
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0110Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0110";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public String getInboundFolder() {
		return (String) getParameters().get("NDSI_EMS0110_INBOUND");
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0110_IN_FILEMASK");
	}

	@Override
	public String getArchiveFolder() {
		return (String) getParameters().get("NDSI_EMS0110_ARCHIVE");
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0110;
	}
}
