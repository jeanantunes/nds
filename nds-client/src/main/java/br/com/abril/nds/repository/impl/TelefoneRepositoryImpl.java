package br.com.abril.nds.repository.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.TelefoneRepository;

@Repository
public class TelefoneRepositoryImpl extends AbstractRepository<Telefone, Long> implements TelefoneRepository {

	public TelefoneRepositoryImpl() {
		super(Telefone.class);
	}

	@Override
	public void removerTelefones(Collection<Long> idsTelefones) {
		StringBuilder hql = new StringBuilder("delete from Telefone where id in (:ids)");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", idsTelefones);
		
		query.executeUpdate();
	}
}