package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;

@Repository
public class MovimentoEstoqueRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> 
implements MovimentoEstoqueRepository {
	
	/**
	 * Construtor padrão.
	 */
	public MovimentoEstoqueRepositoryImpl() {
		super(MovimentoEstoque.class);
	}
	
		
	/**
	 * Obtém uma lista de extratoEdicao de acordo com statusAprovacao.
	 */
	//@Override
	@SuppressWarnings("unchecked")
	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(FiltroExtratoEdicaoDTO filtro, StatusAprovacao statusAprovacao) {

		String codigoProduto = filtro.getCodigoProduto();
		Long numeroEdicao = filtro.getNumeroEdicao();
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select new " + ExtratoEdicaoDTO.class.getCanonicalName() );		
		
		hql.append(" ( m.id, m.data, m.tipoMovimento.descricao, ");		
		
		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada then m.qtde else 0 end), ");

		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoSaida then m.qtde else 0 end) )  ");

		hql.append(" from MovimentoEstoque m ");		

		hql.append(" where m.produtoEdicao.numeroEdicao = :numeroEdicao and ");		

		hql.append(" m.produtoEdicao.produto.codigo = :codigoProduto ");		

		
		if(statusAprovacao != null) {
			hql.append(" and m.status = :statusAprovacao  ");
		}
		
		hql.append(" group by m.produtoEdicao.id, m.data, m.tipoMovimento.id ");		
		
		hql.append(" order by m.dataCriacao asc, m.id ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}
		
		query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameter("tipoOperacaoSaida", OperacaoEstoque.SAIDA);

		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameter("numeroEdicao", numeroEdicao);
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
				
	}
	
	@Override
	public BigInteger obterReparteDistribuidoProduto(String codigoProduto){
//		 select coalesce(sum(QTDE),0) from movimento_estoque where TIPO_MOVIMENTO_ID=13 and PRODUTO_EDICAO_ID=2350
		
		Query query = getSession().createQuery("select coalesce(sum(me.qtde),0) from MovimentoEstoque me " +
				" join me.tipoMovimento tm " +
				" join me.produtoEdicao " +
				" join me.produtoEdicao.produto produto " +
				"where produto.codigo = :produtoId " +
				"and tm.id = :tipoMovimentoId");
		
		query.setParameter("produtoId", codigoProduto);
		
		final long ENVIO_AO_JORNALEIRO = 13l; //13:Envio ao jornaleiro tabela tipo_movimento 
		query.setParameter("tipoMovimentoId", ENVIO_AO_JORNALEIRO);
		
		Object uniqueResult2 = query.uniqueResult();
		BigInteger uniqueResult = (BigInteger)uniqueResult2;
		return uniqueResult;
	}
}
