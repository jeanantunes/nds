package br.com.abril.nds.integracao.ems0106.processor;

import java.math.BigInteger;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0106.inbound.EMS0106Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0106MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Override
	public void processMessage(Message message) {
		
		EMS0106Input input = (EMS0106Input) message.getBody();
		if (input == null) {
			
			this.ndsiLoggerFactory.getLogger().logError(
					message, EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
			return;
		}
		
		String codigoPublicacao = input.getCodigoPublicacao();
		Long edicao = input.getEdicao();
			
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoPublicacao,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU ProdutoEdicao");
			return;
		}
			
		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(
				produtoEdicao);
		if (lancamento == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU Lancamento");
			return;
		}
		
		Estudo estudo = lancamento.getEstudo();
		if (estudo == null) {
			
			// Cadastrar novo estudo:
			estudo = new Estudo();
			estudo.setQtdeReparte(BigInteger.valueOf(
					input.getReparteDistribuir()));
			estudo.setDataLancamento(lancamento.getDataLancamentoDistribuidor());
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
			getSession().persist(estudo);
			
			// Associar novo estudo com o lançamento existente:
			lancamento.setEstudo(estudo);
			getSession().merge(lancamento);
		} else {
			
			// Atualizar o valor total do reparte:
			estudo.setQtdeReparte(BigInteger.valueOf(
					input.getReparteDistribuir()));
			getSession().merge(estudo);
		}
		
	}
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Obtém o Lançamento com data de lançamento mais próximo do dia corrente.
	 *  
	 * @param produtoEdicao
	 * @return
	 */
	private Lancamento getLancamentoPrevistoMaisProximo(
			ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoDistribuidor >= :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoDistribuidor ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
}