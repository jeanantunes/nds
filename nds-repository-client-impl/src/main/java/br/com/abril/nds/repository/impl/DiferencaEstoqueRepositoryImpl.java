package br.com.abril.nds.repository.impl;

import static org.apache.commons.lang.StringUtils.leftPad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ContasAPagarConsignadoVO;
import br.com.abril.nds.dto.ImpressaoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
					hql += "order by (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * (coalesce(diferenca.produtoEdicao.produto.desconto, 0)) / 100)) ";
					break;
				case TIPO_DIFERENCA:
					hql += "order by diferenca.tipoDiferenca ";
					break;
				case VALOR_TOTAL_DIFERENCA:
					hql += " order by "
						+ " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * (coalesce(diferenca.produtoEdicao.produto.desconto, 0)) / 100)) ";

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
			
				switch (filtro.getTipoDiferenca()) {
				
					case ALTERACAO_REPARTE_PARA_LANCAMENTO:
						query.setParameterList("tipoDiferenca", Arrays.asList(
								TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO,
								TipoDiferenca.ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS,
								TipoDiferenca.ALTERACAO_REPARTE_PARA_RECOLHIMENTO,
								TipoDiferenca.ALTERACAO_REPARTE_PARA_SUPLEMENTAR));
						break;
					
					case FALTA_EM:
						query.setParameterList("tipoDiferenca", Arrays.asList(
								TipoDiferenca.FALTA_EM,
								TipoDiferenca.FALTA_EM_DIRECIONADA_COTA,
								TipoDiferenca.AJUSTE_REPARTE_FALTA_COTA));
						break;

					case SOBRA_DE:
						query.setParameterList("tipoDiferenca", Arrays.asList(
								TipoDiferenca.SOBRA_DE,
								TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA));
						break;

						
					case SOBRA_EM:
						query.setParameterList("tipoDiferenca", Arrays.asList(
								TipoDiferenca.SOBRA_EM,
								TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA,
								TipoDiferenca.SOBRA_ENVIO_PARA_COTA));
						break;

						
					default:
						query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
						break;
				}
				
				
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
			
			switch (filtro.getTipoDiferenca()) {
			
				case ALTERACAO_REPARTE_PARA_LANCAMENTO:
					query.setParameterList("tipoDiferenca", Arrays.asList(
							TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO,
							TipoDiferenca.ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS,
							TipoDiferenca.ALTERACAO_REPARTE_PARA_RECOLHIMENTO,
							TipoDiferenca.ALTERACAO_REPARTE_PARA_SUPLEMENTAR));
					break;
				
				case FALTA_EM:
					query.setParameterList("tipoDiferenca", Arrays.asList(
							TipoDiferenca.FALTA_EM,
							TipoDiferenca.FALTA_EM_DIRECIONADA_COTA,
							TipoDiferenca.AJUSTE_REPARTE_FALTA_COTA));
					break;

				case SOBRA_DE:
					query.setParameterList("tipoDiferenca", Arrays.asList(
							TipoDiferenca.SOBRA_DE,
							TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA));
					break;

					
				case SOBRA_EM:
					query.setParameterList("tipoDiferenca", Arrays.asList(
							TipoDiferenca.SOBRA_EM,
							TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA,
							TipoDiferenca.SOBRA_ENVIO_PARA_COTA));
					break;

					
				default:
					query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
					break;
			}
			
			
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
				+ " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * (coalesce(diferenca.produtoEdicao.produto.desconto, 0)) / 100)) as valorTotalDiferenca ";
		}

		hql += " from Diferenca diferenca " 
			+  " left join diferenca.produtoEdicao.produto.fornecedores fornecedor "
			+  " where diferenca.statusConfirmacao = :statusConfirmacao ";
		
		if (filtro != null) {
			
			if (filtro.getDataMovimento() != null) {
				hql += " and diferenca.dataMovimento = :dataMovimento ";
			}
			
			if (filtro.getTipoDiferenca() != null) {
				
				if( TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO.equals(filtro.getTipoDiferenca()) ||
					TipoDiferenca.FALTA_EM.equals(filtro.getTipoDiferenca()) ||
					TipoDiferenca.SOBRA_DE.equals(filtro.getTipoDiferenca()) ||
					TipoDiferenca.SOBRA_EM.equals(filtro.getTipoDiferenca()) ) {
					
					hql += " and diferenca.tipoDiferenca in (:tipoDiferenca) ";
					
				} else {
					hql += " and diferenca.tipoDiferenca = :tipoDiferenca ";
				}
	
			}
		}
		
		return hql;
	}

	//TODO: Sérgio - Validar o parametro idCota pois pode quebrar a consulta 
	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencas(filtro, false);
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case DATA_LANCAMENTO_NUMERO_EDICAO:
					hql += "order by diferenca.dataMovimento, "
						 + " diferenca.produtoEdicao.numeroEdicao ";
					break;
				case DATA_LANCAMENTO:
					hql += "order by diferenca.dataMovimento ";
					break;
				case CODIGO_PRODUTO:
					hql += "order by lpad(diferenca.produtoEdicao.produto.codigo, 8 , '0') ";
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
					hql += "order by diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * diferenca.produtoEdicao.produto.desconto / 100) ";
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
					hql += " order by "
						 + " diferenca.qtde * (diferenca.produtoEdicao.precoVenda - (diferenca.produtoEdicao.precoVenda * diferenca.produtoEdicao.produto.desconto / 100)) ";
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

			diferenca.setValorTotalDiferenca((BigDecimal) resultado[1]);
			
			diferenca.setExistemRateios(((Long) resultado[2]) > 0);
									
			diferenca.setQtde((BigInteger) resultado[3]);
			
			listaDiferencas.add(diferenca);
		}
		
		return listaDiferencas;
	}
	
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
									 Date dataLimiteLancamentoPesquisa) {
		
		String hql = this.gerarQueryDiferencas(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaDiferencas(filtro, query);
		
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
	@SuppressWarnings("deprecation")
    private String gerarQueryDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
										boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(distinct diferenca) ";
			
		} else {
			
			
			String hqlCota = "";
			
			if (filtro.getNumeroCota()!=null){
				
				hqlCota += " ((select rd.qtde from Diferenca d join d.rateios rd where rd.cota.numeroCota = :numeroCota and d = diferenca) * coalesce(movimentoEstoqueCota.valoresAplicados.precoComDesconto,diferenca.produtoEdicao.precoVenda)) as valorTotalDiferenca ";
			}
			else{
			
				hqlCota += " (diferenca.qtde * coalesce(diferenca.produtoEdicao.precoVenda, 0)) as valorTotalDiferenca ";
			}
			
			hql = " select distinct(diferenca), "

			    + hqlCota + ", "
				
				+ " (select count(rateios) from RateioDiferenca rateios where rateios.diferenca.id = diferenca.id ";
			
				if (filtro.getNumeroCota()!=null){
				    
					hql += "  and rateios.cota.numeroCota = :numeroCota ";
				}
				
				hql +=  "), ";
				
				if (filtro.getNumeroCota()!=null){

					hql += " (select rd.qtde from Diferenca d join d.rateios rd where rd.cota.numeroCota = :numeroCota and d = diferenca) ";
				}
				else{

					hql += " diferenca.qtde ";
				}
		}
		
		hql += " from Diferenca diferenca "
			 + " left join diferenca.itemRecebimentoFisico itemRecebimentoFisico "
			 + " left join diferenca.produtoEdicao.produto.fornecedores fornecedor "
			 + " left join itemRecebimentoFisico.itemNotaFiscal itemNotaFiscal "
			 + " left join itemNotaFiscal.notaFiscal notaFiscal "
			 + " join diferenca.lancamentoDiferenca lancamentoDiferenca "
			 + " left join lancamentoDiferenca.movimentoEstoque movimentoEstoque"
			 + " left join diferenca.lancamentoDiferenca.movimentosEstoqueCota movimentoEstoqueCota";
		
		if (filtro != null) {
		
			if (filtro.getIdCota() != null || filtro.getNumeroCota()!=null) {
				 hql += " join diferenca.rateios rateios ";
			}
			
			hql += " where diferenca.lancamentoDiferenca is not null "
				+ " and diferenca.statusConfirmacao = :statusConfirmacao "
				+ " and (movimentoEstoque.status=:statusAprovado or movimentoEstoqueCota.status=:statusAprovado or movimentoEstoqueCota.status is null)";
				
			if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
				hql += " and diferenca.produtoEdicao.produto.codigo = :codigoProduto ";
			}
						
			if (filtro.getIdFornecedor() != null) {
				hql += " and fornecedor.id = :idFornecedor ";
			}
			
			if (filtro.getIdCota() != null) {
				hql += " and rateios.cota.id = :idCota ";
			}
			
            if (filtro.getNumeroCota()!=null){
				hql += " and rateios.cota.numeroCota = :numeroCota ";
			}
					
			if (filtro.getPeriodoVO() != null
					&& filtro.getPeriodoVO().getDataInicial() != null
					&& filtro.getPeriodoVO().getDataFinal() != null) {
				
				hql += " and diferenca.dataMovimento between :dataInicial and :dataFinal ";
			}
			
			if (filtro.getTipoDiferenca() != null) {
				hql += " and diferenca.tipoDiferenca in (:tipoDiferenca) ";
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
	@SuppressWarnings("deprecation")
    private void aplicarParametrosParaPesquisaDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
													 	 Query query) {
		
		if (filtro == null) {
			
			return;
		}
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.CONFIRMADO);
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			query.setParameter("codigoProduto", filtro.getCodigoProduto());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getIdCota() != null) {
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if (filtro.getNumeroCota()!=null){
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}
		
		if (filtro.getPeriodoVO() != null
				&& filtro.getPeriodoVO().getDataInicial() != null
				&& filtro.getPeriodoVO().getDataFinal() != null) {
			
			query.setParameter("dataInicial", filtro.getPeriodoVO().getDataInicial());
			query.setParameter("dataFinal", filtro.getPeriodoVO().getDataFinal());
		}
		
		if (filtro.getTipoDiferenca() != null) {
		    
		    final Set<TipoDiferenca> tiposDiferenca = new HashSet<TipoDiferenca>();
		    
		    switch (filtro.getTipoDiferenca()){
    		    case FALTA_DE:
    		        tiposDiferenca.add(TipoDiferenca.FALTA_DE);
    		        tiposDiferenca.add(TipoDiferenca.PERDA_DE);
    		    break;
    		    case FALTA_EM:
    		        tiposDiferenca.add(TipoDiferenca.FALTA_EM);
    		        tiposDiferenca.add(TipoDiferenca.FALTA_EM_DIRECIONADA_COTA);
    		        tiposDiferenca.add(TipoDiferenca.PERDA_EM);
    		    break;
    		    case SOBRA_DE:
                    tiposDiferenca.add(TipoDiferenca.SOBRA_DE);
                    tiposDiferenca.add(TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA);
                    tiposDiferenca.add(TipoDiferenca.GANHO_DE);
                break;
    		    case SOBRA_EM:
                    tiposDiferenca.add(TipoDiferenca.SOBRA_EM);
                    tiposDiferenca.add(TipoDiferenca.SOBRA_ENVIO_PARA_COTA);
                    tiposDiferenca.add(TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA);
                    tiposDiferenca.add(TipoDiferenca.GANHO_EM);
                break;
    		    case ALTERACAO_REPARTE_PARA_LANCAMENTO:
    		        tiposDiferenca.add(TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO);
    		        tiposDiferenca.add(TipoDiferenca.AJUSTE_REPARTE_FALTA_COTA);
                    tiposDiferenca.add(TipoDiferenca.ALTERACAO_REPARTE_PARA_RECOLHIMENTO);
                    tiposDiferenca.add(TipoDiferenca.ALTERACAO_REPARTE_PARA_SUPLEMENTAR);
                    tiposDiferenca.add(TipoDiferenca.ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS);
                break;
                default:
                    tiposDiferenca.add(filtro.getTipoDiferenca());
		    }
		    
			query.setParameterList("tipoDiferenca", tiposDiferenca);
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
			
			.append(" case when (diferenca.tipoDiferenca = 'FALTA_DE') then ")
			.append(" (- diferenca.qtde) ")
			.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then ")
			.append(" (- diferenca.qtde) ")
			.append(" when (diferenca.tipoDiferenca = 'PERDA_EM') then ")
			.append(" (- diferenca.qtde) ")
		
			.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then ")
			.append(" (diferenca.qtde) ")
			.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then ")
			.append(" (diferenca.qtde) ")
			.append(" when (diferenca.tipoDiferenca = 'GANHO_EM') then ")
			.append(" (diferenca.qtde) ")
			
			.append(" else 0 end) ")
			
			.append(" from Diferenca diferenca ")
			.append(" join diferenca.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" where produto.codigo = :codigoProduto ")
			.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ")
			.append(" and diferenca.tipoEstoque = :tipoEstoque ")
			.append(" and diferenca.dataMovimento = :dataMovimento ")
			.append(" and diferenca.statusConfirmacao=:statusConfirmacao");
		
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("tipoEstoque", tipoEstoque);
		query.setParameter("dataMovimento", dataMovimento);
		query.setParameter("statusConfirmacao", StatusConfirmacao.CONFIRMADO);
		
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

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
    @Override
    public List<Diferenca> obterDiferencas(Date data) {
        String hql = "from Diferenca diferenca where diferenca.dataMovimento = :data";
        Query query = getSession().createQuery(hql);
        query.setParameter("data", data);
        return query.list();
    }
	
	@SuppressWarnings("unchecked")
    @Override
	public List<Diferenca> obterDiferencas(Date data, StatusConfirmacao... statusConfirmacao) {
		
		String hql = " from Diferenca diferenca where diferenca.dataMovimento = :dataMovimento "
				   + " and diferenca.statusConfirmacao = :statusConfirmacao ";
		
		Query query = getSession().createQuery(hql);
     
		query.setParameter("dataMovimento", data);
		query.setParameterList("statusConfirmacao", statusConfirmacao);
     
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public List<Diferenca> obterDiferencas(Date dataMovimento, StatusConfirmacao statusConfirmacao) {
		
		String hql = " from Diferenca diferenca where diferenca.dataMovimento = :dataMovimento "
				   + " and diferenca.statusConfirmacao = :statusConfirmacao ";
		
        Query query = getSession().createQuery(hql);
        
        query.setParameter("dataMovimento", dataMovimento);
        query.setParameter("statusConfirmacao", statusConfirmacao);
        
        return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ImpressaoDiferencaEstoqueDTO> obterDadosParaImpressaoNaData(Date data) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct produtoEdicao as produtoEdicao, ")
		   .append(" sum( ")
		   .append(" coalesce( ")
		   .append(" case when (diferenca.tipoDiferenca = :faltaDe or diferenca.tipoDiferenca = :faltaEm) ")
		   .append(" then diferenca.qtde else 0 end ")
		   .append(" , 0)) as qtdeFaltas, ")
		   .append(" sum( ")
		   .append(" coalesce( ")
		   .append(" case when (diferenca.tipoDiferenca = :sobraDe or diferenca.tipoDiferenca = :sobraEm or ")
		   .append(" 	diferenca.tipoDiferenca = :sobraDeDirCota or diferenca.tipoDiferenca = :sobraEmDirCota) ")
		   .append(" then diferenca.qtde else 0 end ")
		   .append(" , 0)) as qtdeSobras, ")
		   .append(" diferenca.id as idDiferenca ")
		   .append(" from Lancamento lancamento ")
		   .append(" join lancamento.produtoEdicao produtoEdicao ")
		   .append(" left join produtoEdicao.diferencas diferenca ")
		   .append(" where lancamento.dataLancamentoDistribuidor = :dataBalanceamento ")
		   .append(" and lancamento.status not in (:statusLancamento) ")
		   .append(" and (diferenca.statusConfirmacao != :statusConfirmacao or diferenca.statusConfirmacao is null) ")
		   .append(" group by produtoEdicao.id ")
		   .append(" order by produtoEdicao.produto.nome, produtoEdicao.numeroEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataBalanceamento", data);

		query.setParameterList("statusLancamento", Arrays.asList(StatusLancamento.CONFIRMADO,
																 StatusLancamento.CANCELADO,
										                         StatusLancamento.PLANEJADO,
										                         StatusLancamento.EM_BALANCEAMENTO,
										                         StatusLancamento.FURO));
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.CANCELADO);
		query.setParameter("faltaDe", TipoDiferenca.FALTA_DE);
		query.setParameter("faltaEm", TipoDiferenca.FALTA_EM);
		
		query.setParameter("sobraDe", TipoDiferenca.SOBRA_DE);
		query.setParameter("sobraEm", TipoDiferenca.SOBRA_EM);
		query.setParameter("sobraDeDirCota", TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA);
		query.setParameter("sobraEmDirCota", TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA);
		
		query.setResultTransformer(
			new AliasToBeanResultTransformer(ImpressaoDiferencaEstoqueDTO.class));

		return query.list();
	}
	
	@Override
	public Long obterQuantidadeDadosParaImpressaoNaData(Date data) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(distinct produtoEdicao) ")
		   .append(" from Lancamento lancamento ")
		   .append(" join lancamento.produtoEdicao produtoEdicao ")
		   .append(" left join produtoEdicao.diferencas diferenca ")
		   .append(" where lancamento.dataLancamentoDistribuidor = :dataBalanceamento ")
		   .append(" and lancamento.status not in (:statusLancamento) ")
		   .append(" group by produtoEdicao.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataBalanceamento", data);
		
		query.setParameterList(
			"statusLancamento", Arrays.asList(StatusLancamento.CANCELADO,
					                          StatusLancamento.PLANEJADO,
					                          StatusLancamento.EM_BALANCEAMENTO,
					                          StatusLancamento.FURO));

		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public List<ContasAPagarConsignadoVO> pesquisarDiferncas(
            final String codigoProduto, final Long numeroEdicao, final Date data){
        
        final SQLQuery query = this.getSession().createSQLQuery(
                "select " +
                "  d.QTDE as diferenca, " +
                "  d.TIPO_DIFERENCA as motivo " +
                "from DIFERENCA d " +
                "  join PRODUTO_EDICAO pe ON (pe.ID = d.PRODUTO_EDICAO_ID) " +
                "  join PRODUTO p ON (p.ID = pe.PRODUTO_ID) " +
                "  join LANCAMENTO l ON (l.PRODUTO_EDICAO_ID = pe.ID) " +
                "where " +
                "  (p.CODIGO = :codigoProduto OR p.CODIGO_ICD = :codigoICD OR p.CODIGO = :codigoProdin) " +
                "  AND pe.NUMERO_EDICAO = :numeroEdicao " +
                "  AND l.DATA_REC_DISTRIB = :data " +
                "GROUP BY d.ID ");
        
        query.setParameter("codigoProduto", leftPad(codigoProduto, 8, "0"));
        query.setParameter("codigoICD", codigoProduto);
        query.setParameter("codigoProdin", leftPad(codigoProduto.concat("01"), 8, "0"));
        query.setParameter("numeroEdicao", numeroEdicao);
        query.setParameter("data", data);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarConsignadoVO.class));
        
        return query.list();
    }
	
	@Override
	public BigDecimal obterSaldoDaDiferencaDeEntradaDoConsignadoDoDistribuidor(final Date dataFechamento) {
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" coalesce(sum(diferenca_.QTDE * produtoEdicao_.PRECO_VENDA), 0) as valor "); 
		
		sql.append(" from DIFERENCA diferenca_ ");
		sql.append(" inner join LANCAMENTO_DIFERENCA lancamento_ on diferenca_.LANCAMENTO_DIFERENCA_ID = lancamento_.ID ");
		sql.append(" inner join PRODUTO_EDICAO produtoEdicao_ on diferenca_.PRODUTO_EDICAO_ID = produtoEdicao_.ID "); 
	    
		sql.append(" where diferenca_.DATA_MOVIMENTACAO = :dataMovimentacao "); 
		sql.append(" and diferenca_.TIPO_DIFERENCA in (:tiposDiferenca) ");
	    sql.append(" and diferenca_.PRODUTO_EDICAO_ID in ( ");
	    sql.append("		select distinct produtoEdicao.ID ");
	    sql.append("		from EXPEDICAO expedicaoProduto ");
	    sql.append("		inner join LANCAMENTO lancamentoProduto on expedicaoProduto.ID = lancamentoProduto.EXPEDICAO_ID  ");
	    sql.append("		inner join PRODUTO_EDICAO produtoEdicao on lancamentoProduto.PRODUTO_EDICAO_ID = produtoEdicao.ID "); 
	    sql.append("		inner join PRODUTO produto  on produtoEdicao.PRODUTO_ID = produto.ID "); 
	    sql.append("		where lancamentoProduto.STATUS <> :statusFuro ");
	    sql.append("		and produto.FORMA_COMERCIALIZACAO = :formaComercializacao ");
	    sql.append("		and data_lcto_distribuidor between date_add(:dataMovimentacao, interval -7 month) and date_add(:dataMovimentacao, interval -1 day) ");
	    sql.append(" ) ");
	    sql.append(" and diferenca_.PRODUTO_EDICAO_ID not in ( ");
	    sql.append(" 		select distinct produtoEdicao.ID ");
	    sql.append(" 		from EXPEDICAO expedicaoProduto ");
	    sql.append(" 		inner join LANCAMENTO lancamentoProduto on expedicaoProduto.ID = lancamentoProduto.EXPEDICAO_ID "); 
	    sql.append(" 		inner join PRODUTO_EDICAO produtoEdicao on lancamentoProduto.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
	    sql.append(" 		inner join PRODUTO produto  on produtoEdicao.PRODUTO_ID = produto.ID ");
	    sql.append(" 		where lancamentoProduto.STATUS <> :statusFuro ");
	    sql.append(" 		and produto.FORMA_COMERCIALIZACAO = :formaComercializacao ");
	    sql.append(" 		and data_lcto_distribuidor = :dataMovimentacao ");
	    sql.append(" ) ");
	    sql.append(" and diferenca_.id in ( ");
	    sql.append("	select distinct rateioDiferencaCota.DIFERENCA_ID from RATEIO_DIFERENCA rateioDiferencaCota "); 
	    sql.append(" ) ");
		
	    SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataMovimentacao", dataFechamento);
		query.setParameter("formaComercializacao", FormaComercializacao.CONSIGNADO.name());
		query.setParameter("statusFuro", StatusLancamento.FURO.name());
		query.setParameterList("tiposDiferenca", Arrays.asList( 
												   TipoDiferenca.FALTA_EM_DIRECIONADA_COTA.name(),
												   TipoDiferenca.FALTA_EM.name(),
												   TipoDiferenca.PERDA_EM.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_RECOLHIMENTO.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_SUPLEMENTAR.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS.name()));
		
		query.addScalar("valor",StandardBasicTypes.BIG_DECIMAL);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterSaldoDaDiferencaDeSaidaDoConsignadoDoDistribuidor(final Date dataFechamento) {
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" coalesce(sum(diferenca_.QTDE*produtoEdicao_.PRECO_VENDA),0) as valor ");
		
		sql.append(" from DIFERENCA diferenca_ ");
		sql.append(" inner join LANCAMENTO_DIFERENCA lancamento_  on diferenca_.LANCAMENTO_DIFERENCA_ID=lancamento_.ID ");
		sql.append(" inner join PRODUTO_EDICAO produtoEdicao_ on diferenca_.PRODUTO_EDICAO_ID=produtoEdicao_.ID "); 
	    
		sql.append(" where diferenca_.DATA_MOVIMENTACAO=:dataMovimentacao ");
		sql.append(" and diferenca_.TIPO_DIFERENCA in (:tiposDiferenca) ");
	    
		sql.append(" and diferenca_.PRODUTO_EDICAO_ID in ( ");
		sql.append("		select distinct produtoEdicao.ID "); 
		sql.append("		from EXPEDICAO expedicaoProduto ");
		sql.append("		inner join LANCAMENTO lancamentoProduto  on expedicaoProduto.ID=lancamentoProduto.EXPEDICAO_ID  ");
		sql.append("		inner join PRODUTO_EDICAO produtoEdicao on lancamentoProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID "); 
		sql.append("		inner join PRODUTO produto  on produtoEdicao.PRODUTO_ID=produto.ID "); 
		sql.append("		where lancamentoProduto.STATUS<>:statusFuro ");
		sql.append("		and   lancamentoProduto.DATA_LCTO_DISTRIBUIDOR <:dataMovimentacao ");
		sql.append("		and   produto.FORMA_COMERCIALIZACAO=:formaComercializacao ");
		
		sql.append(" ) ");
		sql.append(" and diferenca_.id in ( ");
		sql.append("	select  distinct rateioDiferencaCota.DIFERENCA_ID from  RATEIO_DIFERENCA rateioDiferencaCota "); 
		sql.append(" ) ");        
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataMovimentacao", dataFechamento);
		query.setParameter("formaComercializacao", FormaComercializacao.CONSIGNADO.name());
		query.setParameter("statusFuro", StatusLancamento.FURO.name());
		query.setParameterList("tiposDiferenca", Arrays.asList( 
												   TipoDiferenca.SOBRA_EM.name(),
												   TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA.name(),
												   TipoDiferenca.GANHO_EM.name()));
		
		query.addScalar("valor",StandardBasicTypes.BIG_DECIMAL);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterSaldoDaDiferencaDeSaidaDoConsignadoDoDistribuidorNoDia(final Date dataFechamento, boolean precoCapaHistoricoAlteracao) {
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		
		if(precoCapaHistoricoAlteracao) {
			
			sql.append(" coalesce(sum( if(diferenca_.TIPO_DIFERENCA IN ('SOBRA_EM','SOBRA_EM_DIRECIONADA_COTA','GANHO_EM') ");
			sql.append(" 				, diferenca_.QTDE * (coalesce((select valor_atual ");
			sql.append("		from historico_alteracao_preco_venda ");
			sql.append("		where id = (select max(id) ");
			sql.append("					from historico_alteracao_preco_venda hapv ");
			sql.append("					where hapv.PRODUTO_EDICAO_ID = produtoEdicao_.ID ");
			sql.append("					and diferenca_.DATA_MOVIMENTACAO <> hapv.data_operacao ");
			sql.append("					)), produtoEdicao_.PRECO_VENDA)) ");
			sql.append(" 				, diferenca_.QTDE * (coalesce((select valor_atual ");
			sql.append("		from historico_alteracao_preco_venda ");
			sql.append("		where id = (select max(id) ");
			sql.append("					from historico_alteracao_preco_venda hapv ");
			sql.append("					where hapv.PRODUTO_EDICAO_ID = produtoEdicao_.ID");
			sql.append("					and diferenca_.DATA_MOVIMENTACAO <> hapv.data_operacao ");
			sql.append("					)), produtoEdicao_.PRECO_VENDA))*-1) ");  
			sql.append(" 		  ),0) as valor  ");
			
		} else {
			
			sql.append(" coalesce(sum( if(diferenca_.TIPO_DIFERENCA IN ('SOBRA_EM','SOBRA_EM_DIRECIONADA_COTA','GANHO_EM') ");
			sql.append(" 				,diferenca_.QTDE * produtoEdicao_.PRECO_VENDA ");
			sql.append(" 				,diferenca_.QTDE * produtoEdicao_.PRECO_VENDA*-1) ");  
			sql.append(" 		  ),0) as valor  ");
		}
		
		
		sql.append(" from DIFERENCA diferenca_ ");
		sql.append(" inner join LANCAMENTO_DIFERENCA lancamento_  on diferenca_.LANCAMENTO_DIFERENCA_ID=lancamento_.ID ");
		sql.append(" inner join PRODUTO_EDICAO produtoEdicao_ on diferenca_.PRODUTO_EDICAO_ID=produtoEdicao_.ID "); 
	    
		// sql.append(" where diferenca_.DATA_MOVIMENTACAO=:dataMovimentacao ");
		
		sql.append(" where diferenca_.DATA_MOVIMENTACAO in (");
		sql.append("		select distinct date(DATA_EXPEDICAO) "); 
		sql.append("		from EXPEDICAO expedicaoProduto ");
		sql.append("		inner join LANCAMENTO lancamentoProduto  on expedicaoProduto.ID=lancamentoProduto.EXPEDICAO_ID  ");
		sql.append("		inner join PRODUTO_EDICAO produtoEdicao on lancamentoProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID "); 
		sql.append("		inner join PRODUTO produto  on produtoEdicao.PRODUTO_ID=produto.ID "); 
		sql.append("		where lancamentoProduto.STATUS<>:statusFuro ");
		sql.append("		and   lancamentoProduto.DATA_LCTO_DISTRIBUIDOR =:dataMovimentacao ");
		sql.append("		and   produto.FORMA_COMERCIALIZACAO=:formaComercializacao ");
		sql.append(" 		GROUP BY DATA_EXPEDICAO) ");
		
		sql.append(" and diferenca_.TIPO_DIFERENCA in (:tiposDiferenca) ");
		sql.append(" and diferenca_.PRODUTO_EDICAO_ID in ( ");
		sql.append("		select distinct produtoEdicao.ID "); 
		sql.append("		from EXPEDICAO expedicaoProduto ");
		sql.append("		inner join LANCAMENTO lancamentoProduto  on expedicaoProduto.ID=lancamentoProduto.EXPEDICAO_ID  ");
		sql.append("		inner join PRODUTO_EDICAO produtoEdicao on lancamentoProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID "); 
		sql.append("		inner join PRODUTO produto  on produtoEdicao.PRODUTO_ID=produto.ID "); 
		sql.append("		where lancamentoProduto.STATUS<>:statusFuro ");
		sql.append("		and   lancamentoProduto.DATA_LCTO_DISTRIBUIDOR =:dataMovimentacao ");
		sql.append("		and   produto.FORMA_COMERCIALIZACAO=:formaComercializacao ");
		
		sql.append(" ) ");
		sql.append(" and diferenca_.id in ( ");
		sql.append("	select  distinct rateioDiferencaCota.DIFERENCA_ID from  RATEIO_DIFERENCA rateioDiferencaCota "); 
		sql.append(" ) ");        
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataMovimentacao", dataFechamento);
		query.setParameter("formaComercializacao", FormaComercializacao.CONSIGNADO.name());
		query.setParameter("statusFuro", StatusLancamento.FURO.name());
		query.setParameterList("tiposDiferenca", Arrays.asList( 
												   TipoDiferenca.SOBRA_EM.name(),
												   TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA.name(),
												   TipoDiferenca.GANHO_EM.name(),
												   TipoDiferenca.FALTA_EM_DIRECIONADA_COTA.name(),
												   TipoDiferenca.FALTA_EM.name(),
												   TipoDiferenca.PERDA_EM.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_RECOLHIMENTO.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_SUPLEMENTAR.name(),
												   TipoDiferenca.ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS.name()));
		
		query.addScalar("valor",StandardBasicTypes.BIG_DECIMAL);
		
		return (BigDecimal) query.uniqueResult();
	}
}
