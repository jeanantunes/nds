package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.repository.FiadorRepository;

@Repository
public class FiadorRepositoryImpl extends AbstractRepository<Fiador, Long> implements FiadorRepository {

	public FiadorRepositoryImpl() {
		super(Fiador.class);
	}

	@Override
	public Fiador obterFiadorPorCpf(String cpf) {
		StringBuilder hql = new StringBuilder("select f from Fiador f, Pessoa p ");
		hql.append(" where f.pessoa.id = p.id ")
		   .append(" and p.cpf = :cpf ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cpf", cpf);
		
		return (Fiador) query.uniqueResult();
	}

	@Override
	public Fiador obterFiadorPorCnpj(String cnpj) {
		StringBuilder hql = new StringBuilder("select f from Fiador f, Pessoa p ");
		hql.append(" where f.pessoa.id = p.id ")
		   .append(" and p.cnpj = :cnpj ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cnpj", cnpj);
		
		return (Fiador) query.uniqueResult();
	}
}