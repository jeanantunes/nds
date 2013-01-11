package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;

@Repository
public class TipoGeradorFluxoPDVRepositoryImpl extends AbstractRepositoryModel<TipoGeradorFluxoPDV,Long> implements TipoGeradorFluxoPDVRepsitory {
	
	public TipoGeradorFluxoPDVRepositoryImpl() {
		super(TipoGeradorFluxoPDV.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long... codigos) {
		
		Criteria criteria = super.getSession().createCriteria(TipoGeradorFluxoPDV.class);
		
		criteria.add(Restrictions.in("codigo", codigos));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoNotIn(Long... codigos ){
		
		Criteria criteria = super.getSession().createCriteria(TipoGeradorFluxoPDV.class);
		
		criteria.add(Restrictions.not(Restrictions.in("codigo", codigos)));
		
		return criteria.list();
	}
	
	// Inserindo dados falsos
	public static void main(String[] args) {
		
	}
}
