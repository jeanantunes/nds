package br.com.abril.nds.integracao.ems0106.route;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0106.inbound.EMS0106Input;
import br.com.abril.nds.integracao.ems0106.processor.EMS0106MessageProcessor;
import br.com.abril.nds.integracao.ems0107.route.EMS0107Route;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0106Route extends FixedLengthRouteTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0107Route.class);
	
	@Autowired
	private EMS0106MessageProcessor messageProcessor;

	@Autowired
	private EMS0107Route ems0107Route;

	@Override
	public void onStart() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Iniciou carga da EMS0107 - %s", new Date()));
		}
		
		ems0107Route.execute(getUserName());
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Terminou carga da EMS0107 - %s", new Date()));
		}
	}
	
	@Override
	public void onEnd() {
		
	}
	
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
	public String getInboundFolder() {
		return (String) getParameters().get("NDSI_EMS0106_INBOUND");
	}

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0106_IN_FILEMASK");
	}

	@Override
	public String getArchiveFolder() {
		return (String) getParameters().get("NDSI_EMS0106_ARCHIVE");
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0106;
	}
}