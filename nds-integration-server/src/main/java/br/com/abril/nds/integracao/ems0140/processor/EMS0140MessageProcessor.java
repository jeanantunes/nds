package br.com.abril.nds.integracao.ems0140.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0140Input;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0140MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
	}

	@Override
	public void processMessage(Message message) {
		EMS0140Input input = new EMS0140Input();
		input.get_id();
	}

	@Override
	public void posProcess(Object tempVar) {
		
	}

}
