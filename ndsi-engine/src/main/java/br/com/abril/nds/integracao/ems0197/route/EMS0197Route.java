package br.com.abril.nds.integracao.ems0197.route;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0197.processor.EMS0197MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;

@Component
@Scope("prototype")
public class EMS0197Route extends FileOutputRoute{
	
	@Autowired
	private EMS0197MessageProcessor messageProcessor;


	@Override
	public String getUri() {
		return "EMS0197";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0197;
	}
	
	public int execute(String userName, Date data) {
		messageProcessor.setDataLctoDistrib(data);
		execute(userName);
		
		return messageProcessor.getQuantidadeArquivosGerados();
	}

}
