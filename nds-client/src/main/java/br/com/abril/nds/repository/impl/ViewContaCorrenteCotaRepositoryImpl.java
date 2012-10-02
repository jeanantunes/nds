package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.repository.ViewContaCorrenteCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ViewContaCorrenteCotaRepositoryImpl extends AbstractRepositoryModel<ViewContaCorrenteCota, Integer> implements ViewContaCorrenteCotaRepository {

	public ViewContaCorrenteCotaRepositoryImpl() {		
		
		super(ViewContaCorrenteCota.class);		
	}
	
	@Override
	public Long getQuantidadeViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro){
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select count(viewContaCorrente) ");	
			
		Query query = corpoQuery(filtro, hql,null);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewContaCorrenteCota> getListaViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro) {
		PaginacaoVO paginacao = filtro.getPaginacao();
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select viewContaCorrente ");	
			
		Query query = corpoQuery(filtro, hql, getOrdenacaoConsulta(filtro,paginacao));
		
		
		if (paginacao != null) {
			
			if (paginacao.getPosicaoInicial() != null) {
				
				query.setFirstResult(paginacao.getPosicaoInicial());
			}
			
			if (paginacao.getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}

	/**
	 * @param filtro
	 * @param hql
	 * @return
	 * @throws HibernateException
	 */
	private Query corpoQuery(FiltroViewContaCorrenteCotaDTO filtro,
			StringBuffer hql, StringBuffer ordenacao) throws HibernateException {
		hql.append(" from ViewContaCorrenteCota viewContaCorrente	");
		
		hql.append(" where ");
		
		hql.append(" viewContaCorrente.numeroCota = :numeroCota ");
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			hql.append(" and viewContaCorrente.dataConsolidado between :inicioPeriodo and :fimPeriodo ");
		}	
	
		
		if (ordenacao != null) {
			hql.append(ordenacao);
		}
		Query query  = getSession().createQuery(hql.toString());
			
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
			query.setParameter("fimPeriodo", filtro.getFimPeriodo());
		}
		
		
		return query;
	}

	private StringBuffer getOrdenacaoConsulta(FiltroViewContaCorrenteCotaDTO filtro,PaginacaoVO paginacao) {
		
		StringBuffer hql = new StringBuffer();
		
		String colunaOrdenacao = filtro.getColunaOrdenacao();
		
		if (colunaOrdenacao != null) {
			hql.append("order by viewContaCorrente.").append(colunaOrdenacao).append(" ");
			
			if (paginacao != null) {				
				hql.append(paginacao.getOrdenacao());
			}else{
				hql.append("asc");
			}
		}
		
		return hql; 
	}

}
