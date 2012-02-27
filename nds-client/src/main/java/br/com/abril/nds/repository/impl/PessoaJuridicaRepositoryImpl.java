package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.PessoaJuridicaRepository;

/**
 * Implementação do repositório de Pessoa
 * 
 * @author francisco.garcia
 * 
 */
@Repository
public class PessoaJuridicaRepositoryImpl extends
		AbstractRepository<PessoaJuridica, Long> implements
		PessoaJuridicaRepository {

	public PessoaJuridicaRepositoryImpl() {
		super(PessoaJuridica.class);
	}

	@Override
	public PessoaJuridica buscarPorCnpj(String cnpj) {

		String hql = "from PessoaJuridica where cnpj = :cnpj";

		Query query = getSession().createQuery(hql);

		query.setParameter("cnpj", cnpj);

		return (PessoaJuridica) query.uniqueResult();
	}

	@Override
	public PessoaJuridica buscarCnpjPorFornecedor(String nomeFantasia) {
		String hql = "from PessoaJuridica where nomeFantasia = :nomeFantasia";

		Query query = getSession().createQuery(hql);

		query.setParameter("nomeFantasia", nomeFantasia);

		return (PessoaJuridica) query.uniqueResult();
	}

}
