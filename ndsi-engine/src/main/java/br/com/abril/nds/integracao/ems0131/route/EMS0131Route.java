package br.com.abril.nds.integracao.ems0131.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0131.inbound.EMS0131Input;
import br.com.abril.nds.integracao.ems0131.processor.EMS0131MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0131Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0131MessageProcessor messageProcessor;
	
	@Override
	public String getInboundFolder() {
		return (String) getParameters().get("NDSI_EMS0131_INBOUND");
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0131_IN_FILEMASK");
	}

	@Override
	public String getArchiveFolder() {
		return (String) getParameters().get("NDSI_EMS0131_ARCHIVE");
	}

	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0131Input.class);
		
	}

	@Override
	public String getUri() {
		return "EMS0131";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		// TODO Auto-generated method stub
		return null;
	}

}
