package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class EstoqueProdutoRepositoryImpl extends AbstractRepositoryModel<EstoqueProduto, Long> implements EstoqueProdutoRespository {

	 private static final Logger LOGGER = LoggerFactory.getLogger(EstoqueProdutoRepositoryImpl.class);
	public EstoqueProdutoRepositoryImpl() {
		super(EstoqueProduto.class);
		
	}

	@Override
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {

//		Criteria criteria = super.getSession().createCriteria(EstoqueProduto.class, "estoqueProduto");
//		
//		criteria.createAlias("estoqueProduto.produtoEdicao", "produtoEdicao");
//		
//		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
//		
//		criteria.setMaxResults(1);
//		
//		return (EstoqueProduto) criteria.uniqueResult();
		
		Query query = 
				this.getSession().createQuery(
					" select ep from EstoqueProduto ep where ep.produtoEdicao.id = :idProdutoEdicao ");
			
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
			
			query.setMaxResults(1);
			
			return (EstoqueProduto) query.uniqueResult();
	}

	@SuppressWarnings("rawtypes")
	public Long selectForUpdate(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT E.ID AS id ");
		
		hql.append(" FROM ESTOQUE_PRODUTO E ");
		
		hql.append(" WHERE E.PRODUTO_EDICAO_ID = :idProdutoEdicao FOR UPDATE ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		((org.hibernate.SQLQuery)query).addScalar("id", StandardBasicTypes.LONG);
		
		List ids = query.list();
	
		if(ids==null || ids.isEmpty()){
			return null;
		}
		
		return (Long) ids.get(0);
		
	}
	
	public EstoqueProduto obterEstoqueProdutoParaAtualizar(Long idProdutoEdicao) {
		
		Query query = 
			this.getSession().createQuery(
				" select ep from EstoqueProduto ep where ep.produtoEdicao.id = :idProdutoEdicao ");
		
		query.setLockOptions(LockOptions.UPGRADE.setTimeOut(180000)); // timeout de 180s para evitar dead lock
		

		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (EstoqueProduto) query.uniqueResult();
	}
	
	public void atualizarEstoqueProduto(Long idProdutoEdicao, TipoEstoque tipoEstoque, BigInteger qtde) {
		
		String sql = "update estoque_produto set ";
		
		switch (tipoEstoque) {
		
			case SUPLEMENTAR:
				
				sql += " QTDE_SUPLEMENTAR = QTDE_SUPLEMENTAR + ";
				break;
				
			case DEVOLUCAO_ENCALHE:
				
				sql += " QTDE_DEVOLUCAO_ENCALHE = QTDE_DEVOLUCAO_ENCALHE + ";
				break;
				
			default:
				break;
		}
		
		sql += " :qtde where PRODUTO_EDICAO_ID = :idProdutoEdicao ";
		
		this.getSession()
			.createSQLQuery(sql)
				.setParameter("qtde", qtde)
				.setParameter("idProdutoEdicao", idProdutoEdicao)
				.setTimeout(60000)  // timeout de 60s para evitar dead lock
				.executeUpdate();
	}

	public EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao) {
		StringBuilder hql = new StringBuilder("select estoqueProduto ");
		hql.append(" from EstoqueProduto estoqueProduto join estoqueProduto.produtoEdicao produtoEdicao ")
		   .append(" where produtoEdicao.id = :idProdutoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setMaxResults(1);
		
		return (EstoqueProduto) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<EstoqueProdutoDTO> buscarEstoquesProdutos() {
		StringBuilder hql = new StringBuilder("")
			.append("SELECT coalesce(ep.qtde, 0) AS qtde ")
			.append("	, coalesce(ep.qtde_danificado, 0) as qtdeDanificado ")
			.append("	, coalesce(ep.qtde_devolucao_encalhe, 0) as qtdeDevolucaoEncalhe ")
			.append("	, coalesce(ep.qtde_suplementar, 0) as qtdeSuplementar ")
			.append("	, coalesce(ep.qtde_juramentado, 0) as qtdeJuramentado ")
			.append("	, ep.produto_edicao_id as produtoEdicaoId ")
			.append("from ESTOQUE_PRODUTO ep ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.addScalar("qtde", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeDanificado", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeDevolucaoEncalhe", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeSuplementar", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeJuramentado", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("produtoEdicaoId", StandardBasicTypes.LONG);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueProdutoDTO.class));
		
		return (List<EstoqueProdutoDTO>) query.list();
	}

	@Override
	public Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro) {
		
		StringBuilder hql = new StringBuilder("select (estoqueProduto.id) ");
		this.getQueryBuscarEstoqueProdutoRecolhimento(hql);
		
		hql.append(" group by chamadaEncalhe.produtoEdicao.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.setParametrosBuscarEstoqueProdutoRecolhimento(query, filtro);
		
		return (long) query.list().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro) {
		
		StringBuilder hqlRecolhimentoPDV = new StringBuilder();
		hqlRecolhimentoPDV.append(" coalesce((select sum(conEnc.qtde) ")
		   .append(" from ConferenciaEncalhe conEnc ")
		   .append("	join conEnc.controleConferenciaEncalheCota conConfEncCota ")
		//   .append("	join conEnc.produtoEdicao pe ")
		   .append("	where conConfEncCota.dataOperacao = :dataRecolhimento ")
		   .append("	and conEnc.produtoEdicao.id = produtoEdicao.id),0) ");
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" chamadaEncalhe.sequencia as sequencia, ")
		   .append(" produto.codigo as codigo, ")
		   .append(" produto.nome as produto, ")
		   .append(" produtoEdicao.numeroEdicao as numeroEdicao, ")
		   .append(" coalesce(estoqueProduto.qtde, 0) as lancamento, ")
		   .append(" coalesce(estoqueProduto.qtdeSuplementar, 0) as suplementar, ")
		   .append(" coalesce(estoqueProduto.qtdeDanificado, 0) as danificado, ")
		   .append(" (coalesce(estoqueProduto.qtdeDevolucaoEncalhe, 0)  ")
		   //mater comentado até que se prove o contrário, assim disse César, MNDS-208
		   //.append(" - ").append(hqlRecolhimentoPDV)
		   .append(" ) as recolhimento, ")
		   .append(hqlRecolhimentoPDV)
		   .append(" as recolhimentoPDV,")
		   .append(" (coalesce(estoqueProduto.qtde, 0) + coalesce(estoqueProduto.qtdeSuplementar, 0) +  ")
		   .append(" coalesce(estoqueProduto.qtdeDanificado, 0) + ")
		   .append(hqlRecolhimentoPDV).append(" + ")
		   .append(" coalesce(estoqueProduto.qtdeDevolucaoEncalhe, 0) - ")
		   .append(hqlRecolhimentoPDV)
		   .append(" )as total ");
		
		this.getQueryBuscarEstoqueProdutoRecolhimento(hql);
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		hql.append(" group by chamadaEncalhe.produtoEdicao.id ");
		
		if (paginacaoVO != null){
			
			hql.append(" order by ")
			   .append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		this.setParametrosBuscarEstoqueProdutoRecolhimento(query, filtro);
		
		if (paginacaoVO != null && paginacaoVO.getPaginaAtual() != null){
			
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueProdutoRecolimentoDTO.class));
		
		return query.list();
	}
	
	private void getQueryBuscarEstoqueProdutoRecolhimento(StringBuilder hql){
		
		hql.append(" from EstoqueProduto estoqueProduto ")
		   .append(" join estoqueProduto.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produtoEdicao.chamadaEncalhes chamadaEncalhe ")
		 // alteracoes abaixo por questao de performance
		 //.append(" join chamadaEncalhe.chamadaEncalheCotas cec ")
		   .append(" where chamadaEncalhe.dataRecolhimento = :dataRecolhimento ")
		 //.append(" and cec.postergado = :naoPostergado ");
		   .append("  AND exists (     select cec FROM ChamadaEncalheCota cec where chamadaEncalhe.id = cec.chamadaEncalhe.id ")
           .append(" and  cec.postergado = :naoPostergado )");
		   
	}
	
	private void setParametrosBuscarEstoqueProdutoRecolhimento(Query query, 
			FiltroEstoqueProdutosRecolhimento filtro){
		
		query.setParameter("dataRecolhimento", filtro.getDataRecolhimento());
		query.setParameter("naoPostergado", false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> obterDatasRecProdutosFechados() {
		
		StringBuilder hql = new StringBuilder("select distinct ");
		hql.append(" lan.dataRecolhimentoPrevista ")
		   .append(" from EstoqueProduto ep ")
		   .append(" join ep.produtoEdicao pre ")
		   .append(" join pre.lancamentos lan ")
		   .append(" join pre.chamadaEncalhes ce")
		   .append(" where lan.status = :statusFechado ")
		   .append(" group by ep.id ")
		   .append(" having (sum(coalesce(ep.qtde,0)) + sum(coalesce(ep.qtdeSuplementar,0)) ")
		   .append(" + sum(coalesce(ep.qtdeDevolucaoEncalhe,0))) != 0 ")
		   .append(" order by lan.dataRecolhimentoPrevista ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("statusFechado", StatusLancamento.FECHADO);
		
		return query.list();
	}
	
	@Override
	public BigInteger buscarQtdEstoquePorProduto(String codigoProduto, List<Long> numeroEdicao) {
		
		StringBuilder hql = new StringBuilder("select sum(coalesce(e.qtde,0)) ");
		hql.append(" from EstoqueProduto e ")
		   .append(" join e.produtoEdicao pe ")
		   .append(" join pe.produto p ")
		   .append(" where p.codigo = :codigoProduto ")
		   .append(" and pe.numeroEdicao in (:numeroEdicao) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameterList("numeroEdicao", numeroEdicao);
		
		return (BigInteger) query.uniqueResult();
	}
	
	
	@Override
	public BigInteger buscarQtdEstoqueProdutoEdicao(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select e.qtde 				")
		   .append(" from EstoqueProduto e 		")
		   .append(" join e.produtoEdicao pe 	")
		   .append(" where pe.id = :idProdutoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (BigInteger) query.uniqueResult();
	}
	
	@Override
	public BigInteger buscarQtdeEstoqueParaTransferenciaParcial(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select coalesce(e.qtde, 0) + coalesce(e.qtdeSuplementar, 0) + coalesce(e.qtdeDevolucaoEncalhe, 0) + coalesce(e.qtdeDanificado, 0) ")
		   .append(" from EstoqueProduto e ")
		   .append(" join e.produtoEdicao pe ")
		   .append(" where pe.id = :idProdutoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (BigInteger) query.uniqueResult();
	}
	
	public BigDecimal obterValorTotalSuplementar(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum(estoque.QTDE_SUPLEMENTAR * produtoEdicao.preco_venda) ") ;
		hql.append("from estoque_produto estoque ") ;
		hql.append("join produto_edicao produtoEdicao on produtoEdicao.ID = estoque.PRODUTO_EDICAO_ID ") ;
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		return (BigDecimal) query.uniqueResult();	
	}
	
}