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
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ContasAPagarConsignadoVO;
import br.com.abril.nds.dto.ImpressaoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
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
	
				hql += " and diferenca.tipoDiferenca = :tipoDiferenca ";
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
		
		Set<Diferenca> setDiferencas = new HashSet<Diferenca>();
		
		List<Diferenca> listaDiferencas = new ArrayList<Diferenca>();
		
		for (Object[] resultado : listaResultados) {

			Diferenca diferenca = (Diferenca) resultado[0];

			diferenca.setValorTotalDiferenca((BigDecimal) resultado[1]);
			
			diferenca.setExistemRateios(((Long) resultado[2]) > 0);
									
			diferenca.setQtde((BigInteger) resultado[3]);
			
			setDiferencas.add(diferenca);
		}
		
		listaDiferencas.addAll(setDiferencas);
		
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
			if(filtro.getTipoDiferenca().equals(TipoDiferenca.SOBRA_DE)) {
				query.setParameterList("tipoDiferenca", new TipoDiferenca[] {filtro.getTipoDiferenca(), TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA});
			} else if (filtro.getTipoDiferenca().equals(TipoDiferenca.SOBRA_EM)) {
				query.setParameterList("tipoDiferenca", new TipoDiferenca[] {filtro.getTipoDiferenca(), TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA});
			} else {
				query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
			}
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
}
