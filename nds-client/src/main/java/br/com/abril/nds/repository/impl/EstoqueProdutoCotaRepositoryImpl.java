package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;

@Repository
public class EstoqueProdutoCotaRepositoryImpl extends AbstractRepositoryModel<EstoqueProdutoCota, Long> implements EstoqueProdutoCotaRepository{

	public EstoqueProdutoCotaRepositoryImpl() {
		super(EstoqueProdutoCota.class);
	}

	@Override
	public EstoqueProdutoCota buscarEstoquePorProdutoECota(Long idProdutoEdicao, Long idCota) {

		if(	idProdutoEdicao == null ) {
			throw new NullPointerException();
		}
		
		Criteria criteria = super.getSession().createCriteria(EstoqueProdutoCota.class);
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		criteria.add(Restrictions.eq("cota.id", idCota));
				
		criteria.setMaxResults(1);
		
		return (EstoqueProdutoCota) criteria.uniqueResult();
	}
	
	public EstoqueProdutoCota buscarEstoquePorProdutEdicaoECota(Long idProdutoEdicao, Long idCota) {
		StringBuilder hql = new StringBuilder("select estoqueProdutoCota ");
		hql.append(" from EstoqueProdutoCota estoqueProdutoCota, ProdutoEdicao produtoEdicao, Cota cota ")
		   .append(" where estoqueProdutoCota.produtoEdicao.id = produtoEdicao.id ")
		   .append(" and estoqueProdutoCota.cota.id            = cota.id ")
		   .append(" and produtoEdicao.id                      = :idProdutoEdicao ")
		   .append(" and cota.id                               = :idCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (EstoqueProdutoCota) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstoqueProdutoCota> buscarListaEstoqueProdutoCota(Set<Long> idsLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select estoqueProdutoCota ")
		   .append(" from EstudoCota estudoCota ")
		   .append(" join estudoCota.estudo estudo ")
		   .append(" join estudo.lancamentos lancamento, EstoqueProdutoCota estoqueProdutoCota ")
		   
		   .append(" where estoqueProdutoCota.produtoEdicao = estudo.produtoEdicao ")
		   .append(" and estoqueProdutoCota.cota = estudoCota.cota")
		   .append(" and lancamento.id in (:idsLancamento)");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("idsLancamento", idsLancamento);
		
		return query.list();
	}
	
	public BigDecimal buscarQuantidadeEstoqueProdutoEdicao(Long numeroEdicao, String codigoProduto ,Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) ")
			.append(" from EstoqueProdutoCota estoqueProdutoCota " )
			.append(" join estoqueProdutoCota.cota cota ")
			.append(" join estoqueProdutoCota.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" where produtoEdicao.numeroEdicao = :numeroEdicao ")
			.append(" and produto.codigo =:codigoProduto ")
			.append(" and cota.numeroCota =:numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroCota", numeroCota);
		query.setMaxResults(1);
		
		return (BigDecimal) query.uniqueResult();
	}

	
	public BigDecimal obterValorTotalReparteCota(
			Integer numeroCota, 
			List<Long> listaIdProdutoEdicao, 
			Long idDistribuidor) {
		
		String subQueryConsultaValorComissionamento = getSubQueryConsultaValorComissionamento();
		
		StringBuilder hql = new StringBuilder();
		
			hql.append(" select ")
			
			.append(" sum( estoqueProdutoCota.qtdeRecebida * ")
			
			.append(" ( produtoEdicao.precoVenda - ( produtoEdicao.precoVenda  *  ( ")
			
			.append( subQueryConsultaValorComissionamento )
			
			.append(" / 100 ) ) ) ) ")
			
			.append(" from EstoqueProdutoCota estoqueProdutoCota ")
			
			.append(" join estoqueProdutoCota.cota cota ")
			
			.append(" join estoqueProdutoCota.produtoEdicao produtoEdicao ")
			
			.append(" where ")
			
			.append(" produtoEdicao.id in ( :listaIdProdutoEdicao ) ")

			.append(" and cota.numeroCota = :numeroCota ");
			
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("listaIdProdutoEdicao", listaIdProdutoEdicao);
		
		query.setParameter("numeroCota", numeroCota);

		query.setParameter("idDistribuidor", idDistribuidor);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	
	/**
	 * Retorna String referente a uma subquery que obtém o valor comissionamento 
	 * (percentual de desconto) para determinado produtoEdicao a partir de idCota e idDistribuidor. 
	 * 
	 * @return String
	 */
	private static String getSubQueryConsultaValorComissionamento() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select case when ( pe.desconto is not null ) then pe.desconto else ");
		
		hql.append(" ( case when ( ct.fatorDesconto is not null ) then ct.fatorDesconto  else  ");
		
		hql.append(" ( case when ( distribuidor.fatorDesconto is not null ) then distribuidor.fatorDesconto else 0 end ) end  ");
		
		hql.append(" ) end ");
		
		hql.append(" from ProdutoEdicao pe, Cota ct, Distribuidor distribuidor ");
		
		hql.append(" where ");
		
		hql.append(" ct.id = cota.id and ");

		hql.append(" pe.id = produtoEdicao.id and ");

		hql.append(" distribuidor.id = :idDistribuidor ) ");
		
		return hql.toString();
		
	}
	
	@Override
	public BigDecimal obterConsignado(boolean cotaInadimplente){
		
		StringBuilder hql = new StringBuilder("select  ");
		hql.append(" (sum(es.qtdeRecebida) * sum(es.produtoEdicao.precoVenda)) - (sum(es.qtdeDevolvida) * sum(es.produtoEdicao.precoVenda)) ")
		   .append(" from EstoqueProdutoCota es ");
		
		if (cotaInadimplente){
			
			hql.append(" where es.cota.id not in ( ")
			   .append(" select distinct hist.divida.cota.id ")
			   .append(" from HistoricoAcumuloDivida hist ")
			   .append(" where hist.status != :quitada) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (cotaInadimplente){
			
			query.setParameter("quitada", StatusInadimplencia.QUITADA);
		}
		
		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public Double obterFaturamentoCota(Long idCota) {
		
		if (idCota == null)
			throw new InvalidParameterException();
				
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT sum((epc.qtdeRecebida - epc.qtdeDevolvida) * (produtoEdicao.precoVenda - produtoEdicao.desconto))")
		
		.append(" FROM EstoqueProdutoCota AS epc ")
		.append(" JOIN epc.cota as cota ")
		.append(" JOIN epc.produtoEdicao as produtoEdicao ");
		
		
		hql.append(" WHERE cota.id = :idCota ");
		

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota",idCota);
		
		return ((BigDecimal) query.uniqueResult()).doubleValue();
	}
}
