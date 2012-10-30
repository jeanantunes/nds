package br.com.abril.nds.integracao.ems0136.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0136MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub

	}

}
