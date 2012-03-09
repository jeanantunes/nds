package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ExpedicaoRepository;

/**
 * Classe responsável por implementar as funcionalidades referente a expedição de lançamentos de produtos.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class ExpedicaoRepositoryImpl extends AbstractRepository<Expedicao,Long> implements ExpedicaoRepository {

	public ExpedicaoRepositoryImpl() {
		super(Expedicao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(gerarQueryResumoProduto(Boolean.FALSE))
					
		.append(getOrderBy(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list(); 
		
	}
	
	/**
	 * Retorna o sql referente a consulta de Reusumo de produtos expedidos
	 * @param isCount
	 * @return String
	 */
	private String gerarQueryResumoProduto(boolean isCount){
		
		StringBuilder hql = new StringBuilder();
		
		if (isCount){
			
			hql.append("SELECT count (produto.codigo) ");
		}
		else{
		
			hql.append("SELECT new ") .append(ExpedicaoDTO.class.getCanonicalName()) 
			.append(" ( ") 
						.append("produto.codigo,")
						.append("produto.nome,")
						.append("produtoEd.numeroEdicao,")
						.append("produtoEd.precoVenda,")
						.append("estudo.qtdeReparte,")
						.append(" SUM (( case ")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde * produtoEd.pacotePadrao)")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
							.append(" else 0")
						.append(" end )) as qntDiferenca, ")
						.append("produtoEd.precoVenda*estudo.qtdeReparte ")
			.append(" ) ");
		}	
		
		hql.append( "FROM" )
			.append( " Estudo estudo join estudo.lancamento lancamento ") 
			.append( " JOIN lancamento.recebimentos itemRecebimento ")
			.append("  JOIN lancamento.expedicao expedicao ")
			.append( " JOIN itemRecebimento.diferenca diferenca")
			.append( " JOIN diferenca.produtoEdicao produtoEd ")
			.append( " JOIN produtoEd.produto produto ")
			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor =:dataLancamento ")
			.append(" and lancamento.status =:status ");
		
		hql.append(" group by ")
			.append("produto.codigo,")
			.append("produto.nome,")
			.append("produtoEd.numeroEdicao,")
			.append("produtoEd.precoVenda,")
			.append("estudo.qtdeReparte,")
			.append("produtoEd.precoVenda*estudo.qtdeReparte ");

		return hql.toString();
	}
	
	/**
	 * Retorna uma string com o conteudo da ordenação da consulta
	 * @param filtro
	 * @return
	 */
	private String getOrderBy(FiltroResumoExpedicaoDTO filtro ){
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColunaProduto() != null ){
			
			switch (filtro.getOrdenacaoColunaProduto()) {
				
				case CODIGO_PRODUTO:
					hql.append(" ORDER BY produto.codigo ");
					break;
				case DESCRICAO_PRODUTO:
					hql.append(" ORDER BY produto.nome ");
					break;
				case NUMERO_EDICAO:
					hql.append(" ORDER BY produtoEd.numeroEdicao ");
					break;
				case PRECO_CAPA:
					hql.append(" ORDER BY produtoEd.precoVenda ");
					break;
				case REPARTE:
					hql.append(" ORDER BY estudo.qtdeReparte ");
					break;
				case DIFERENCA:
					hql.append(" ORDER BY ")
					.append(" sum( ( case ")
						.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
						.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde *  produtoEd.pacotePadrao)")
						.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
						.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
						.append(" else 0")
					.append(" end ) )");
					break;
				case VALOR_FATURADO:
					hql.append(" ORDER BY  produtoEd.precoVenda*estudo.qtdeReparte ");
					break;
				default:
					hql.append(" ORDER BY produto.codigo ");
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append( filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		return hql.toString();
	}

	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorBox(FiltroResumoExpedicaoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createQuery(gerarQueryResumoProduto(Boolean.TRUE));
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		
		@SuppressWarnings("unchecked")
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
		
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorBox(FiltroResumoExpedicaoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

}
