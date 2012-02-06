package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Pessoa;
import br.com.abril.nds.repository.PessoaRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author francisco.garcia
 *
 */
@Repository
public class PessoaRepositoryImpl extends AbstractRepository<Pessoa, Long> implements
		PessoaRepository {

	public PessoaRepositoryImpl() {
		super(Pessoa.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> buscarPorNome(String nome) {
		String hql = "from Pessoa where upper(nome) like upper(:nome) or upper(razaoSocial) like upper(:nome)";
		Query query = getSession().createQuery(hql);
		query.setParameter("nome",  nome + "%");
		return query.list();
	}

	
}
