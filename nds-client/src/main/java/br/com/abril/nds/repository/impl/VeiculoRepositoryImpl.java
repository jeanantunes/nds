package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.repository.VeiculoRepository;

@Repository
public class VeiculoRepositoryImpl extends AbstractRepository<Veiculo, Long>
		implements VeiculoRepository {

	public VeiculoRepositoryImpl() {
		super(Veiculo.class);
	}

	@Override
	public void removerPorId(Long idVeiculo) {
		
		Query query = this.getSession().createQuery("delete from Veiculo where id = :idVeiculo");
		query.setParameter("idVeiculo", idVeiculo);
		
		query.executeUpdate();
	}
}