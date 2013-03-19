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
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
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
		
		hql.append(getHqlResumoLancamentoPorBox());
		
		hql.append(" group by ");
		hql.append(" produtoEdicao.ID ");
		
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
		
		hql.append(gerarQueryResumoExpedicaoProdutosDoBox());
					
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
		
		Query query = getSession().createSQLQuery(gerarQueryResumoExpedicaoProdutosDoBox());
		
		this.setParametersQueryResumoExpedicaoProdutosDoBox(filtro, query);
		
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
	}
	
	public ExpedicaoDTO obterTotaisResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		String hqlResumoExpedicaoProdutoDoBox = gerarQueryResumoExpedicaoProdutosDoBox();
		
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
		query.setParameterList("tiposBox",  Arrays.asList(TipoBox.LANCAMENTO.name(),TipoBox.POSTO_AVANCADO.name()));
	}
	
	private void setParametersQueryResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro, Query query) {
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameterList("tiposBox",  Arrays.asList(TipoBox.LANCAMENTO.name(),TipoBox.POSTO_AVANCADO.name()));
		query.setParameter("codigoBox", filtro.getCodigoBox());
	}
	
	/**
	 * Retorna o sql referente a consulta de Reusumo de produtos expedidos
	 * @return String
	 */
	private String gerarQueryResumoExpedicaoProdutosDoBox(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" produto.CODIGO as codigoProduto, ");
		hql.append(" produto.NOME as nomeProduto, ");
		hql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		hql.append(" produtoEdicao.PRECO_VENDA as precoCapa, ");
		hql.append(this.getHQLDesconto() + " as desconto, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA) as qntReparte, ");
		hql.append(" sum(case ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_DE' then -(diferenca.QTDE*produtoEdicao.PACOTE_PADRAO) ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_DE' then diferenca.QTDE*produtoEdicao.PACOTE_PADRAO ");
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
		hql.append(" inner join ");
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
		hql.append(" and box.TIPO_BOX in (:tiposBox) ");
		hql.append(" and box.CODIGO = :codigoBox ");
		
		hql.append(" group by ");
		hql.append(" produtoEdicao.ID ");

		return hql.toString();
	}
	
	private String getHQLDesconto(){
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.DESCONTO");
		hql.append(" from VIEW_DESCONTO view ")
		   .append(" where view.COTA_ID = cota.ID ")
		   .append(" and view.PRODUTO_EDICAO_ID = produtoEdicao.ID ")
		   .append(" and view.FORNECEDOR_ID = fornecedor.ID), 0) ");
		
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
					hql.append(" produto.CODIGO ");
					break;
				case DESCRICAO_PRODUTO:
					hql.append(" produto.NOME ");
					break;
				case NUMERO_EDICAO:
					hql.append(" produtoEdicao.NUMERO_EDICAO ");
					break;
				case PRECO_CAPA:
					hql.append(" produtoEdicao.PRECO_VENDA ");
					break;
				case REPARTE:
					hql.append(" estudo.QTDE_REPARTE ");
					break;
				case DIFERENCA:
					hql.append(" ")
					.append(" sum( ( case ")
						.append(" when (diferenca.TIPO_DIFERENCA = 'FALTA_DE') then (-(diferenca.QTDE * produtoEdicao.PACOTE_PADRAO))")
						.append(" when (diferenca.TIPO_DIFERENCA = 'SOBRA_DE') then (diferenca.QTDE *  produtoEdicao.PACOTE_PADRAO)")
						.append(" when (diferenca.TIPO_DIFERENCA = 'FALTA_EM') then (-diferenca.QTDE)")
						.append(" when (diferenca.TIPO_DIFERENCA = 'SOBRA_EM') then (diferenca.QTDE)")
						.append(" else 0")
					.append(" end ) )");
					break;
				case VALOR_FATURADO:
					hql.append("  produtoEdicao.PRECO_VENDA*estudo.QTDE_REPARTE ");
					break;
				default:
					hql.append(" produto.CODIGO ");
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
		hql.append(" box.ID as idBox, ");
		hql.append(" concat(box.CODIGO, ");
		hql.append(" '-', ");
		hql.append(" box.NOME) as codigoBox, ");
		hql.append(" box.NOME as nomeBox, ");
		hql.append(" produtoEdicao.PRECO_VENDA as precoCapa, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA) as qntReparte, ");
		hql.append(" sum(case ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_DE' then -(diferenca.QTDE*produtoEdicao.PACOTE_PADRAO) ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_DE' then diferenca.QTDE*produtoEdicao.PACOTE_PADRAO ");
		hql.append(" when diferenca.TIPO_DIFERENCA='FALTA_EM' then -diferenca.QTDE ");
		hql.append(" when diferenca.TIPO_DIFERENCA='SOBRA_EM' then diferenca.QTDE ");
		hql.append(" else 0 ");
		hql.append(" end) as qntDiferenca, ");
		hql.append(" sum(estudoCota.QTDE_EFETIVA)*produtoEdicao.PRECO_VENDA as valorFaturado, ");
		hql.append(" produto.CODIGO as codigoProduto, ");
		hql.append(" produto.NOME as nomeProduto, ");
		hql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		hql.append(" count(produtoEdicao.ID) as qntProduto ");
		
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
		hql.append(" inner join ");
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
		hql.append(" and box.TIPO_BOX in (:tiposBox) ");
		
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

}
