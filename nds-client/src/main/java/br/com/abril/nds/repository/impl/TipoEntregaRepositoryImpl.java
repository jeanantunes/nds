package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;

/**
 * Classe de implementação do Repository de TipoEntrega.
 * 
 * @author Discover Technology.
 */
@Repository
public class TipoEntregaRepositoryImpl extends AbstractRepositoryModel<TipoEntrega, Long> implements TipoEntregaRepository {

	public TipoEntregaRepositoryImpl() {
		super(TipoEntrega.class);
	}
	
	@Override
	public TipoEntrega buscarPorDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		
		Criteria criteria =  getSession().createCriteria(TipoEntrega.class);
		
		criteria.add(Restrictions.eq("descricaoTipoEntrega", descricaoTipoEntrega));
		
		return (TipoEntrega) criteria.uniqueResult();
	}
}
