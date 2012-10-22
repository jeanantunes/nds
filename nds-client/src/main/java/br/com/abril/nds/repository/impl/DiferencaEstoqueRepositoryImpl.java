package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.estoque.Diferenca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class DiferencaEstoqueRepositoryImpl extends AbstractRepositoryModel<Diferenca, Long> implements DiferencaEstoqueRepository {

	/**
	 * Construtor.
	 */
	public DiferencaEstoqueRepositoryImpl() {
		
		super(Diferenca.class);
	}

	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, false);
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case CODIGO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.codigo ";
					break;
				case DESCRICAO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.nomeComercial ";
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
				case PRECO_DESCONTO:
					hql += "order by diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100)";
					break;
				case TIPO_DIFERENCA:
					hql += "order by diferenca.tipoDiferenca ";
					break;
				case VALOR_TOTAL_DIFERENCA:
					//+  " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100))) "
					hql += " order by "
						+  " case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
						+  " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
						+  " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100))) "
						+  " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
						+  " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100))) "
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
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.PENDENTE);
		
		if (filtro != null) {
		
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
		}
		
		List<Object[]> listaResultados = query.list();
		
		List<Diferenca> listaDiferencas = new ArrayList<Diferenca>();
		
		for (Object[] resultado : listaResultados) {
			
			Diferenca diferenca = (Diferenca) resultado[0];
			
			BigDecimal valorTotalDiferenca = (BigDecimal) resultado[1];
			
			listaDiferencas.add(new Diferenca(false, diferenca, valorTotalDiferenca));
		}
		
		return listaDiferencas;
	}
	
	public Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.PENDENTE);
		
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

			String innerHQLDesconto = this.obterHQLDesconto(null);  

			//+ " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * ("+ innerHQLDesconto +") / 100))) "
			hql = " select diferenca, "
				+ " (case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
				+ " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
				+ " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * ("+ innerHQLDesconto +") / 100))) "
				+ " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
				+ " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * ("+ innerHQLDesconto +") / 100))) "
				+ " else 0 end) as valorTotalDiferenca ";
		}

		hql += " from Diferenca diferenca " 
			+  " left join diferenca.produtoEdicao.produto.fornecedores fornecedor "
			+  " where diferenca.statusConfirmacao = :statusConfirmacao ";
		
		if (filtro != null) {
			
			if (filtro.getDataMovimento() != null) {

				hql += " and diferenca.dataMovimento = :dataMovimento ";
			}
			
			if (filtro.getTipoDiferenca() != null) {
	
				hql += " and diferenca.tipoDiferenca = :tipoDiferenca ";
			}
		}
		
		return hql;
	}

	private String obterHQLDesconto(Long idCota) {
		
		StringBuilder hql = new StringBuilder();
		
		if (idCota != null) {

			hql.append(" coalesce ( ")
			   .append(" coalesce(( ")
			   .append(" select view.desconto")
			   .append(" from ViewDesconto view ")
			   .append(" where view.cotaId = :idCota ")
			   .append(" and view.produtoEdicaoId = diferenca.produtoEdicao.id ")
			   .append(" and view.fornecedorId = fornecedor.id), diferenca.produtoEdicao.produto.desconto), 0) ");
		
		} else {

			hql.append(" coalesce (diferenca.produtoEdicao.produto.desconto, 0) ");
		}

		return hql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
										   Date dataLimiteLancamentoPesquisa) {
		
		String hql = this.gerarQueryDiferencas(filtro, dataLimiteLancamentoPesquisa, false);
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case DATA_LANCAMENTO_NUMERO_EDICAO:
					hql += "order by diferenca.dataMovimento, "
						 + " diferenca.produtoEdicao.numeroEdicao ";
					break;
				case DATA_LANCAMENTO:
					hql += "order by diferenca.dataMovimento";
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
				case PRECO_DESCONTO:
					hql += "order by diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100) ";
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
					hql += "order by diferenca.statusConfirmacao ";
					break;
				case VALOR_TOTAL_DIFERENCA:
					 //+ " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100))) "
					hql += " order by "
						 + " case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
						 + " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
						 + " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100))) "
						 + " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
						 + " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * desconto / 100))) "
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
		
		aplicarParametrosParaPesquisaDiferencas(filtro, dataLimiteLancamentoPesquisa, query);
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
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

			boolean existemRateios = ((Long) resultado[2]) > 0;

			listaDiferencas.add(new Diferenca(existemRateios, diferenca, valorTotalDiferenca));
		}
		
		return listaDiferencas;
	}
	
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
									 Date dataLimiteLancamentoPesquisa) {
		
		String hql = this.gerarQueryDiferencas(filtro, dataLimiteLancamentoPesquisa, true);
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaDiferencas(filtro, dataLimiteLancamentoPesquisa, query);
		
		return (Long) query.uniqueResult();
	}
	
	/*
	 * Gera a query de busca de diferenças.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param dataLimiteLancamentoPesquisa - Data limite de lançamento para realizar a pesquisa
	 * @param totalizar - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
										Date dataLimiteLancamentoPesquisa,
										boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(diferenca) ";
			
		} else {
			
			String innerHQLDesconto = this.obterHQLDesconto(filtro.getIdCota());  

			//+ " diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * ("+ innerHQLDesconto +") / 100))) "
			hql = " select diferenca, "
				+ " (case when (diferenca.tipoDiferenca = 'FALTA_DE' or "
				+ " diferenca.tipoDiferenca = 'SOBRA_DE') then ("
				+ " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * ("+ innerHQLDesconto +") / 100))) "
				+ " when (diferenca.tipoDiferenca = 'FALTA_EM' or diferenca.tipoDiferenca = 'SOBRA_EM') then ("
				+ " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * ("+ innerHQLDesconto +") / 100))) "
				+ " else 0 end) as valorTotalDiferenca, "
				+ " (select count(rateios) from RateioDiferenca rateios where rateios.diferenca.id = diferenca.id) ";
		}
		
		hql += " from Diferenca diferenca "
			 + " left join diferenca.itemRecebimentoFisico itemRecebimentoFisico "
			 + " left join diferenca.produtoEdicao.produto.fornecedores fornecedor "
			 + " left join itemRecebimentoFisico.itemNotaFiscal itemNotaFiscal "
			 + " left join itemNotaFiscal.notaFiscal notaFiscal ";
		
		if (filtro != null) {
		
			if (filtro.getIdCota() != null) {
				 hql += " join diferenca.rateios rateios ";
			}
			
			hql += " where diferenca.lancamentoDiferenca is not null "
				+ " and diferenca.statusConfirmacao = :statusConfirmacao ";
			
			if (dataLimiteLancamentoPesquisa != null) {
				hql += " and diferenca.produtoEdicao.id in " +
					   " 	(select distinct lancamento.produtoEdicao.id from Lancamento lancamento " +
					   " 		where lancamento.dataLancamentoDistribuidor >= :dataLimiteLancamentoPesquisa) ";
			}
				
			if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
				hql += " and diferenca.produtoEdicao.produto.codigo = :codigoProduto ";
			}
						
			if (filtro.getIdFornecedor() != null) {
				hql += " and fornecedor.id = :idFornecedor ";
			}
			
			if (filtro.getIdCota() != null) {
				hql += " and rateios.cota.id = :idCota ";
			}
						
			if (filtro.getPeriodoVO() != null
					&& filtro.getPeriodoVO().getDataInicial() != null
					&& filtro.getPeriodoVO().getDataFinal() != null) {
				
				hql += " and diferenca.dataMovimento between :dataInicial and :dataFinal ";
			}
			
			if (filtro.getTipoDiferenca() != null) {
				hql += " and diferenca.tipoDiferenca = :tipoDiferenca ";
			}
		}
		
		return hql;
	}
	
	/*
	 * Aplica os parâmetros para a busca de diferenças.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param dataLimiteLancamentoPesquisa - Data limite de lançamento para realizar a pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
														 Date dataLimiteLancamentoPesquisa,
													 	 Query query) {
		
		if (filtro == null) {
			
			return;
		}
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.CONFIRMADO);
		
		if (dataLimiteLancamentoPesquisa != null) {
			query.setParameter("dataLimiteLancamentoPesquisa", dataLimiteLancamentoPesquisa);
		}
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			query.setParameter("codigoProduto", filtro.getCodigoProduto());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getIdCota() != null) {
			query.setParameter("idCota", filtro.getIdCota());
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
		
		Boolean resultado = (Boolean) query.uniqueResult();
		return resultado == null ? Boolean.FALSE : resultado;
	}
	
	@Override
	public BigDecimal obterValorFinanceiroPorTipoDiferenca(TipoDiferenca tipoDiferenca){
		
		StringBuilder hql = new StringBuilder("select sum(diferenca.lancamentoDiferenca.movimentoEstoque.qtde) * sum(diferenca.produtoEdicao.precoVenda) ");
		hql.append(" from Diferenca diferenca, Distribuidor distribuidor ")
		   .append(" where diferenca.lancamentoDiferenca.movimentoEstoque.data = distribuidor.dataOperacao ")
		   .append(" and diferenca.tipoDiferenca = :tipoDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoDiferenca", tipoDiferenca);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigInteger obterQuantidadeTotalDiferencas(String codigoProduto, Long numeroEdicao,
									  				 TipoEstoque tipoEstoque, Date dataMovimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select sum( ")
			
			//.append(" (- diferenca.qtde * produtoEdicao.pacotePadrao) ")
			.append(" case when (diferenca.tipoDiferenca = 'FALTA_DE') then ")
			.append(" (- diferenca.qtde) ")
			.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then ")
			.append(" (- diferenca.qtde) ")
			
			//.append(" (diferenca.qtde * produtoEdicao.pacotePadrao) ")
			.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then ")
			.append(" (diferenca.qtde) ")
			.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then ")
			.append(" (diferenca.qtde) ")
			
			.append(" else 0 end")
			.append(" )")
			
			.append(" from Diferenca diferenca ")
			.append(" join diferenca.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" where produto.codigo = :codigoProduto ")
			.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ")
			.append(" and diferenca.tipoEstoque = :tipoEstoque ")
			.append(" and diferenca.dataMovimento = :dataMovimento ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("tipoEstoque", tipoEstoque);
		query.setParameter("dataMovimento", dataMovimento);
		
		return (BigInteger) query.uniqueResult();
	}
	
	@Override
	public boolean existeDiferencaPorNota(Long idProdutoEdicao, Date dataNotaEnvio,
										  Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select diferenca.id ")
			.append(" from Diferenca diferenca ")
			.append(" join diferenca.produtoEdicao produtoEdicao ")
			.append(" join diferenca.rateios rateio ")
			.append(" join rateio.cota cota ")
			.append(" where produtoEdicao.id = :idProdutoEdicao ")
			.append(" and diferenca.tipoDirecionamento = :tipoDirecionamento ")
			.append(" and rateio.dataNotaEnvio = :dataNotaEnvio ")
			.append(" and cota.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("tipoDirecionamento", TipoDirecionamentoDiferenca.NOTA);
		query.setParameter("dataNotaEnvio", dataNotaEnvio);
		query.setParameter("numeroCota", numeroCota);
		
		query.setMaxResults(1);
		
		return ((Long) query.uniqueResult() != null);
	}
	
}