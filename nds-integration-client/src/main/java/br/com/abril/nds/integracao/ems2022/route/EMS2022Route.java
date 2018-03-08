package br.com.abril.nds.integracao.ems2022.route;

import br.com.abril.nds.integracao.ems2022.processor.EMS2022MessageProcessor;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.DBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class EMS2022Route extends DBImportRouteTemplate {
	
	@Autowired
	private EMS2022MessageProcessor messageProcessor;

	@Override
	public InterfaceEnum getInterfaceEnum() {
		return InterfaceEnum.EMS2022;
	}

	@Override
	public String getUri() { 
		return "EMS2022";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return this.messageProcessor;
	}

	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS2022;
	}

}
