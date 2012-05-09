package br.com.abril.nds.integracao.ems0107.processor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.EstudoCota;

@Component
public class EMS0107MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
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
		
		entityManager.persist(input);		
	}
	
	public void processMessageOld(Message message) {

		EMS0107Input input = (EMS0107Input) message.getBody();
		
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe.produto ");
		sql.append("FROM   ProdutoEdicao pe ");
		sql.append("WHERE ");
		sql.append("	   pe.numeroEdicao = :numeroEdicao ");
		sql.append("	   AND p.codigo    = :codigoProduto ");
		Query query = entityManager.createQuery(sql.toString());
		query.setMaxResults(1);
		query.setParameter("numeroEdicao", input.getEdicao());
		query.setParameter("codigoProduto", input.getCodigoPublicacao());
		Produto produto = null;
		try {
			produto = (Produto) query.getSingleResult();
		} catch(NoResultException e) {
			// FIXME Não encontrou o produto. Realizar Log
			// Passar para a próxima linha
			return;
		}
		
		// Obter a cota
		sql = null;
		query = null;
		sql = new StringBuilder();
		sql.append("SELECT co");
		sql.append("FROM Cota co ");
		sql.append("WHERE ");
		sql.append("     co.numeroCota = :numeroCota ");
		query = entityManager.createQuery(sql.toString());
		query.setMaxResults(1);
		query.setParameter("numeroCota", input.getCodigoCota());
		Cota cota = null;
		try {
			cota = (Cota) query.getSingleResult();
		} catch(NoResultException e) {
			// FIXME Não encontrou a cota. Realizar Log
			// Passar para a próxima linha
			return;
		}	
		
		// Obter o Estudo ?
		
		// Inserir EstudoCota
		EstudoCota estudoCota = new EstudoCota();
		estudoCota.setCota(cota);
		// estudoCota.setEstudo(estudo);
		estudoCota.setQtdeEfetiva(input.getQuantidadeReparte());
		estudoCota.setQtdePrevista(input.getQuantidadeReparte());
		entityManager.persist(estudoCota);
	}

}
