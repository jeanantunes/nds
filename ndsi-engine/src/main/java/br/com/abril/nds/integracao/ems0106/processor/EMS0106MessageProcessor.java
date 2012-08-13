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
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0106MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void processMessage(Message message) {
		
		EMS0106Input input = (EMS0106Input) message.getBody();

		if (input != null) {
		
			String codigoPublicacao = input.getCodigoPublicacao();
			Long edicao = input.getEdicao();
			
			List<ProdutoEdicao> listaProdutoEdicao =
				this.obterProdutoEdicaoPor(codigoPublicacao, edicao);
			
			if (listaProdutoEdicao == null || listaProdutoEdicao.isEmpty()) {
				this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU ProdutoEdicao OU Lancamento");
				return;
			}
			
			Lancamento lancamento = 
				this.getLancamento(codigoPublicacao, edicao);
		
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
	
	@SuppressWarnings("unchecked")
	private List<ProdutoEdicao> obterProdutoEdicaoPor(String codigoPublicacao, Long edicao) {
		
		try {
			
			Criteria criteria = 
				this.getSession().createCriteria(ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);
			
			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return criteria.list();
			
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
	private Lancamento getLancamento(String codigoPublicacao, Long edicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("			JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("			JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("	pe.numeroEdicao = :numeroEdicao ");
		sql.append("	AND p.codigo = :codigoProduto ");
		sql.append("	AND lcto.dataLancamentoPrevista >= current_date() ");
		sql.append("ORDER BY lcto.dataLancamentoPrevista DESC");

		Query query = getSession().createQuery(sql.toString());
		
		query.setMaxResults(1);

		query.setParameter("numeroEdicao", edicao);
		query.setParameter("codigoProduto", codigoPublicacao);

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