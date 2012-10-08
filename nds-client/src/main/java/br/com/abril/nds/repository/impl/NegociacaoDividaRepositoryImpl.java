package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.repository.NegociacaoDividaRepository;

@Repository
public class NegociacaoDividaRepositoryImpl extends AbstractRepositoryModel<Negociacao, Long> 
											implements NegociacaoDividaRepository{

	public NegociacaoDividaRepositoryImpl() {
		super(Negociacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NegociacaoDividaDTO> obterCotaPorNumero(FiltroConsultaNegociacaoDivida filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cobranca.dataEmissao as dtEmissao, ");
		hql.append(" cobranca.dataVencimento as dtVencimento, ");
		hql.append(" cobranca.valor as vlDivida, ");
		hql.append(" cobranca.encargos as encargos, ");
		hql.append(" CASE WHEN (datediff(cobranca.dataVencimento, current_date())) < 0 ");
		hql.append(" THEN 0 ELSE datediff(cobranca.dataVencimento, current_date()) END  as prazo, ");
		hql.append(" (cobranca.encargos + cobranca.valor) as total, ");
		hql.append(" cobranca.id as idCobranca ");
		
		this.getObterCotaPorNumeroFrom(hql, filtro);
		
		if (filtro.getPaginacaoVO() != null &&
				filtro.getPaginacaoVO().getSortColumn() != null){
			
			hql.append(" order by ").append(filtro.getPaginacaoVO().getSortColumn());
			
			if (filtro.getPaginacaoVO().getOrdenacao() != null){
				
				hql.append(" ").append(filtro.getPaginacaoVO().getOrdenacao());
			}
		}
	
		Query query = getSession().createQuery(hql.toString());
		query.setFirstResult((filtro.getPaginacaoVO().getPaginaAtual() - 1) * filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		this.setParametrosObterCotaPorNumero(query, filtro);

		query.setResultTransformer(new AliasToBeanResultTransformer(NegociacaoDividaDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterCotaPorNumeroCount(FiltroConsultaNegociacaoDivida filtro){
		
		StringBuilder hql = new StringBuilder(" select count (cobranca.id) ");
		this.getObterCotaPorNumeroFrom(hql, filtro);
		
		Query query = getSession().createQuery(hql.toString());
		
		this.setParametrosObterCotaPorNumero(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	private void getObterCotaPorNumeroFrom(StringBuilder hql, FiltroConsultaNegociacaoDivida filtro){
		
		hql.append(" FROM Cobranca cobranca ");
		hql.append(" JOIN cobranca.cota ");
		hql.append(" WHERE cobranca.cota.numeroCota = :numCota ");
		hql.append(" AND cobranca.statusCobranca = :status ");
		
		if(!filtro.isLancamento()){
			hql.append(" AND cobranca.dataVencimento <= current_date() ");
		}
	}
	
	private void setParametrosObterCotaPorNumero(Query query, FiltroConsultaNegociacaoDivida filtro){
		
		query.setParameter("numCota", filtro.getNumeroCota());
		query.setParameter("status", StatusCobranca.NAO_PAGO);
	}
}