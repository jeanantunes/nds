package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;

@Repository
public class FechamentoEncalheBoxRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalheBox, FechamentoEncalheBoxPK> implements FechamentoEncalheBoxRepository {

	public FechamentoEncalheBoxRepositoryImpl() {
		super(FechamentoEncalheBox.class);
	}
	
	
	@Override
	public List<FechamentoEncalheBox> buscarFechamentoEncalheBox(
			FiltroFechamentoEncalheDTO filtro) {
		
		Criteria criteria = this.getSession().createCriteria(FechamentoEncalheBox.class);
		criteria.add(Restrictions.eq("fechamentoEncalheBoxPK.box.id", filtro.getBoxId()));
		criteria.add(Restrictions.eq("fechamentoEncalheBoxPK.fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe", filtro.getDataEncalhe()));
		return criteria.list();

	}
	

}
