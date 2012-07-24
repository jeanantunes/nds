package br.com.abril.nds.integracao.ems0119.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0119.processor.EMS0119MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;

@Component
@Scope("prototype")
public class EMS0119Route extends FixedLengthRouteTemplate{
	
	@Autowired
	private EMS0119MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0119";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0119;
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0119_IN_FILEMASK");
	}

	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0119Input.class);
	}
	
}
