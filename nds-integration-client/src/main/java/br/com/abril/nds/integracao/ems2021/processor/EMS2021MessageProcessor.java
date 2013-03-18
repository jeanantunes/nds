package br.com.abril.nds.integracao.ems2021.processor;

import java.util.concurrent.atomic.AtomicReference;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

public class EMS2021MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
		
	}

}
