package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.GeradorFluxoPDVRepository;

@Repository
public class GeradorFluxoPDVRepositoryImpl extends AbstractRepositoryModel<GeradorFluxoPDV, Long> implements GeradorFluxoPDVRepository {
	
	public GeradorFluxoPDVRepositoryImpl() {
		super(GeradorFluxoPDV.class);
	}
	
	public GeradorFluxoPDV obterGeradorFluxoPDV(Long idPDV){
		
		Criteria criteria = getSession().createCriteria(GeradorFluxoPDV.class);
		
		criteria.add(Restrictions.eq("pdv.id", idPDV));
		
		return (GeradorFluxoPDV) criteria.uniqueResult();
	}
	
	public void removerGeradorFluxoPDV(Long idPDV) {
		
		Query deleteTipo = getSession().createSQLQuery(
			" delete from GERADOR_FLUXO_PDV_TIPO_GERADOR_FLUXO_PDV " +
			" where GERADOR_FLUXO_PDV_ID = " +
			" (select id from GERADOR_FLUXO_PDV where PDV_ID= :idPDV) "
		);
		
		deleteTipo.setParameter("idPDV", idPDV).executeUpdate();
		
		Query deleteGrupo = getSession().createSQLQuery(" delete from GERADOR_FLUXO_PDV where PDV_ID = :idPDV ");

		deleteGrupo.setParameter("idPDV", idPDV).executeUpdate();
	}
}
