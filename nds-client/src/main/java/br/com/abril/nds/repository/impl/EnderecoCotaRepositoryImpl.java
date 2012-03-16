package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.repository.EnderecoCotaRepository;

@Repository
public class EnderecoCotaRepositoryImpl extends AbstractRepository<EnderecoCota, Long>
		implements EnderecoCotaRepository {

	public EnderecoCotaRepositoryImpl() {
		super(EnderecoCota.class);
	}

	/**
	 * @see br.com.abril.nds.repository.EnderecoCotaRepository#removerEnderecosCota(java.util.List)
	 */
	@Override
	public void removerEnderecosCota(Long idCota, List<Endereco> listaEndereco) {

		StringBuilder hql = new StringBuilder();

		hql.append(" delete from EnderecoCota enderecoCota ")
		   .append(" where enderecoCota.cota.id = :idCota ")
		   .append(" and endereco in (:enderecos) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("enderecos", listaEndereco);

		query.executeUpdate();
	}
}
