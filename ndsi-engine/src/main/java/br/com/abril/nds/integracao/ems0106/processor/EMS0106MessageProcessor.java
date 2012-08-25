package br.com.abril.nds.integracao.ems0106.processor;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

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

		if (input != null) {
		
			String codigoPublicacao = input.getCodigoPublicacao();
			Long edicao = input.getEdicao();
			
			ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(
					codigoPublicacao, edicao);
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
		
			List<Estudo> listaEstudos = 
				this.getEstudosSalvos(
					lancamento.getProdutoEdicao().getId(), 
					lancamento.getDataLancamentoPrevista());
			
			if (listaEstudos.isEmpty()) {
				
				Estudo estudo = new Estudo();
		
				estudo.setProdutoEdicao(lancamento.getProdutoEdicao());
				estudo.setDataLancamento(lancamento.getDataLancamentoPrevista());
				estudo.setQtdeReparte(BigInteger.valueOf(input.getReparteDistribuir()));
		
				getSession().persist(estudo);
				///FIXME Comentado para verificação posterios junto a Eduardo "PunkRock" Castro em 08/08
/*		
				for (ProdutoEdicao produtoEdicao : listaProdutoEdicao) {
					
					estudo = new Estudo();
					
					estudo.setProdutoEdicao(produtoEdicao);
					estudo.setDataLancamento(lancamento.getDataLancamentoPrevista());
					estudo.setQtdeReparte(BigInteger.valueOf( input.getReparteDistribuir() ));
		
					getSession().persist(estudo);
				}
*/
			}
			
		} else {
			this.ndsiLoggerFactory.getLogger().logError(
				message, EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
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
	/*
	@SuppressWarnings("unchecked")
	private List<EMS0107Input> getListaCotaPublicacao(String codigoPublicacao, Long edicao) {

		// Obter a cota
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT e ");
		sql.append("FROM EMS0107Input e JOIN FETCH e.cota ");
		sql.append("WHERE ");
		sql.append("     e.codigoPublicacao = :codigoProduto ");
		sql.append("     AND e.edicao = :numeroEdicao ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("codigoProduto", codigoPublicacao);
		query.setParameter("numeroEdicao", edicao);

		return query.list();
	}
	*/
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
		sql.append("      AND lcto.dataLancamentoPrevista >= :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	private List<Estudo> getEstudosSalvos(Long idProdutoEdicao, Date dataLancamentoPrevista) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM Estudo e ");
		sql.append("	JOIN FETCH e.produtoEdicao pe ");
		sql.append("WHERE ");
		sql.append("	pe.id = :produtoEdicaoId ");
		sql.append("	AND e.dataLancamento = :dataLancamento ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("produtoEdicaoId", idProdutoEdicao);
		query.setParameter("dataLancamento", dataLancamentoPrevista);

		return query.list();
	}
	
}