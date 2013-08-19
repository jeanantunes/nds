package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TiposEstabelecimentoRepository;

@Repository
public class TiposEstabelecimentoRepositoryImpl extends AbstractRepositoryModel<TipoEstabelecimentoAssociacaoPDV,Long> implements TiposEstabelecimentoRepository{
	
	public TiposEstabelecimentoRepositoryImpl() {
		
		super(TipoEstabelecimentoAssociacaoPDV.class);
	}
	
	public TipoEstabelecimentoAssociacaoPDV obterTipoEstabelecimentoAssociacaoPDV(Long codigo){
		
		Criteria criteria = getSession().createCriteria(TipoEstabelecimentoAssociacaoPDV.class);
		criteria.add(Restrictions.eq("codigo", codigo));
		criteria.setMaxResults(1);
		
		return (TipoEstabelecimentoAssociacaoPDV) criteria.uniqueResult();
	}
}
