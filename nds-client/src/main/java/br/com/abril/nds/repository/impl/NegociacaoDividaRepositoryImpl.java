package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.repository.NegociacaoDividaRepository;

@Repository
public class NegociacaoDividaRepositoryImpl extends AbstractRepository implements NegociacaoDividaRepository{

	@SuppressWarnings("unchecked")
	@Override
	public List<NegociacaoDividaDTO> obterCotaPorNumero(FiltroConsultaNegociacaoDivida filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cobranca.dataEmissao as dtEmissao, ");
		hql.append(" cobranca.dataVencimento as dtVencimento, ");
		hql.append(" divida.valor as vlDivida, ");
		hql.append(" cobranca.encargos as encargos, ");
		hql.append(" CASE WHEN (datediff(cobranca.dataVencimento, current_date())) < 0 ");
		hql.append(" THEN 0 ELSE datediff(cobranca.dataVencimento, current_date()) END  as prazo, ");
		hql.append(" (cobranca.encargos + divida.valor) as total, ");
		hql.append(" cobranca.id as idCobranca, ");
		hql.append(" divida.id  as idDivida ");
		hql.append(" FROM Cobranca cobranca ");
		hql.append(" JOIN cobranca.divida divida ");
		hql.append(" JOIN divida.cota ");
		hql.append(" WHERE cobranca.cota.numeroCota = :numCota ");
		hql.append(" AND cobranca.statusCobranca = :status ");
		if(!filtro.isLancamento()){
			hql.append(" AND cobranca.dataVencimento <= current_date() ");
		}
	
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numCota", filtro.getNumeroCota());
		query.setParameter("status", StatusCobranca.NAO_PAGO);

		query.setResultTransformer(new AliasToBeanResultTransformer(NegociacaoDividaDTO.class));
		
		return query.list();
	}

}
