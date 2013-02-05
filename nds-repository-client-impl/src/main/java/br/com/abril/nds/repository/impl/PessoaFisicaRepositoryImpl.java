package br.com.abril.nds.repository.impl;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.PessoaFisicaRepository;

/**
 * Implementação do repositório
 * {@link br.com.abril.nds.repository.PessoaFisicaRepository}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class PessoaFisicaRepositoryImpl extends AbstractRepositoryModel<PessoaFisica, Long>
										implements PessoaFisicaRepository {

	/**
	 * Construtor.
	 */
	public PessoaFisicaRepositoryImpl() {
		super(PessoaFisica.class);
	}

	/**
	 * @see br.com.abril.nds.repository.PessoaFisicaRepository#buscarPorCpf(java.lang.String)
	 */
	@Override
	public PessoaFisica buscarPorCpf(String cpf) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from PessoaFisica pessoaFisica ")
		   .append(" where pessoaFisica.cpf = :cpf ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("cpf", cpf);
		
		return (PessoaFisica) query.uniqueResult();
	}
}
