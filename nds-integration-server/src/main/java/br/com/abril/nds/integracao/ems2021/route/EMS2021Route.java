package br.com.abril.nds.integracao.ems2021.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems2021.processor.EMS2021MessageProcessor;
import br.com.abril.nds.integracao.enums.RouteInterface;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.route.DBImportRouteTemplate;
import br.com.abril.nds.model.integracao.MessageProcessor;

@Component
@Scope("prototype")
public class EMS2021Route extends DBImportRouteTemplate {

    @Autowired
    private EMS2021MessageProcessor messageProcessor;

    @Override
    public InterfaceEnum getInterfaceEnum() {
	return InterfaceEnum.EMS2021;
    }

    @Override
    public String getUri() {
	return "EMS2021";
    }

    @Override
    public MessageProcessor getMessageProcessor() {
	return this.messageProcessor;
    }

    @Override
    public RouteInterface getRouteInterface() {
	return RouteInterface.EMS2021;
    }

}
