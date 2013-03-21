package br.com.abril.nds.repository.impl;

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
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ExpedicaoRepository;

/**
 * Classe responsável por implementar as funcionalidades referente a expedição de lançamentos de produtos.
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
		
		hql.append(this.getHqlResumoLancamentoPorProduto());

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
		hql.append(getHqlResumoLancamentoPorBox());
		
		hql.append(" group by ");
		hql.append(" produtoEdicao.ID ");
		
		Query query = getSession().createSQLQuery(hql.toString());
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);
		
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
	}
	
	public ExpedicaoDTO obterTotaisResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		String hqlResumoLancamentoPorBox = getHqlResumoLancamentoPorBox();
		
		hqlResumoLancamentoPorBox = hqlResumoLancamentoPorBox + " group by produtoEdicao.ID ";
		
		String from = hqlResumoLancamentoPorBox.substring(hqlResumoLancamentoPorBox.lastIndexOf("from"));
		
		hql.append(" select SUM(queryResumoLancamentoPorBox.qntReparte) as qntReparte, ");
		hql.append(" SUM(queryResumoLancamentoPorBox.valorFaturado) as valorFaturado ");
		
		hql.append(" from ( ");
		
		hql.append(" select SUM(estudoCota.QTDE_EFETIVA) as qntReparte,");
		hql.append(" (SUM(estudoCota.QTDE_EFETIVA) * produtoEdicao.PRECO_VENDA) as valorFaturado ");
		
		hql.append(from);
		
		hql.append(") as queryResumoLancamentoPorBox ");
		
		Query query = getSession().createSQLQuery(hql.toString())
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
		
		hql.append(getHqlResumoLancamentoPorBox());
		
		hql.append(" group by ");
		hql.append(" box.ID ");
		
		Query query = this.getQueryResumoLancamentoPorBox(hql);
		
		this.setParametersQueryResumoExpedicaoPorProduto(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ExpedicaoDTO> obterResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(gerarQueryResumoExpedicaoProdutosDoBox(filtro));
					
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
	
	@SuppressWarnings("unchecked")
	public Long obterQuantidadeResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createSQLQuery(gerarQueryResumoExpedicaoProdutosDoBox(filtro));
		
		this.setParametersQueryResumoExpedicaoProdutosDoBox(filtro, query);
		
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
	}
	
	public ExpedicaoDTO obterTotaisResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		String hqlResumoExpedicaoProdutoDoBox = gerarQueryResumoExpedicaoProdutosDoBox(filtro);
		
		String from =
			hqlResumoExpedicaoProdutoDoBox.substring(hqlResumoExpedicaoProdutoDoBox.lastIndexOf("from"));
		
		hql.append(" select SUM(queryResumoExpedicaoProdutoDoBox.valorFaturado) as valorFaturado ");
		
		hql.append(" from ( ");
		
		hql.append(" select (SUM(estudoCota.QTDE_EFETIVA) * produtoEdicao.PRECO_VENDA) as valorFaturado ");
		
		hql.append(from);
		
		hql.append(") as queryResumoExpedicaoProdutoDoBox ");
		
		Query query = getSession().createSQLQuery(hql.toString()).addScalar("valorFaturado");
		
		this.setParametersQueryResumoExpedicaoProdutosDoBox(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExpedicaoDTO.class));
		
		return (ExpedicaoDTO) query.uniqueResult();
	}

	private Query getQueryResumoLancamentoPorBox(StringBuilder hql) {
		
		Query query = getSession().createSQLQuery(hql.toString())
			.addScalar("dataLancamento")
			.addScalar("idBox", StandardBasicTypes.LONG)
			.addScalar("codigoBox")
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
		//query.setParameterList("tiposBox",  Arrays.asList(TipoBox.LANCAMENTO.name(),TipoBox.POSTO_AVANCADO.name()));
	}
	
	private void setParametersQueryResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro, Query query) {
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		//query.setParameterList("tiposBox",  Arrays.asList(TipoBox.LANCAMENTO.name(),TipoBox.POSTO_AVANCADO.name()));
		if(filtro != null && filtro.getCodigoBox() != null)
			query.setParameter("codigoBox", filtro.getCodigoBox());
	}
	
	/**
	 * Retorna o sql referente a consulta de Reusumo de produtos expedidos
	 * @param filtro TODO
	 * @return String
	 */
	private String gerarQueryResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" produto.CODIGO as codigoProduto, ");
		hql.append(" produto.NOME as nomeProduto, ");
		hql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		hql.append(" produtoEdicao.PRECO_VENDA as precoCapa, ");
		hql.append(" coalesce((select valor from desconto where id = produtoEdicao.desconto_id),");
		hql.append(" (select valor from desconto where id = produto.desconto_id), 0) as desconto, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA) as qntReparte, ");
		hql.append(" sum(case ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_DE' then -diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_DE' then diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_EM' then -diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_EM' then diferenca.QTDE ");
		hql.append(" else 0 ");
		hql.append(" end) as qntDiferenca, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA)*produtoEdicao.PRECO_VENDA as valorFaturado, ");
		hql.append(" pessoa.RAZAO_SOCIAL as razaoSocial ");
		
		hql.append(" from ");
		hql.append(" EXPEDICAO expedicao ");
		hql.append(" inner join " );
		hql.append(" LANCAMENTO lancamento ");
		hql.append(" on expedicao.ID=lancamento.EXPEDICAO_ID ");
		hql.append(" inner join ");
		hql.append(" ESTUDO estudo ");
		hql.append(" on lancamento.PRODUTO_EDICAO_ID=estudo.PRODUTO_EDICAO_ID ");
		hql.append(" and lancamento.DATA_LCTO_PREVISTA=estudo.DATA_LANCAMENTO ");
		hql.append(" inner join ");
		hql.append(" EXPEDICAO espedicao on espedicao.ID = lancamento.EXPEDICAO_ID ");
		hql.append(" inner join ");
		hql.append(" PRODUTO_EDICAO produtoEdicao ");
		hql.append(" on estudo.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		hql.append(" inner join ");
		hql.append(" PRODUTO produto ");
		hql.append(" on produtoEdicao.PRODUTO_ID=produto.ID ");
		hql.append(" inner join ");
		hql.append(" PRODUTO_FORNECEDOR produtoFornecedor ");
		hql.append(" on produto.ID=produtoFornecedor.PRODUTO_ID ");
		hql.append(" inner join ");
		hql.append(" FORNECEDOR fornecedor ");
		hql.append(" on produtoFornecedor.fornecedores_ID=fornecedor.ID ");
		hql.append(" inner join ");
		hql.append(" PESSOA pessoa ");
		hql.append(" on fornecedor.JURIDICA_ID=pessoa.ID ");
		hql.append(" inner join ");
		hql.append(" ESTUDO_COTA estudoCota ");
		hql.append(" on estudo.ID=estudoCota.ESTUDO_ID ");
		hql.append(" inner join ");
		hql.append(" COTA cota ");
		hql.append(" on estudoCota.COTA_ID=cota.ID ");
		hql.append(" left outer join ");
		hql.append(" BOX box ");
		hql.append(" on cota.BOX_ID=box.ID ");
		hql.append(" left outer join ");
		hql.append(" RATEIO_DIFERENCA rateioDiferenca ");
		hql.append(" on estudoCota.ID=rateioDiferenca.ESTUDO_COTA_ID ");
		hql.append(" left outer join ");
		hql.append(" DIFERENCA diferenca ");
		hql.append(" on rateioDiferenca.DIFERENCA_ID=diferenca.id ");
		
		hql.append(" where ");
		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR = :dataLancamento ");
		//hql.append(" and box.TIPO_BOX in (:tiposBox) ");
		if(filtro != null && filtro.getCodigoBox() != null)
			hql.append(" and box.CODIGO = :codigoBox ");
		else
			hql.append(" and box.CODIGO is null ");
		hql.append(" group by ");
		hql.append(" produtoEdicao.ID ");

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
	
	/**
	 * Retorna o Hql da consulta de lançamentos agrupadas por BOX
	 * @return String
	 */
	private String getHqlResumoLancamentoPorBox(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		hql.append(" coalesce(box.ID, 0) as idBox, ");
		hql.append(" concat(coalesce(box.CODIGO, ''), ");
		hql.append(" '-', ");
		hql.append(" coalesce(box.NOME, '')) as codigoBox, ");
		hql.append(" coalesce(box.NOME, '') as nomeBox, ");
		hql.append(" produtoEdicao.PRECO_VENDA as precoCapa, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA) as qntReparte, ");
		hql.append(" sum(case ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_DE' then -diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_DE' then diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_EM' then -diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_EM' then diferenca.QTDE ");
		hql.append(" else 0 ");
		hql.append(" end) as qntDiferenca, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA)*produtoEdicao.PRECO_VENDA as valorFaturado, ");
		hql.append(" produto.CODIGO as codigoProduto, ");
		hql.append(" produto.NOME as nomeProduto, ");
		hql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		hql.append(" count(distinct produtoEdicao.ID) as qntProduto ");
		
		hql.append(" from ");
		hql.append(" EXPEDICAO expedicao ");
		hql.append(" inner join ");
		hql.append(" LANCAMENTO lancamento ");
		hql.append(" on expedicao.ID=lancamento.EXPEDICAO_ID ");
		hql.append(" inner join ");
		hql.append(" ESTUDO estudo ");
		hql.append(" on lancamento.PRODUTO_EDICAO_ID=estudo.PRODUTO_EDICAO_ID ");
		hql.append(" and lancamento.DATA_LCTO_PREVISTA=estudo.DATA_LANCAMENTO ");
		hql.append(" inner join ");
		hql.append(" EXPEDICAO espedicao on espedicao.ID = lancamento.EXPEDICAO_ID ");
		hql.append(" inner join ");
		hql.append(" PRODUTO_EDICAO produtoEdicao ");
		hql.append(" on estudo.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		hql.append(" inner join ");
		hql.append(" PRODUTO produto ");
		hql.append(" on produtoEdicao.PRODUTO_ID=produto.ID ");
		hql.append(" inner join ");
		hql.append(" ESTUDO_COTA estudoCota ");
		hql.append(" on estudo.ID=estudoCota.ESTUDO_ID ");
		hql.append(" inner join ");
		hql.append(" COTA cota ");
		hql.append(" on estudoCota.COTA_ID=cota.ID ");
		hql.append(" left outer join ");
		hql.append(" BOX box ");
		hql.append(" on cota.BOX_ID=box.ID ");
		hql.append(" left outer join ");
		hql.append(" RATEIO_DIFERENCA rateiosDiferenca ");
		hql.append(" on estudoCota.ID=rateiosDiferenca.ESTUDO_COTA_ID ");
		hql.append(" left outer join ");
		hql.append(" DIFERENCA diferenca ");
		hql.append(" on rateiosDiferenca.DIFERENCA_ID=diferenca.id ");
		
		hql.append(" where ");
		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR = :dataLancamento ");
		//hql.append(" and box.TIPO_BOX in (:tiposBox) ");
		
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
	
	private String getHqlResumoLancamentoPorProduto() {

		StringBuilder fromClause = new StringBuilder(this.getHqlResumoLancamentoPorBox());

		StringBuilder innerQuery = new StringBuilder();
		innerQuery.append(" select ");
		innerQuery.append(" diferenca.id as diferencaId, ");
		innerQuery.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		innerQuery.append(" coalesce(box.ID, 0) as idBox, ");
		innerQuery.append(" concat(coalesce(box.CODIGO, ''), ");
		innerQuery.append(" '-', ");
		innerQuery.append(" coalesce(box.NOME, '')) as codigoBox, ");
		innerQuery.append(" coalesce(box.NOME, '') as nomeBox, ");
		innerQuery.append(" produtoEdicao.PRECO_VENDA as precoCapa, ");
		innerQuery.append(" estudoCota.QTDE_EFETIVA as qntReparte, ");
		innerQuery.append(" case ");
		innerQuery.append(" when diferenca.TIPO_DIFERENCA='FALTA_DE' then -diferenca.QTDE ");
		innerQuery.append(" when diferenca.TIPO_DIFERENCA='SOBRA_DE' then diferenca.QTDE ");
		innerQuery.append(" when diferenca.TIPO_DIFERENCA='FALTA_EM' then -diferenca.QTDE ");
		innerQuery.append(" when diferenca.TIPO_DIFERENCA='SOBRA_EM' then diferenca.QTDE ");
		innerQuery.append(" else 0 ");
		innerQuery.append(" end as qntDiferenca, ");
		innerQuery.append(" estudoCota.QTDE_EFETIVA*produtoEdicao.PRECO_VENDA as valorFaturado, ");
		innerQuery.append(" produto.CODIGO as codigoProduto, ");
		innerQuery.append(" produto.NOME as nomeProduto, ");
		innerQuery.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		innerQuery.append(" produtoEdicao.ID as produtoEdicaoId ");
		innerQuery.append(fromClause.substring(fromClause.lastIndexOf("from")));

		StringBuilder sql = new StringBuilder();

		sql.append(" select diferencaGrouped.dataLancamento as dataLancamento, diferencaGrouped.idBox as idBox, ");
		sql.append(" diferencaGrouped.codigoBox as codigoBox, ");
		sql.append(" diferencaGrouped.nomeBox as nomeBox, diferencaGrouped.precoCapa as precoCapa, ");
		sql.append(" sum(diferencaGrouped.qntReparte) as qntReparte, sum(diferencaGrouped.qntDiferenca) as qntDiferenca, ");
		sql.append(" sum(diferencaGrouped.valorFaturado) as valorFaturado, ");
		sql.append(" diferencaGrouped.codigoProduto as codigoProduto, diferencaGrouped.nomeProduto as nomeProduto, ");
		sql.append(" diferencaGrouped.numeroEdicao as numeroEdicao, diferencaGrouped.qntProduto as qntProduto ");
		sql.append(" from ( ");
		sql.append(" select nonGrouped.dataLancamento as dataLancamento, nonGrouped.idBox as idBox, ");
		sql.append(" nonGrouped.codigoBox as codigoBox, ");
		sql.append(" nonGrouped.nomeBox as nomeBox, nonGrouped.precoCapa as precoCapa, ");
		sql.append(" sum(nonGrouped.qntReparte) as qntReparte, nonGrouped.qntDiferenca as qntDiferenca, ");
		sql.append(" sum(nonGrouped.valorFaturado) as valorFaturado, nonGrouped.codigoProduto as codigoProduto, ");
		sql.append(" nonGrouped.nomeProduto as nomeProduto, nonGrouped.numeroEdicao as numeroEdicao, ");
		sql.append(" count(distinct nonGrouped.produtoEdicaoId) as qntProduto, ");
		sql.append(" nonGrouped.produtoEdicaoId as produtoEdicaoId ");
		sql.append(" from ( ");
		sql.append(innerQuery);
		sql.append(" ) nonGrouped ");
		sql.append(" group by nonGrouped.diferencaId, nonGrouped.produtoEdicaoId ");
		sql.append(" ) diferencaGrouped ");
		sql.append(" group by diferencaGrouped.produtoEdicaoId ");

		return sql.toString();
	}
}
