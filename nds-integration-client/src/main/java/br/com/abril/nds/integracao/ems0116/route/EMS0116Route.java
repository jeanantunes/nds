package br.com.abril.nds.integracao.ems0116.route;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.route.EMS0107Route;
import br.com.abril.nds.integracao.ems0116.inbound.EMS0116Input;
import br.com.abril.nds.integracao.ems0116.processor.EMS0116MessageProcessor;
import br.com.abril.nds.integracao.ems0117.route.EMS0117Route;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0116Route extends FixedLengthRouteTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0107Route.class);
	
	@Autowired
	private EMS0116MessageProcessor messageProcessor;
	
	@Autowired
	private EMS0117Route ems0117Route;
	
	@Override
	public void onStart() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Iniciou carga da EMS0117 - %s", new Date()));
		}
		
		ems0117Route.execute(getUserName());
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Terminou carga da EMS0117 - %s", new Date()));
		}
	}
	
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
