package br.com.abril.nds.integracao.ems0129.route;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0129.processor.EMS0129MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0129Route extends FileOutputRoute{
	
	@Autowired
	private EMS0129MessageProcessor messageProcessor;

	@Override
	public String getUri() {
		return "EMS0129";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0129;
	}
	
	public String execute(String userName, Date data, String codigoDistribuidor) {
		
		getParameters().put("DATA_LCTO_DISTRIB", data);

		execute(userName, codigoDistribuidor);
		
		return messageProcessor.getMensagemValidacao();
	}
	
}