package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PessoaJuridica> buscarPorCnpj(String cnpj) {
		cnpj = "345";
		String hql = "from PessoaJuridica";
		Query query = getSession().createQuery(hql);
		//query.setParameter("cnpj", cnpj);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<PessoaFisica> obterSociosPorFiador(Long idFiador){
		
		StringBuilder hql = new StringBuilder("select f.socios from Fiador f ");
		hql.append(" where f.id = :idFiador ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return query.list();
	}

	@Override
	public PessoaFisica buscarPorCPF(String cpf) {
		
		Criteria criteria = this.getSession().createCriteria(PessoaFisica.class);
		criteria.add(Restrictions.eq("cpf", cpf));
		criteria.setMaxResults(1);
		
		return (PessoaFisica) criteria.uniqueResult();
	}
}
