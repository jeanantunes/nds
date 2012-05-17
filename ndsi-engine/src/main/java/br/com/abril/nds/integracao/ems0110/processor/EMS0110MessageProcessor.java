package br.com.abril.nds.integracao.ems0110.processor;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;

@Component
public class EMS0110MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
		
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message){
		EMS0110Input input = (EMS0110Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();

	}
}