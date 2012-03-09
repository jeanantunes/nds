package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.estoque.Diferenca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class DiferencaEstoqueRepositoryImpl extends AbstractRepository<Diferenca, Long> implements DiferencaEstoqueRepository {

	/**
	 * Construtor.
	 */
	public DiferencaEstoqueRepositoryImpl() {
		
		super(Diferenca.class);
	}

	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, false);
		
		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case CODIGO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.codigo ";
					break;
				case DESCRICAO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.descricao ";
					break;
				case QUANTIDADE:
					hql += "order by diferenca.qtde ";
					break;
				case NUMERO_EDICAO:
					hql += "order by diferenca.produtoEdicao.numeroEdicao ";
					break;
				case PACOTE_PADRAO:
					hql += "order by diferenca.produtoEdicao.pacotePadrao ";
					break;
				case PRECO_VENDA:
					hql += "order by diferenca.produtoEdicao.precoVenda ";
					break;
				case TIPO_DIFERENCA:
					hql += "order by diferenca.tipoDiferenca ";
					break;
				case VALOR_TOTAL_DIFERENCA:
					hql += " order by "
						+  " case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
						+  " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
						+  " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) "
						+  " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
						+  " diferenca.qtde * diferenca.produtoEdicao.precoVenda) "
						+  " else 0 end ";
					break;
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		Query query = super.getSession().createQuery(hql);
		
		if (filtro.getDataMovimento() != null) {
			
			query.setParameter("dataMovimento", filtro.getDataMovimento());
		}
		
		if (filtro.getTipoDiferenca() != null) {
		
			query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
		}
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		List<Object[]> listaResultados = query.list();
		
		List<Diferenca> listaDiferencas = new ArrayList<Diferenca>();
		
		for (Object[] resultado : listaResultados) {
			
			Diferenca diferenca = (Diferenca) resultado[0];
			
			BigDecimal valorTotalDiferenca = (BigDecimal) resultado[1];
			
			listaDiferencas.add(new Diferenca(diferenca, valorTotalDiferenca));
		}
		
		return listaDiferencas;
	}
	
	public Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		if (filtro.getDataMovimento() != null) {
			
			query.setParameter("dataMovimento", filtro.getDataMovimento());
		}
		
		if (filtro.getTipoDiferenca() != null) {
		
			query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
		}
		
		return (Long) query.uniqueResult();
	}
	
	/*
	 * Gera a query de busca de diferenças para lançamento.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param totalizar - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro, 
												  boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(diferenca) ";
			
		} else {

			hql = " select diferenca, "
				+ " (case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
				+ " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
				+ " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) "
				+ " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
				+ " diferenca.qtde * diferenca.produtoEdicao.precoVenda) "
				+ " else 0 end) as valorTotalDiferenca ";
		}
					
		hql += " from Diferenca diferenca "
			+  " left join diferenca.movimentoEstoque movimentoEstoque ";
		
		boolean whereUtilizado = false;
		
		if (filtro.getDataMovimento() != null) {
			
			hql += (!whereUtilizado) ? " where " : " and ";
						
			hql += " movimentoEstoque.dataInclusao = :dataMovimento ";
			
			whereUtilizado = true;
		}
		
		if (filtro.getTipoDiferenca() != null) {
			
			hql += (!whereUtilizado) ? " where " : " and ";
			
			hql += " diferenca.tipoDiferenca = :tipoDiferenca ";
			
			whereUtilizado = true;
		}
		
		return hql;
	}

	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencas(filtro, false);
		
		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case DATA_LANCAMENTO_NUMERO_EDICAO:
					hql += "order by diferenca.movimentoEstoque.dataInclusao, "
						 + " diferenca.produtoEdicao.numeroEdicao ";
					break;
				case DATA_LANCAMENTO:
					hql += "order by diferenca.movimentoEstoque.dataInclusao ";
					break;
				case CODIGO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.codigo ";
					break;
				case DESCRICAO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.nome ";
					break;
				case NUMERO_EDICAO:
					hql += "order by diferenca.produtoEdicao.numeroEdicao ";
					break;
				case PRECO_VENDA:
					hql += "order by diferenca.produtoEdicao.precoVenda ";
					break;
				case TIPO_DIFERENCA:
					hql += "order by diferenca.tipoDiferenca ";
					break;
				case NUMERO_NOTA_FISCAL:
					hql += "order by notaFiscal.numero ";
					break;
				case QUANTIDADE:
					hql += "order by diferenca.qtde ";
					break;
				case STATUS_APROVACAO:
					hql += "order by diferenca.tipoDiferenca ";
					break;
				case VALOR_TOTAL_DIFERENCA:
					hql += " order by "
						 + " case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
						 + " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
						 + " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) "
						 + " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
						 + " diferenca.qtde * diferenca.produtoEdicao.precoVenda) "
						 + " else 0 end ";
					break;
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaDiferencas(filtro, query);
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		List<Object[]> listaResultados = query.list();
		
		List<Diferenca> listaDiferencas = new ArrayList<Diferenca>();
		
		for (Object[] resultado : listaResultados) {
			
			Diferenca diferenca = (Diferenca) resultado[0];
			
			BigDecimal valorTotalDiferenca = (BigDecimal) resultado[1];
			
			listaDiferencas.add(new Diferenca(diferenca, valorTotalDiferenca));
		}
		
		return listaDiferencas;
	}
	
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencas(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaDiferencas(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	/*
	 * Gera a query de busca de diferenças.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param totalizar - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro, 
										boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(diferenca) ";
			
		} else {
			
			hql = " select diferenca, "
				+ " (case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
				+ " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
				+ " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) "
				+ " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
				+ " diferenca.qtde * diferenca.produtoEdicao.precoVenda) "
				+ " else 0 end) as valorTotalDiferenca ";
		}
		
		hql += " from Diferenca diferenca "
			 + " left join diferenca.itemRecebimentoFisico itemRecebimentoFisico "
			 + " left join itemRecebimentoFisico.itemNotaFiscal itemNotaFiscal "
			 + " left join itemNotaFiscal.notaFiscal notaFiscal ";
		
		if (filtro.getIdFornecedor() != null) {
			 
			 hql += " join diferenca.produtoEdicao.produto.fornecedores fornecedores ";
		}
			 
		hql += " where diferenca.movimentoEstoque is not null ";
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql += " and diferenca.produtoEdicao.produto.codigo = :codigoProduto ";
		}
		
		if (filtro.getNumeroEdicao() != null) {
			hql += " and diferenca.produtoEdicao.numeroEdicao = :numeroEdicao ";
		}
		
		if (filtro.getIdFornecedor() != null) {
			hql += " and fornecedores.id = :idFornecedor ";
		}
		
		if (filtro.getPeriodoVO() != null
				&& filtro.getPeriodoVO().getDataInicial() != null
				&& filtro.getPeriodoVO().getDataFinal() != null) {
			
			hql += " and diferenca.movimentoEstoque.dataInclusao between :dataInicial and :dataFinal ";
		}
		
		if (filtro.getTipoDiferenca() != null) {
			hql += " and diferenca.tipoDiferenca = :tipoDiferenca ";
		}
		
		return hql;
	}
	
	/*
	 * Aplica os parâmetros para a busca de diferenças.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro, 
													 	 Query query) {
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			query.setParameter("codigoProduto", filtro.getCodigoProduto());
		}
		
		if (filtro.getNumeroEdicao() != null) {
			query.setParameter("numeroEdicao", filtro.getNumeroEdicao());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getPeriodoVO() != null
				&& filtro.getPeriodoVO().getDataInicial() != null
				&& filtro.getPeriodoVO().getDataFinal() != null) {
			
			query.setParameter("dataInicial", filtro.getPeriodoVO().getDataInicial());
			query.setParameter("dataFinal", filtro.getPeriodoVO().getDataFinal());
		}
		
		if (filtro.getTipoDiferenca() != null) {
			query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
		}
	}
	
	/*
	 * @see br.com.abril.nds.repository.DiferencaEstoqueRepository#buscarStatusDiferencaLancadaAutomaticamente(java.lang.Long)
	 */
	public boolean buscarStatusDiferencaLancadaAutomaticamente(Long idDiferenca){
		Query query = 
				this.getSession().createQuery("select d.automatica from Diferenca d where d.id = :idDiferenca");
		query.setParameter("idDiferenca", idDiferenca);
		
		return (Boolean) query.uniqueResult();
	}

}
