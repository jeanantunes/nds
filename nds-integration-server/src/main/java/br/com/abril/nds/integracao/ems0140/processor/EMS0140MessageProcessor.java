package br.com.abril.nds.integracao.ems0140.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0140MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0140MessageProcessor.class);
	
	@Autowired
	private SessionFactory sessionFactoryGfs;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);
		
		tempVar.set(objs);
	}

	@Override
	public void processMessage(Message message) {
		//EMS0140Input input = (EMS0140Input) message.getBody();
		
	}

	@Override
	public void posProcess(Object tempVar) {
		
	}

}
