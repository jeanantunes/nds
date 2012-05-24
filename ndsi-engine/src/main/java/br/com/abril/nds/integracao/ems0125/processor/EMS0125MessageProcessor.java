package br.com.abril.nds.integracao.ems0125.processor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.integracao.model.canonic.EMS0125Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;


@Component
public class EMS0125MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
		
	@Override
	public void processMessage(Message message){
		EMS0125Input input = (EMS0125Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe  ");
		sql.append("FROM ProdutoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("     pe.numeroEdicao = :numeroEdicao ");
		sql.append("  AND   p.codigo = :codigo ");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("numeroEdicao", input.getEdicao());
		query.setParameter("codigo", input.getCodProd());
		
		ProdutoEdicao produtoEdicao = null;
			
		try {
			
			produtoEdicao = (ProdutoEdicao) query.getSingleResult();
			
		} catch(NoResultException e) {
			
			// Não encontrou o Produto / ProdutoEdicao Realizar Log			
			ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.SEM_DOMINIO,"Produto " +  input.getCodProd() + " e Produto Edicao " + input.getEdicao() + "nao encontrado.");
			e.printStackTrace();
			return;
		}
		
		produtoEdicao.setChamadaCapa(input.getCodBarra());
		ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do ProdutoEdicao " + input.getEdicao());
				
	}
}
		
		