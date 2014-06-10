package br.com.abril.nds.integracao.ems0118.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0118.inbound.EMS0118Input;
import br.com.abril.nds.integracao.ems0118.processor.EMS0118MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;
/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
@Scope("prototype")
public class EMS0118Route extends FixedLengthRouteTemplate{

	@Autowired
	private EMS0118MessageProcessor messageProcessor;
	

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0118_IN_FILEMASK");
	}


	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0118Input.class);		
	}

	@Override
	public String getUri() {
		return "EMS0118";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0118;
	}
	
	

}
