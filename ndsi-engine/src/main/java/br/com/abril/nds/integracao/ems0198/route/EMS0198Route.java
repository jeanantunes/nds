package br.com.abril.nds.integracao.ems0198.route;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0198.processor.EMS0198MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0198Route extends FileOutputRoute{
	
	@Autowired
	private EMS0198MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0198";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0198;
	}
	
	public void execute(String userName, Date data) {
		
		getParameters().put("DATA_LCTO_DISTRIB", data);
		execute(userName);
	}
	
}