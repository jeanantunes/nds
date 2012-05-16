package br.com.abril.nds.integracao.ems0131.processor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.abril.nds.integracao.ems0131.outbound.EMS0131Output;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.model.cadastro.Cota;

public class EMS0131MessageProcessor implements MessageProcessor {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public void processMessage(Message message) {
	
		EMS0131Output input = (EMS0131Output) message.getBody();
		// Obter cota
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c");
		sql.append("FROM Cota c JOIN FETCH c.box b JOIN FETCH c.pessoa p JOIN FETCH c.endereco end");
		
		Query query = entityManager.createQuery(sql.toString());
		
		@SuppressWarnings("unchecked")
		List<Cota> cotas =  query.getResultList();
		
	
	}

}
