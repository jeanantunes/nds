package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		
		StringBuilder hql = new StringBuilder("select estoque ");
		hql.append(" from EstoqueProdutoCota estoque    ")
		   .append(" join  estoque.produtoEdicao produtoEdicao ")
		   .append(" join estoque.cota cota")
		   .append(" where produtoEdicao.id =:idProdutoEdicao ")
		   .append(" and cota.id            =:idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (EstoqueProdutoCota) query.uniqueResult();
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
	
	public BigInteger obterTotalEmEstoqueProdutoCota(Long idCota, Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum(estoque.qtdeRecebida - estoque.qtdeDevolvida) 	");
		hql.append(" from EstoqueProdutoCota estoque  							");
		hql.append(" where estoque.cota.id = :idCota and 						");
		hql.append(" estoque.produtoEdicao.id = :idProdutoEdicao  				");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (BigInteger) query.uniqueResult();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstoqueProdutoCota> buscarListaEstoqueProdutoCota(Long idLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select estoqueProdutoCota ")
		   .append(" from EstudoCota estudoCota ")
		   .append(" join estudoCota.estudo estudo ")
		   .append(" join estudo.lancamentos lancamento, EstoqueProdutoCota estoqueProdutoCota ")
		   
		   .append(" where estoqueProdutoCota.produtoEdicao = estudo.produtoEdicao ")
		   .append(" and estoqueProdutoCota.cota = estudoCota.cota")
		   .append(" and lancamento.id = :idLancamento");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idLancamento", idLancamento);
		
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

		hql.append(" SELECT sum((epc.qtdeRecebida - epc.qtdeDevolvida) * (mec.valoresAplicados.precoComDesconto)) ")
		.append(" FROM EstoqueProdutoCota AS epc ")
		.append(" JOIN epc.movimentos as mec ")
		.append(" JOIN epc.cota as cota ")
		.append(" JOIN epc.produtoEdicao as produtoEdicao ")
		.append(" JOIN produtoEdicao.produto.fornecedores as fornecedor ");
		
		
		hql.append(" WHERE cota.id = :idCota ");
		

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota",idCota);
		
		BigDecimal retorno = (BigDecimal) query.uniqueResult();
		
		if (retorno == null){
			
			return 0D;
		}
		
		return retorno.doubleValue();
	}
}