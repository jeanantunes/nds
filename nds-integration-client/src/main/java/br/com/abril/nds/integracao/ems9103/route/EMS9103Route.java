package br.com.abril.nds.integracao.ems9103.route;



import br.com.abril.nds.integracao.ems0120.processor.EMS0120MessageProcessor;
import br.com.abril.nds.integracao.ems0140.processor.EMS0140MessageProcessor;
import br.com.abril.nds.integracao.ems9103.processor.EMS9103MessageProcessor;
import br.com.abril.nds.integracao.engine.FileOutputRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EMS9103Route  extends FileOutputRoute {

    @Autowired
    private EMS9103MessageProcessor messageProcessor;

    @Override
    public String getUri() {
        return "EMS9103";
    }

    @Override
    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    @Override
    public RouteInterface getRouteInterface() {
        return RouteInterface.EMS9103;
    }

}
