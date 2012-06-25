package br.com.abril.nds.integracao.ems0114.processor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0114Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
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
	public void processMessage(Message message) {
				
		// Distribuidor unico para todo sistema
		if (verificarDistribuidor(message)) {
			
			ProdutoEdicao produtoEdicao = this.findProdutoEdicao(message);
				
			Lancamento lancamento = this.findLancamento(message);
				
			criarLancamentoConformeInput(lancamento, produtoEdicao, message);
			
		} else {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor nao encontrado.");
		}	
	}
	
	private void criarLancamentoConformeInput(Lancamento lancamento, ProdutoEdicao produtoEdicao, Message message) {
		EMS0114Input input = (EMS0114Input) message.getBody();
		
		if (lancamento != null) {
			
			if (!lancamento.getProdutoEdicao().getNumeroEdicao().equals(produtoEdicao.getNumeroEdicao())) {
				
				lancamento.setProdutoEdicao(produtoEdicao);
				ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
						"Atualizacao do Produto Edicao para " + produtoEdicao.getNumeroEdicao());
			}
			
			if (!lancamento.getDataRecolhimentoDistribuidor().equals(input.getDataRecolhimento())) {
				
				lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());
				ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
						"Atualizacao da Data de Recolhimento Distribuidor para " + input.getDataRecolhimento());
			}	
			
		} else {
			
			Calendar data = Calendar.getInstance();
			
			lancamento = new Lancamento();
			lancamento.setDataCriacao(data.getTime());
			lancamento.setDataStatus(data.getTime());
			lancamento.setReparte(new BigDecimal(0));
			lancamento.setDataLancamentoDistribuidor(data.getTime());
			lancamento.setDataLancamentoPrevista(data.getTime());
			lancamento.setStatus(StatusLancamento.EXPEDIDO);
			lancamento.setProdutoEdicao(produtoEdicao);
			
			if (produtoEdicao.isParcial()){
				
				lancamento.setTipoLancamento(TipoLancamento.PARCIAL);
				
			} else {
				
				lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
			}
				
			data.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
			lancamento.setDataRecolhimentoDistribuidor(data.getTime());
			
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());
			
			entityManager.persist(lancamento);
		}
	}
			
	private boolean verificarDistribuidor(Message message) {
		EMS0114Input input = (EMS0114Input) message.getBody();
		
		Integer codigoDistribuidorSistema = (Integer) message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR);
		Integer codigoDistribuidorArquivo = Integer.parseInt(input.getCodDistrib()); 
			
		if (codigoDistribuidorSistema.equals(codigoDistribuidorArquivo)) {
			return true;
		}
		
		return false;
	}
	
	private ProdutoEdicao findProdutoEdicao(Message message) {
		EMS0114Input input = (EMS0114Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT pe FROM ProdutoEdicao pe JOIN FETCH pe.produto p ");
		sql.append("WHERE pe.numeroEdicao = :numeroEdicao ");
		sql.append("  AND   p.codigo = :codigo ");
				
		try {
		
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("numeroEdicao", input.getEdicao());
			query.setParameter("codigo", input.getCodProd());
			
			return (ProdutoEdicao) query.getSingleResult();
			
		} catch (NoResultException e) {
			
			// Não encontrou o Produto. Realiza Log 			
			ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.HIERARQUIA, "Produto " +  input.getCodProd() + " e Produto Edicao nao encontrado.");
			throw new RuntimeException("Produto Edicao nao encontrado.");
		}
	}
	
	private Lancamento findLancamento(Message message) {
		EMS0114Input input = (EMS0114Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT l FROM Lancamento l ");
		sql.append("WHERE l.dataRecolhimentoPrevista = :dataRecolhimentoPrevista ");

		Query query = this.entityManager.createQuery(sql.toString());
		query.setParameter("dataRecolhimentoPrevista", input.getDataRecolhimento());
		
		@SuppressWarnings("unchecked")
		List<Lancamento> lancamentos = (List<Lancamento>) query.getResultList();
		
		Lancamento lancamento = null;
		
		if (!lancamentos.isEmpty()) {
			
			for (Lancamento lancamento2 : lancamentos) {
				
				if (lancamento2.getDataRecolhimentoPrevista().equals(input.getDataRecolhimento())) {
					
					lancamento = lancamento2;
				}				
			}
			
			return lancamento;
			
		} else {
			
			return null;
		}
	}
}