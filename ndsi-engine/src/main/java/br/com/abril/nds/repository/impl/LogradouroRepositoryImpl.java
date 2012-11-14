package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.repository.LogradouroRepository;

@Repository
public class LogradouroRepositoryImpl extends AbstractRepositoryModel<Logradouro, Long> implements
		LogradouroRepository {

	public LogradouroRepositoryImpl() {
		super(Logradouro.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Logradouro> pesquisarLogradouros(String nomeLogradouro) {
		
		Query query = 
				this.getSession().createQuery("select l from Logradouro l where l.nome like :nome ");
		query.setParameter("nome", "%" + nomeLogradouro + "%");
		
		return query.list();
	}
}