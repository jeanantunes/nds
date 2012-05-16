package br.com.abril.nds.integracao.ems0114.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0114Input;
import br.com.abril.nds.integracao.ems0114.processor.EMS0114MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0114Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0114MessageProcessor messageProcessor;
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0114Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0114";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public String getInboundFolder() {
		return (String) getParameters().get("NDSI_EMS0114_INBOUND");
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0114_IN_FILEMASK");
	}

	@Override
	public String getArchiveFolder() {
		return (String) getParameters().get("NDSI_EMS0114_ARCHIVE");
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0114;
	}
}
