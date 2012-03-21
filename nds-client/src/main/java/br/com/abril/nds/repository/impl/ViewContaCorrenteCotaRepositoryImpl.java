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
public class ViewContaCorrenteCotaRepositoryImpl extends AbstractRepository<ViewContaCorrenteCota, Integer> implements ViewContaCorrenteCotaRepository {

	public ViewContaCorrenteCotaRepositoryImpl(){		
		super(ViewContaCorrenteCota.class);		
	}
	

@SuppressWarnings("unchecked")
public List<ViewContaCorrenteCota> getListaViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro) {
		
	StringBuffer hql = new StringBuffer("");
	
	hql.append(" select viewContaCorrente ");	
		
	hql.append(" from ViewContaCorrenteCota viewContaCorrente	");
	
	hql.append(" where ");
	
	hql.append(" viewContaCorrente.numeroCota = :numeroCota ");
	
	PaginacaoVO paginacao = filtro.getPaginacao();
	
	ColunaOrdenacao colunaOrdenacao = filtro.getColunaOrdenacao();
	if (colunaOrdenacao != null) {
		if (ColunaOrdenacao.CONSIGNADO == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.consignado ");
		} else if (ColunaOrdenacao.DEBITO_CREDITO == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.debitoCredito ");
		} else if (ColunaOrdenacao.DT_CONSOLIDADO == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.dataConsolidado ");
		} else if (ColunaOrdenacao.ENCALHE == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.encalhe ");
		} else if (ColunaOrdenacao.ENCARGOS == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.encargos ");
		} else if (ColunaOrdenacao.NUMERO_ATRASADOS == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.numeroAtrasados ");
		} else if (ColunaOrdenacao.PENDENTE == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.pendente ");
		} else if (ColunaOrdenacao.TOTAL == colunaOrdenacao) {
				hql.append("order by viewContaCorrente.total ");
		} else if (ColunaOrdenacao.VALOR_POSTERGADO == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.valorPostergado ");
		} else if (ColunaOrdenacao.VENDA_ENCALHE == colunaOrdenacao) {
			hql.append("order by viewContaCorrente.vendaEncalhe ");
		}
		
		String ordenacao = "asc";
		if (paginacao != null) {
			if (paginacao.getOrdenacao().equals(Ordenacao.DESC)) {
				ordenacao = "desc";
			}
		}
		hql.append(ordenacao);
	}
	

	Query query  = getSession().createQuery(hql.toString());
		
	query.setParameter("numeroCota", filtro.getNumeroCota());
	
	if (paginacao != null) {
		query.setFirstResult(paginacao.getPosicaoInicial());
		query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	}
	return query.list();
		
}

}
