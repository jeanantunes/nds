package br.com.abril.nds.integracao.ems0107.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.ems0107.processor.EMS0107MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0107Route extends FixedLengthRouteTemplate {
	
	@Autowired
	private EMS0107MessageProcessor messageProcessor;	

	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0107Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0107";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	} 

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0107_IN_FILEMASK");
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0107;
	}
	
}