package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;

@Repository
public class ResumoEncalheFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoEncalheFecharDiaRepository {

	@Override
	public BigDecimal obterValorEncalheFisico(Date dataOperacaoDistribuidor, boolean juramentada) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (ce.qtde * pe.precoVenda) ");			
		hql.append(" from ConferenciaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");		
		hql.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		
		if(juramentada){
			hql.append(" AND ce.juramentada = :juramentada ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		if(juramentada){
			query.setParameter("juramentada", juramentada);
		}
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorEncalheLogico(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (cec.qtdePrevista * pe.precoVenda) ");			
		hql.append(" from ChamadaEncalheCota AS cec ");		
		hql.append(" JOIN cec.chamadaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");		
		hql.append(" WHERE ce.dataRecolhimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ce.tipoChamadaEncalhe = :tipoChamadaEncalhe ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);		
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

}
