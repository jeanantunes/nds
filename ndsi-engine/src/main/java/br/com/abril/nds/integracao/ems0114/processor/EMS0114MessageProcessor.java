package br.com.abril.nds.integracao.ems0114.processor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0114Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

@Component
public class EMS0114MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
		
	@Override
	public void processMessage(Message message){
		EMS0114Input input = (EMS0114Input) message.getBody();
		
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
			
			// Não encontrou o Produto. Realizar Log 			
			ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.HIERARQUIA,"Produto " +  input.getCodProd() + " e Produto Edicao nao encontrado.");
			e.printStackTrace();
			return;
		}
		
		sql = new StringBuilder();
		sql.append("SELECT l  ");
		sql.append("FROM Lancamento l ");
		sql.append("WHERE ");
		sql.append("     l.dataRecolhimentoPrevista = :dataRecolhimentoPrevista ");

		query = entityManager.createQuery(sql.toString());
		query.setParameter("dataRecolhimentoPrevista", input.getDataRecolhimento());
		
		@SuppressWarnings("unchecked")
		List<Lancamento> lancamentos = (List<Lancamento>) query.getResultList();
		Lancamento lancamento = null;
			
			if (lancamentos.isEmpty()) {
				
				lancamento = new Lancamento();
				lancamento.setDataCriacao(new Date());
				lancamento.setDataStatus(new Date());
				lancamento.setReparte(new BigDecimal(0));
				lancamento.setDataLancamentoDistribuidor(new Date());
				lancamento.setDataLancamentoPrevista(new Date());
				
				//FIXME 
		/*		lancamento.setDataRecolhimentoDistribuidor(new Date());
				lancamento.setStatus(StatusLancamento.CONFIRMADO);
				
				if (produtoEdicao.getProduto().isParcial()){
					
					lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
				}*/
				
				
				lancamento.setProdutoEdicao(produtoEdicao);
				lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());
				entityManager.persist(lancamento);
				
			} else {
				
				for (Lancamento lancamento2 : lancamentos) {
					
					if (lancamento2.getDataRecolhimentoPrevista().equals(input.getDataRecolhimento())) {
						
						lancamento = lancamento2;
					}				
				}
				
				ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Lancamento " + lancamento.getId());
				
				lancamento.setProdutoEdicao(produtoEdicao);
				lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());	
			}		
	}
}