package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.repository.BairroRepository;

@Repository
public class BairroRepositoryImpl extends AbstractRepositoryModel<Bairro, Long>
		implements BairroRepository {

	public BairroRepositoryImpl() {
		super(Bairro.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bairro> pesquisarBairros(String nomeBairro) {

		Query query = this.getSession().createQuery(
				"select l from Bairro l where l.nome like :nome ");
		query.setParameter("nome", "%" + nomeBairro + "%");

		return query.list();
	}

}