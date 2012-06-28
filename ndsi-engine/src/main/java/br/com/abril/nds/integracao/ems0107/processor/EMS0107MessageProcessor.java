package br.com.abril.nds.integracao.ems0107.processor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component

public class EMS0107MessageProcessor extends AbstractRepository implements MessageProcessor  {
	private int nextId;
	
	private static EMS0107MessageProcessor instance = new EMS0107MessageProcessor();

	public static EMS0107MessageProcessor getInstance() {
		return instance;
	}
	
	private EMS0107MessageProcessor() {

	}
	
	@Override
	
	public void processMessage(Message message) {
		EMS0107Input input = (EMS0107Input) message.getBody();
		
		input.setId(++nextId);
		
		getSession().persist(input);
	}	
}