package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementacao referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.TipoProduto}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class TipoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoProduto,Long> implements TipoProdutoRepository {

	public TipoProdutoRepositoryImpl() {
		super(TipoProduto.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#busca(java.lang.String, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoProduto> busca(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM, String orderBy,
			Ordenacao ordenacao, int initialResult, int maxResults) {
		
		Criteria criteria = addRestrictions(nomeTipoProduto, codigo, codigoNCM, codigoNBM);
		
		if (orderBy != null) {
		    
    		orderBy = (orderBy.equals("ncm")?"n.codigo":orderBy);
    		
    		if (Ordenacao.ASC == ordenacao) {
    			criteria.addOrder(Order.asc(orderBy));
    		} else if (Ordenacao.DESC == ordenacao) {
    			criteria.addOrder(Order.desc(orderBy));
    		}
		}
		
		if (maxResults >= 0 && initialResult >= 0 ) {
			criteria.setMaxResults(maxResults);
			criteria.setFirstResult(initialResult);
		}
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#quantidade(java.lang.String)
	 */
	@Override
	public Long quantidade(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM) {
		
		Criteria criteria = addRestrictions(nomeTipoProduto, codigo, codigoNCM, codigoNBM);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.list().get(0);
	}
	
	/**
	 * Adiciona as restricoes a consulta.
	 * @param nomeTipoProduto
	 * @param codigo
	 * @param codigoNCM
	 * @return
	 */
	private Criteria addRestrictions(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM) {
		
		Criteria criteria = getSession().createCriteria(TipoProduto.class,"tipoProduto");
		criteria.createAlias("tipoProduto.ncm", "n");
		
		if (!StringUtil.isEmpty(nomeTipoProduto)){
			criteria.add(Restrictions.ilike("descricao", nomeTipoProduto, MatchMode.ANYWHERE));
		}
		
		if (codigo != null) {
			criteria.add(Restrictions.eq("codigo", codigo));
		}
		
		if (!StringUtil.isEmpty(codigoNCM)) {
			criteria.add(Restrictions.eq("n.codigo", Long.parseLong(codigoNCM)));
		}
		
		if (!StringUtil.isEmpty(codigoNBM)) {
			criteria.add(Restrictions.eq("codigoNBM", codigoNBM));
		}
		
		return criteria;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#hasProdutoVinculado(br.com.abril.nds.model.cadastro.TipoProduto)
	 */
	@Override
	public boolean hasProdutoVinculado(TipoProduto tipoProduto) {
		
		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.add(Restrictions.eq("tipoProduto", tipoProduto));
		criteria.setProjection(Projections.rowCount());
		
		Long quantidade = (Long) criteria.list().get(0);
		
		return quantidade > 0;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#getMaxCodigo()
	 */
	@Override
	public Long getMaxCodigo() {
		
		Criteria criteria = getSession().createCriteria(TipoProduto.class);
		
		criteria.setProjection(Projections.max("codigo"));
		
		return (Long) criteria.list().get(0);
	}

	
	/**
	 * Obtem tipo de produto por código
	 * @param codigo
	 * @return TipoProduto
	 */
	@Override
	public TipoProduto obterPorCodigo(Long codigo) {
        Criteria criteria = getSession().createCriteria(TipoProduto.class);
		
		criteria.add(Restrictions.eq("codigo", codigo));
		
		return (TipoProduto) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {
		
		SQLQuery query = getSession().createSQLQuery(this.obterHql(filtro,false));
		
		query.addScalar("codigo");
		query.addScalar("produto");
		query.addScalar("edicao", StandardBasicTypes.LONG);
		query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipoProduto");
		query.addScalar("recolhimento");
		query.addScalar("lancamento");		
		
		this.aplicarFiltroQuery(query, filtro);
		
		this.aplicarParametros(query);
		
		this.aplicarParametrosList(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioTiposProdutosDTO.class));
		
		Integer paginaAtual = filtro.getPaginacaoVO().getPaginaAtual();
		Integer qtdResultadosPorPagina = filtro.getPaginacaoVO().getQtdResultadosPorPagina();
		
		if(paginaAtual!= null && qtdResultadosPorPagina!= null ){
			query.setFirstResult((paginaAtual - 1) * qtdResultadosPorPagina);
		}
		
		if(qtdResultadosPorPagina!= null){
			query.setMaxResults(qtdResultadosPorPagina);
		}
		
		return query.list();
	}

	private void aplicarParametros(Query query) {
		
		query.setParameter("operacaoEstoqueEntrada", OperacaoEstoque.ENTRADA.name());
		query.setParameter("operacaoEstoqueSaida", OperacaoEstoque.SAIDA.name());
	}
	
	private void aplicarParametrosList(Query query) {
		
		List<String> lista = new ArrayList<>();
		
		lista.add(GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name());
		lista.add(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO.name());
		lista.add(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO.name());
		lista.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name());
		lista.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.name());
		lista.add(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name());
		lista.add(GrupoMovimentoEstoque.VENDA_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name());
		
		query.setParameterList("gruposMovimentoEstoque", lista);
	}
	
	public Long obterQunatidade(FiltroRelatorioTiposProdutos filtro){
		
 		SQLQuery query = getSession().createSQLQuery(obterHql(filtro,true));
		
		query.addScalar("total", StandardBasicTypes.LONG);
		
		this.aplicarFiltroQuery(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	private String obterHql(FiltroRelatorioTiposProdutos filtro, boolean isCount){
		
		boolean hasTipoProduto = filtro.getTipoProduto() != null && filtro.getTipoProduto().longValue() != -1L;
		boolean hasLancamentoDe = filtro.getDataLancamentoDe() != null;
		boolean hasLancamentoAte = filtro.getDataLancamentoAte() != null;
		boolean hasRecolhimentoDe = filtro.getDataRecolhimentoDe() != null;
		boolean hasRecolhimentoAte = filtro.getDataRecolhimentoAte() != null;
		
		boolean hasFilter = hasTipoProduto || hasLancamentoDe || hasLancamentoAte || hasRecolhimentoDe || hasRecolhimentoAte;
		
		StringBuilder hql = new StringBuilder();
		
		if(isCount){
			
			hql.append(" SELECT COUNT(produto1_.CODIGO) AS total ");
			
		}else{
			
			hql.append(" SELECT ");
			hql.append(" produto1_.CODIGO AS codigo, ");
			hql.append(" produto1_.NOME AS produto, ");
			hql.append(" produtoedi0_.NUMERO_EDICAO AS edicao, ");
			hql.append(" produtoedi0_.PRECO_VENDA AS precoCapa, ");
	        
			hql.append(" (( ");
			hql.append("   		SELECT ");
			hql.append("   				SUM( ");
			hql.append("   					COALESCE (CASE WHEN (tipomovime7_.OPERACAO_ESTOQUE=:operacaoEstoqueSaida AND fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoe6_.QTDE when (tipomovime7_.OPERACAO_ESTOQUE=:operacaoEstoqueEntrada and fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) then - movimentoe6_.QTDE ELSE 0 END, 0) ");
			hql.append("             ) ");
			hql.append("         FROM ");
			hql.append("             MOVIMENTO_ESTOQUE movimentoe6_, ");
			hql.append("             TIPO_MOVIMENTO tipomovime7_ ");
			hql.append("         WHERE ");
			hql.append("             movimentoe6_.TIPO_MOVIMENTO_ID=tipomovime7_.ID ");
			hql.append("             AND movimentoe6_.PRODUTO_EDICAO_ID = produtoedi0_.ID ");
			hql.append(" 				AND tipomovime7_.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimentoEstoque) ");
			hql.append("   ) * produtoedi0_.PRECO_VENDA) AS faturamento, ");
	            
			hql.append(" tipoprodut2_.DESCRICAO AS tipoProduto, ");
			hql.append(" lancamento3_.DATA_REC_DISTRIB AS recolhimento, ");
			hql.append(" lancamento3_.DATA_LCTO_DISTRIBUIDOR AS lancamento ");
		}
		
		hql.append(" FROM ");
		hql.append("     PRODUTO_EDICAO produtoedi0_ ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto1_ ");
		hql.append("         ON produtoedi0_.PRODUTO_ID=produto1_.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     TIPO_PRODUTO tipoprodut2_ ");
		hql.append("         ON produto1_.TIPO_PRODUTO_ID=tipoprodut2_.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     LANCAMENTO lancamento3_ ");
		hql.append("         ON produtoedi0_.ID=lancamento3_.PRODUTO_EDICAO_ID ");
		hql.append(" LEFT JOIN ");
		hql.append(" 	 FECHAMENTO_ENCALHE fechamentoEncalhe ");
		hql.append(" 		 ON (fechamentoEncalhe.DATA_ENCALHE = lancamento3_.DATA_REC_DISTRIB ");
		hql.append(" 			 AND fechamentoEncalhe.PRODUTO_EDICAO_ID = produtoedi0_.ID) ");
		hql.append(" WHERE ");
		hql.append("     produtoedi0_.ATIVO = :verdadeiro ");

		if(hasFilter) {

			if(hasTipoProduto) {
				hql.append(" and tipoprodut2_.ID = :idTipoProduto");
			}
			if(hasLancamentoDe) {
				hql.append(" and lancamento3_.DATA_LCTO_DISTRIBUIDOR >= :dataLancamentoDe");
			}
			if(hasLancamentoAte) {
				hql.append(" and lancamento3_.DATA_LCTO_DISTRIBUIDOR <= :dataLancamentoAte");
			}
			if(hasRecolhimentoDe) {
				hql.append(" and lancamento3_.DATA_REC_DISTRIB >= :dataRecolhimentoDe");
			}
			if(hasRecolhimentoAte) {
				hql.append(" and lancamento3_.DATA_REC_DISTRIB <= :dataRecolhimentoAte");
			}
			
			if (hasRecolhimentoDe && hasRecolhimentoAte) {
				
				hql.append(" and lancamento3_.STATUS in (:statusLancamentoAposRecolhimento)");
			}
		}
		
		if(filtro.getPaginacaoVO()!= null && !isCount){
			
			hql.append(aplicarOrdenacao(filtro));
		}
		
		return hql.toString();

	}
	
	private String aplicarOrdenacao(FiltroRelatorioTiposProdutos filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColuna() != null ){
			
			hql.append(" order by ");
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO:
					hql.append(" codigo ");
					break;
				case DATA_LANCAMENTO:
					hql.append(" lancamento ");
					break;
				case DATA_RECOLHIMENTO:
					hql.append(" recolhimento ");
					break;
				case FATURAMENTO:
					hql.append(" faturamento ");
					break;
				case NOME_PRODUTO:
					hql.append(" produto ");
					break;
				case NUMERO_EDICAO:
					hql.append(" edicao ");
					break;
				case PRECO_CAPA:
					hql.append(" precoCapa ");
					break;
				case TIPO_PRODUTO:
					hql.append(" tipoProduto ");
					break;
				default:
					hql.append(" codigo ");
			}
			
			hql.append(filtro.getPaginacaoVO().getSortOrder());
			
			if (!filtro.getOrdenacaoColuna().equals(FiltroRelatorioTiposProdutos.OrdenacaoColuna.NUMERO_EDICAO)){
				
				hql.append(", edicao ");
			}
		}
		
		return hql.toString();
	}
	
	private void aplicarFiltroQuery(Query query, FiltroRelatorioTiposProdutos filtro){

		query.setParameter("verdadeiro", true);

		boolean hasTipoProduto = filtro.getTipoProduto() != null && filtro.getTipoProduto().longValue() != -1L;
		boolean hasLancamentoDe = filtro.getDataLancamentoDe() != null;
		boolean hasLancamentoAte = filtro.getDataLancamentoAte() != null;
		boolean hasRecolhimentoDe = filtro.getDataRecolhimentoDe() != null;
		boolean hasRecolhimentoAte = filtro.getDataRecolhimentoAte() != null;
		
		boolean hasFilter = hasTipoProduto || hasLancamentoDe || hasLancamentoAte || hasRecolhimentoDe || hasRecolhimentoAte;

		if(hasFilter) {
			
			if(hasTipoProduto) {
				query.setParameter("idTipoProduto", filtro.getTipoProduto());
			}
			if(hasLancamentoDe) {
				query.setParameter("dataLancamentoDe", filtro.getDataLancamentoDe());
			}
			if(hasLancamentoAte) {
				query.setParameter("dataLancamentoAte", filtro.getDataLancamentoAte());
			}
			if(hasRecolhimentoDe) {
				query.setParameter("dataRecolhimentoDe", filtro.getDataRecolhimentoDe());
			}
			if(hasRecolhimentoAte) {
				query.setParameter("dataRecolhimentoAte", filtro.getDataRecolhimentoAte());
			}
			
			if (hasRecolhimentoDe && hasRecolhimentoAte) {
				
				List<String> listaStatusAposRecolhimento = new ArrayList<>();
				
				listaStatusAposRecolhimento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO.name());
				listaStatusAposRecolhimento.add(StatusLancamento.RECOLHIDO.name());
				
				query.setParameterList(
					"statusLancamentoAposRecolhimento", listaStatusAposRecolhimento);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoProduto> obterTiposProdutoDesc() {
		
		StringBuilder hql = new StringBuilder("select id as id, descricao as descricao ");
		hql.append(" from TipoProduto ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(TipoProduto.class));
		
		return query.list();
	}
}