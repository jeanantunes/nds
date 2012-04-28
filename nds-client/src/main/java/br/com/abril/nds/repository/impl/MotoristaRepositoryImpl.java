package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Motorista;

import br.com.abril.nds.repository.MotoristaRepository;

@Repository
public class MotoristaRepositoryImpl extends
		AbstractRepository<Motorista, Long> implements MotoristaRepository{

	public MotoristaRepositoryImpl() {
		super(Motorista.class);
	}

	@Override
	public void removerPorId(Long idMotorista) {
		
		Query query = this.getSession().createQuery("delete from Motorista where id = :idMotorista");
		query.setParameter("idMotorista", idMotorista);
		
		query.executeUpdate();
	}
}