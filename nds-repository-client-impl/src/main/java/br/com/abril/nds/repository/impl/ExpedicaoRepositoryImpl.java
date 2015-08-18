package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ExpedicaoRepository;

/**
 * Classe responsável por implementar as funcionalidades referente a expedição
 * de lançamentos de produtos.
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class ExpedicaoRepositoryImpl extends AbstractRepositoryModel<Expedicao,Long> implements ExpedicaoRepository {

	public ExpedicaoRepositoryImpl() {
		super(Expedicao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(this.getHqlResumoLancamento(false, false, filtro));

		hql.append(getOrderBy(filtro));
		
		Query query = this.getQueryResumoLancamentoPorBox(hql);
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return query.list(); 
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long obterQuantidadeResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(getHqlResumoLancamento(false, false, filtro));
		
		Query query = getSession().createSQLQuery(hql.toString());
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);
		
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
	}
	
	@Override
    public ExpedicaoDTO obterTotaisResumoExpedicaoPorProduto(boolean isAgrupamentoPorBox, 
			                                                 boolean isDetalhesResumo,
			                                                 FiltroResumoExpedicaoDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append(" SUM( resumoExpedicaoPorProduto.qntReparte ) as qntReparte, ");

		sql.append(" SUM( resumoExpedicaoPorProduto.totalValorFaturado ) as valorFaturado ");
		
		sql.append(" FROM (");
		
		sql.append("     SELECT SUM(innerQuery.qntReparte) + COALESCE( ")
		.append(this.getQntDiferencaResumoLancamento()).append(", 0) + ")
		.append(this.getQntCotaAusente(false)).append(" as qntReparte, ");

		sql.append("            innerQuery.precoCapa * ( SUM(innerQuery.qntReparte) + COALESCE( ")
		.append(this.getQntDiferencaResumoLancamento()).append(", 0) + ")
		.append(this.getQntCotaAusente(false)).append(" ) AS totalValorFaturado ");
		
		sql.append("     FROM ");
		sql.append("     ( ");
		sql.append(        this.getInnerQueryResumoLancamento(isDetalhesResumo));
		sql.append("     ) as innerQuery ");
		sql.append(this.getAgrupamentoResumoLancamento(isAgrupamentoPorBox, isDetalhesResumo, filtro));
        sql.append(" ) as resumoExpedicaoPorProduto ");
        
		Query query = getSession().createSQLQuery(sql.toString())
			.addScalar("qntReparte", StandardBasicTypes.BIG_INTEGER)
			.addScalar("valorFaturado");
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return (ExpedicaoDTO) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ExpedicaoDTO> obterResumoExpedicaoPorBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getHqlResumoLancamento(true, false, filtro));
		
		hql.append(this.ordenarConsultaPorBox(filtro));
		
		Query query = this.getQueryResumoLancamentoPorBox(hql);
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return query.list();
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public List<ExpedicaoDTO> obterResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getHqlResumoLancamento(false, true, filtro));
					
		hql.append(getOrderBy(filtro));
		
		Query query = getSession().createSQLQuery(hql.toString())
			.addScalar("codigoProduto")
			.addScalar("nomeProduto")
			.addScalar("numeroEdicao", StandardBasicTypes.LONG)
			.addScalar("precoCapa")
			.addScalar("desconto")
			.addScalar("qntReparte", StandardBasicTypes.BIG_INTEGER)
			.addScalar("qntDiferenca", StandardBasicTypes.BIG_INTEGER)
			.addScalar("valorFaturado")
			.addScalar("razaoSocial");
		
		this.setParametersQueryResumoExpedicaoProdutosDoBox(filtro, query);
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return query.list();
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public Long obterQuantidadeResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createSQLQuery(getHqlResumoLancamento(false, true, filtro));
		
		this.setParametersQueryResumoExpedicaoProdutosDoBox(filtro, query);
		
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
	}
	
	@Override
    public ExpedicaoDTO obterTotaisResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");

		sql.append(" SUM( resumoExpedicaoPorBox.valorFaturado ) as valorFaturado ");
		
		sql.append(" FROM (");

		sql.append("     SELECT ");		
		sql.append("            innerQuery.precoCapa * ( SUM(innerQuery.qntReparte) + COALESCE( ")
															.append(this.getQntDiferencaResumoLancamento()).append(", 0) - ")
															.append(this.getQntCotaAusente(false)).append(" ) AS valorFaturado ");
		sql.append("     FROM ");
			
		sql.append("     ( ");
	
		sql.append(        this.getInnerQueryResumoLancamento(false));
			
		sql.append("     ) as innerQuery ");

		sql.append(this.getAgrupamentoResumoLancamento(false, true, filtro));

        sql.append(" ) as resumoExpedicaoPorBox ");

		Query query = getSession().createSQLQuery(sql.toString()).addScalar("valorFaturado");
		
		this.setParametersQueryResumoExpedicaoProdutosDoBox(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return (ExpedicaoDTO) query.uniqueResult();
	}

	private Query getQueryResumoLancamentoPorBox(StringBuilder hql) {
		
		Query query = getSession().createSQLQuery(hql.toString())
			.addScalar("dataLancamento")
			.addScalar("idBox", StandardBasicTypes.LONG)
			.addScalar("codigoBox",StandardBasicTypes.LONG)
			.addScalar("nomeBox")
			.addScalar("precoCapa")
			.addScalar("qntReparte", StandardBasicTypes.BIG_INTEGER)
			.addScalar("qntDiferenca", StandardBasicTypes.BIG_INTEGER)
			.addScalar("valorFaturado")
			.addScalar("codigoProduto").addScalar("nomeProduto")
			.addScalar("numeroEdicao", StandardBasicTypes.LONG)
			.addScalar("qntProduto", StandardBasicTypes.LONG);
		
		return query;
	}
	
	private void setParametersQueryResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro, Query query) {
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		
		query.setParameterList("grupoMovimentoEstoque", Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
																  	  GrupoMovimentoEstoque.RECEBIMENTO_REPARTE_CONTA_FIRME.name()));
		
		query.setParameterList("statusAposExpedido", 
							   Arrays.asList(StatusLancamento.EXPEDIDO.name(),
											 StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name(),
											 StatusLancamento.BALANCEADO_RECOLHIMENTO.name(),											 
											 StatusLancamento.EM_RECOLHIMENTO.name(),
											 StatusLancamento.RECOLHIDO.name(),
											 StatusLancamento.FECHADO.name()));
	}
	
	private void setParametersQueryResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro, Query query) {
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);

        if (filtro.getCodigoBox() != null) {
			query.setParameter("codigoBox", filtro.getCodigoBox());
        }
	}

	        /**
     * Retorna uma string com o conteudo da ordenação da consulta
     * 
     * @param filtro
     * @return
     */
	private String getOrderBy(FiltroResumoExpedicaoDTO filtro ){
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColunaProduto() != null ){
			
			hql.append(" ORDER BY ");
			
			switch (filtro.getOrdenacaoColunaProduto()) {
				
				case CODIGO_PRODUTO:
					hql.append(" codigoProduto ");
					break;
				case DESCRICAO_PRODUTO:
					hql.append(" nomeProduto ");
					break;
				case NUMERO_EDICAO:
					hql.append(" numeroEdicao ");
					break;
				case PRECO_CAPA:
					hql.append(" precoCapa ");
					break;
				case REPARTE:
					hql.append(" qntReparte ");
					break;
				case DIFERENCA:
					hql.append(" qntDiferenca ");
					break;
				case VALOR_FATURADO:
					hql.append("  valorFaturado ");
					break;
				default:
					hql.append(" codigoProduto ");
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append( filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		return hql.toString();
	}

	@Override
	public Date obterUltimaExpedicaoDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(Expedicao.class);
		criteria.setProjection(Projections.max("dataExpedicao"));
		criteria.add(Restrictions.eq("dataExpedicao", dataOperacao));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date obterDataUltimaExpedicao() {
		Criteria criteria = getSession().createCriteria(Expedicao.class);
		criteria.setProjection(Projections.max("dataExpedicao"));
		return (Date) criteria.uniqueResult();
	}
	
	        /**
     * Sub-Query para obter quantidade da fiferença no relatorio de Resumo
     * Expedição/Lançamento
     * 
     * @return StringBuilder
     */
	private StringBuilder getQntDiferencaResumoLancamento(){
		
		StringBuilder query = new StringBuilder();
		
		query.append(" ( 											 ")
		     .append("     select 												 ")	     
		     .append("  sum(case when (tm.operacao_Estoque = 'ENTRADA') then (mec.qtde) else (-mec.qtde) end) ")
		     
		     .append("     from diferenca d 										 ")
		     .append("     inner join lancamento_diferenca ld on ld.id = d.lancamento_diferenca_id ")
		     .append("     inner join LANCAMENTO_DIFERENCA_MOVIMENTO_ESTOQUE_COTA ldmec on ld.ID = ldmec.LANCAMENTO_DIFERENCA_ID ")
		     .append("     inner join movimento_estoque_cota mec on ldmec.MOVIMENTO_ESTOQUE_COTA_ID = mec.id ")
		     .append("     inner join lancamento l on mec.lancamento_id = l.id ")
		     .append("     inner join tipo_movimento tm on mec.tipo_movimento_id = tm.id ")
		     .append("     inner join rateio_diferenca rd on (rd.DIFERENCA_ID = d.id) and (rd.cota_id = mec.cota_id)")
		     .append("     where d.produto_edicao_id = produtoEdicaoId 			 ")
		     .append("     and d.STATUS_CONFIRMACAO <> 'CANCELADO' 			 	 ")
		     .append("     and d.TIPO_DIRECIONAMENTO IN ('COTA', 'NOTA') ")
		     .append("     and l.DATA_LCTO_DISTRIBUIDOR = innerQuery.dataLancamento ")
		     // .append("     and tm.id not in (199, 200, 201, 202, 203) ")
		     .append("     group by d.PRODUTO_EDICAO_ID ")
		     .append(" ) ");
		
		return query;
	}

	/**
	 * Inner Query de Resumo de Lancamento
	 * @param isDetalhesResumo
	 * @return StringBuilder
	 */
	private StringBuilder getInnerQueryResumoLancamento(boolean isDetalhesResumo){
		
		StringBuilder innerQuery = new StringBuilder();
		
		innerQuery.append(" SELECT produto.CODIGO AS codigoProduto,  ")
		          .append(" produto.NOME AS nomeProduto,  ")
		          .append(" produtoEdicao.NUMERO_EDICAO AS numeroEdicao, ")
		          .append(" produtoEdicao.PRECO_VENDA AS precoCapa, ")
		          .append(" mec.QTDE AS qntReparte, ")
		          .append(" mec.QTDE * produtoEdicao.PRECO_VENDA AS valorFaturado, ")
		          .append(" tp.GRUPO_MOVIMENTO_ESTOQUE as grupoMovimento, ") 
		          .append(" produtoEdicao.ID AS produtoEdicaoId, ")
		          .append(" box.CODIGO  AS codigoBox, ")
		          .append(" expedicao.data_expedicao AS dataExpedicao, ");
		
		if (isDetalhesResumo) {

			innerQuery.append(" lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ")
					  .append(" coalesce((select valor from desconto where id = produtoEdicao.desconto_id),")
					  .append(" (select valor from desconto where id = produto.desconto_id), 0) as desconto, ")
					  .append(" pessoa.RAZAO_SOCIAL as razaoSocial ");
			
		} else {

			innerQuery.append(" lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ")
					  .append(" COALESCE(box.ID, 0) AS idBox,  ")
					  .append(" COALESCE(box.NOME, '') AS nomeBox  ");
		}
		
		innerQuery.append(" FROM EXPEDICAO expedicao ")
				  .append(" INNER JOIN LANCAMENTO lancamento ON expedicao.ID=lancamento.EXPEDICAO_ID ")
				  .append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON lancamento.PRODUTO_EDICAO_ID=produtoEdicao.ID ")
				  .append(" INNER JOIN PRODUTO produto ON produtoEdicao.PRODUTO_ID=produto.ID ");
		
		if (isDetalhesResumo) {
			
			innerQuery.append(" inner join PRODUTO_FORNECEDOR produtoFornecedor ")
					  .append(" on produto.ID=produtoFornecedor.PRODUTO_ID ")
					  .append(" inner join ")
					  .append(" FORNECEDOR fornecedor ")
					  .append(" on produtoFornecedor.fornecedores_ID=fornecedor.ID ")
					  .append(" inner join ")
					  .append(" PESSOA pessoa ")
					  .append(" on fornecedor.JURIDICA_ID=pessoa.ID ");
		}

		innerQuery.append(" INNER JOIN MOVIMENTO_ESTOQUE_COTA mec ON mec.LANCAMENTO_ID = lancamento.ID ")
				  .append(" INNER JOIN tipo_movimento tp ON tp.ID = mec.TIPO_MOVIMENTO_ID ")
				  .append(" INNER JOIN COTA cota  ON mec.COTA_ID=cota.ID ")
				  .append(" LEFT OUTER JOIN BOX box ON cota.BOX_ID=box.ID ")
				  .append(" WHERE lancamento.DATA_LCTO_DISTRIBUIDOR = :dataLancamento ")
				  .append(" AND lancamento.STATUS IN ( :statusAposExpedido ) ")
				  .append(" AND tp.GRUPO_MOVIMENTO_ESTOQUE IN( :grupoMovimentoEstoque )")
				  .append(" AND mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
				  
		
		return innerQuery;
	}
	
	        /**
     * Obtem agrupamento do resumo de Expedição/Lancamento
     * 
     * @param isAgrupamentoPorBox
     * @param isDetalhesResumo
     * @param filtro
     * @return StringBuilder
     */
	private StringBuilder getAgrupamentoResumoLancamento(boolean isAgrupamentoPorBox, boolean isDetalhesResumo, FiltroResumoExpedicaoDTO filtro){
		
		StringBuilder sql = new StringBuilder();
		
        if (isAgrupamentoPorBox) {
			
			sql.append(" GROUP BY innerQuery.idBox ");
			
		} else if (isDetalhesResumo) {

			sql.append(filtro != null && filtro.getCodigoBox() != null ? 
				" WHERE innerQuery.codigoBox = :codigoBox " : " WHERE innerQuery.codigoBox is null OR innerQuery.codigoBox = '-' "
			);

			sql.append(" GROUP BY innerQuery.produtoEdicaoId ");
		
		} else {
			
			sql.append(" GROUP BY innerQuery.produtoEdicaoId ");
		}
        
        return sql;
	}

	private String getHqlResumoLancamento(boolean isAgrupamentoPorBox, boolean isDetalhesResumo, FiltroResumoExpedicaoDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  innerQuery.precoCapa AS precoCapa, 				 	")
				.append(" SUM(CASE WHEN innerQuery.grupoMovimento in (" + this.getGruposFalta() + ") ")
				.append("			 THEN -innerQuery.qntReparte ")
				.append("			 ELSE innerQuery.qntReparte END ) + ").append(this.getQntCotaAusente(false))
				.append("  AS qntReparte, 						    ")
				.append(" COALESCE( "+ this.getQntDiferencaResumoLancamento() +", 0) AS qntDiferenca,")
				.append(" sum(innerQuery.valorFaturado) + ").append(this.getQntCotaAusente(true)).append(" AS valorFaturado,")
				.append(" innerQuery.codigoProduto AS codigoProduto, 			 ")
				.append(" innerQuery.nomeProduto AS nomeProduto, 				 ")
				.append(" innerQuery.numeroEdicao AS numeroEdicao, 				 ")
				.append(" innerQuery.produtoEdicaoId AS produtoEdicaoId, 		 ")
				.append(" innerQuery.codigoBox as codigoBox, 					 ");
		
		if (isDetalhesResumo) {
			
			sql.append(" innerQuery.desconto AS desconto, 		")
			   .append(" innerQuery.razaoSocial AS razaoSocial ");
			
		} else {
			
			sql.append(" COUNT(DISTINCT innerQuery.produtoEdicaoId) AS qntProduto, ")
			   .append(" innerQuery.dataLancamento as dataLancamento, ")
			   .append(" innerQuery.idBox as idBox, ")
			   .append(" innerQuery.nomeBox as nomeBox ");
		}

		sql.append(" FROM ( ")
		   .append(this.getInnerQueryResumoLancamento(isDetalhesResumo))
		   .append(" ) innerQuery ");
				
		sql.append(this.getAgrupamentoResumoLancamento(isAgrupamentoPorBox, isDetalhesResumo, filtro));
		
		return sql.toString();
	}
	
	private String ordenarConsultaPorBox(final FiltroResumoExpedicaoDTO filtro){
		
		final StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColunaBox() != null ){
			
			hql.append(" ORDER BY ");
			
			switch (filtro.getOrdenacaoColunaBox()) {
			case CODIGO_BOX:
				hql.append(" codigoBox ");
				break;
			case DATA_LANCAMENTO:
				hql.append(" dataLancamento ");
				break;
			case DESCRICAO_BOX:
				hql.append(" nomeBox ");
				break;
			case DIFERENCA:
				hql.append(" qntDiferenca ");
				break;
			case QNT_PRODUTO:
				hql.append(" qntProduto ");
				break;
			case REPARTE:
				hql.append(" qntReparte ");
				break;
			case VALOR_FATURADO:
				hql.append(" valorFaturado ");
				break;
			default:
				hql.append(" codigoBox ");
				break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append( filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		return hql.toString();
	}
	
	private String getGruposFalta() {

		List<String> list = 
				Arrays.asList(GrupoMovimentoEstoque.PERDA_DE.name(),
				      GrupoMovimentoEstoque.PERDA_EM.name(),
				      GrupoMovimentoEstoque.FALTA_DE.name(),
				      GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
				      GrupoMovimentoEstoque.FALTA_EM.name(),
				      GrupoMovimentoEstoque.FALTA_EM_DIRECIONADA_PARA_COTA.name(),
				      GrupoMovimentoEstoque.FALTA_EM_COTA.name());
		
		String gruposFalta = "";
		
		for (String grupo : list) {
			gruposFalta += ", '" + grupo + "'";
		}

		return gruposFalta.replaceFirst(",", "");
	}
	
	private String getQntCotaAusente(boolean calcularValorFaturado){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("( SELECT ");
		
		if(calcularValorFaturado){
			sql.append(" COALESCE(sum(case when (tp_movimento.GRUPO_MOVIMENTO_ESTOQUE in ( ")
				.append(" 'SUPLEMENTAR_COTA_AUSENTE', 'ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO', 'ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO' ")
				.append(" , 'ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR', 'ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS')) then (mec_st.QTDE *-1) else (mec_st.QTDE) end) * precoCapa,0)");
		} else{
			sql.append(" COALESCE(sum(case when (tp_movimento.GRUPO_MOVIMENTO_ESTOQUE in ( ")
				.append(" 'SUPLEMENTAR_COTA_AUSENTE', 'ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO', 'ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO' ")
				.append(" , 'ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR', 'ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS')) then (mec_st.QTDE *-1) else (mec_st.QTDE) end),0)");
		}
		
		sql.append("  FROM ");
		sql.append("	movimento_estoque mec_st inner join tipo_movimento tp_movimento");
		sql.append("	ON tp_movimento.ID = mec_st.TIPO_MOVIMENTO_ID");
		sql.append("	WHERE");
		sql.append("	mec_st.DATA = innerQuery.dataLancamento");
		sql.append("	AND mec_st.PRODUTO_EDICAO_ID =  produtoEdicaoId");
		sql.append("	AND tp_movimento.GRUPO_MOVIMENTO_ESTOQUE IN('SUPLEMENTAR_COTA_AUSENTE', ")
			.append("	'REPARTE_COTA_AUSENTE' "); 
			// .append("	'ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO', ")
			// .append("	'ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO', ")
			// .append("	'ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR', ")
			// .append("	'ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS')");
		sql.append("))");
		
		return sql.toString();
	}
}