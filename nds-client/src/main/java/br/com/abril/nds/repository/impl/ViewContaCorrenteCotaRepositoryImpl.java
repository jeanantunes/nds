package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO.ColunaOrdenacao;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.repository.ViewContaCorrenteCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class ViewContaCorrenteCotaRepositoryImpl extends AbstractRepositoryModel<ViewContaCorrenteCota, Integer> implements ViewContaCorrenteCotaRepository {

	public ViewContaCorrenteCotaRepositoryImpl() {		
		
		super(ViewContaCorrenteCota.class);		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewContaCorrenteCota> getListaViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro) {
			
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select viewContaCorrente ");	
			
		hql.append(" from ViewContaCorrenteCota viewContaCorrente	");
		
		hql.append(" where ");
		
		hql.append(" viewContaCorrente.numeroCota = :numeroCota ");
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			hql.append(" and viewContaCorrente.dataConsolidado between :inicioPeriodo and :fimPeriodo ");
		}
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
		hql.append( getOrdenacaoConsulta(filtro,paginacao) );
		
		Query query  = getSession().createQuery(hql.toString());
			
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
			query.setParameter("fimPeriodo", filtro.getFimPeriodo());
		}
		
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

	private StringBuffer getOrdenacaoConsulta(FiltroViewContaCorrenteCotaDTO filtro,PaginacaoVO paginacao) {
		
		StringBuffer hql = new StringBuffer();
		
		ColunaOrdenacao colunaOrdenacao = filtro.getColunaOrdenacao();
		
		if (colunaOrdenacao != null) {
			
			switch (colunaOrdenacao) {
				
				case CONSIGNADO :
					hql.append("order by viewContaCorrente.consignado ");
					break;

				case DEBITO_CREDITO :
					hql.append("order by viewContaCorrente.debitoCredito ");			
					break;
				
				case DT_CONSOLIDADO :
					hql.append("order by viewContaCorrente.dataConsolidado ");
					break;
				
				case ENCALHE:
					hql.append("order by viewContaCorrente.encalhe ");
					break;
				
				case ENCARGOS:
					hql.append("order by viewContaCorrente.encargos ");
					break;
					
				case NUMERO_ATRASADOS :
					hql.append("order by viewContaCorrente.numeroAtrasados ");
					break;
				
				case PENDENTE:
					hql.append("order by viewContaCorrente.pendente ");
					break;
				
				case TOTAL :
					hql.append("order by viewContaCorrente.total ");
					break;
				
				case VALOR_POSTERGADO :
					hql.append("order by viewContaCorrente.valorPostergado ");
					break;
				
				case VENDA_ENCALHE :
					hql.append("order by viewContaCorrente.vendaEncalhe ");
					break;

			}
			
			String ordenacao = "asc";
			
			if (paginacao != null) {
				
				if (paginacao.getOrdenacao().equals(Ordenacao.DESC)) {
					ordenacao = "desc";
				}
			}
			
			hql.append(ordenacao);
		}
		
		return hql; 
	}

}
