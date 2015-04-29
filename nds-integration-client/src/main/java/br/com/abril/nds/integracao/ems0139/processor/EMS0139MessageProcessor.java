package br.com.abril.nds.integracao.ems0139.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0139Input;
import br.com.abril.nds.model.cadastro.DestinoEncalhe;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0139MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0139MessageProcessor.class);

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory; 
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

		List<Object> objs = new ArrayList<Object>();
		Object obj = new Object();
		objs.add(obj);
		
		tempVar.set(objs);
		
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0139Input input = (EMS0139Input) message.getBody();

		DestinoEncalhe destinoEncalhe = new DestinoEncalhe();

        String codigoDistribuidor = message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
        
        if (codigoDistribuidor == null) {
        
            ndsiLoggerFactory.getLogger().logError(
                    message,
                    EventoExecucaoEnum.HIERARQUIA,
                    String.format( "DestinoEncalhe nulo. Produto %1$s .", input.getCodigoProduto() )
                );
            return ;
        }
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}