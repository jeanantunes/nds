package br.com.abril.nds.repository.impl;

import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;

@Repository
public class AssociacaoVeiculoMotoristaRotaRepositoryImpl extends
		AbstractRepository<AssociacaoVeiculoMotoristaRota, Long> implements
		AssociacaoVeiculoMotoristaRotaRepository {

	public AssociacaoVeiculoMotoristaRotaRepositoryImpl() {
		super(AssociacaoVeiculoMotoristaRota.class);
	}

	@Override
	public void removerAssociacaoPorId(Set<Long> ids) {
		
		Query query = this.getSession().createQuery("delete from AssociacaoVeiculoMotoristaRota where id in (:ids)");
		
		query.setParameterList("ids", ids);
		
		query.executeUpdate();
	}
}