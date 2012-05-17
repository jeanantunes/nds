package br.com.abril.nds.integracao.ems0109.route;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.route.EMS0107Route;
import br.com.abril.nds.integracao.ems0109.inbound.EMS0109Input;
import br.com.abril.nds.integracao.ems0109.processor.EMS0109MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0109Route extends FixedLengthRouteTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0107Route.class);
	
	@Autowired
	private EMS0109MessageProcessor messageProcessor;
	
	@Override
	public void onStart() {
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Iniciou carga da EMS0107 - %s", new Date()));
		}
				
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Terminou carga da EMS0107 - %s", new Date()));
		}
	}
	
	@Override
	public String getInboundFolder() {
		return (String) getParameters().get("NDSI_EMS0109_INBOUND");
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0109_IN_FILEMASK");
	}

	@Override
	public String getArchiveFolder() {
		return (String) getParameters().get("NDSI_EMS0109_ARCHIVE");
	}

	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0109Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0109";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0109;
	}

}
