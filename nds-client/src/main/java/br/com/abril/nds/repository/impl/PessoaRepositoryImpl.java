package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

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
public class PessoaRepositoryImpl extends AbstractRepositoryModel<Pessoa, Long> implements
		PessoaRepository {

	public PessoaRepositoryImpl() {
		super(Pessoa.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> buscarPorNome(String nome) {
		String hql = "from Pessoa where upper(nome) like upper(:nome) or upper(razaoSocial) like upper(:nome)";
		Query query = getSession().createQuery(hql);
		query.setParameter("nome",  "%" + nome + "%");
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
	public List<PessoaFisica> obterSociosPorFiador(Long idFiador, Set<Long> idsIgnorar, Set<String> cpfsIgnorar){
		
		StringBuilder hql = new StringBuilder("select f.socios from Fiador f left join f.socios socios ");
		hql.append(" where f.id = :idFiador ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and f.pessoa.id not in (:idsIgnorar) ");
		}
		
		if (cpfsIgnorar != null && !cpfsIgnorar.isEmpty()){
			hql.append(" and socios.cpf not in (:cpfsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		if (cpfsIgnorar != null && !cpfsIgnorar.isEmpty()){
			query.setParameterList("cpfsIgnorar", cpfsIgnorar);
		}
		
		return query.list();
	}

	@Override
	public PessoaFisica buscarPorCPF(String cpf) {
		
		Criteria criteria = this.getSession().createCriteria(PessoaFisica.class);
		criteria.add(Restrictions.eq("cpf", cpf));
		criteria.setMaxResults(1);
		
		return (PessoaFisica) criteria.uniqueResult();
	}
	
	@Override
	public PessoaJuridica buscarPorCNPJ(String cnpj) {
		
		Criteria criteria = this.getSession().createCriteria(PessoaJuridica.class);
		criteria.add(Restrictions.eq("cnpj", cnpj));
		criteria.setMaxResults(1);
		
		return (PessoaJuridica) criteria.uniqueResult();
	}
	
	@Override
	public Long buscarIdPessoaPorCPF(String cpf) {
		
		StringBuilder hql = new StringBuilder("select p.id ");
		hql.append(" from PessoaFisica p ");
		hql.append(" where p.cpf = :cpf ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cpf", cpf);
		query.setMaxResults(1);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long buscarIdPessoaPorCNPJ(String cnpj) {
		
		StringBuilder hql = new StringBuilder("select p.id ");
		hql.append(" from PessoaJuridica p ");
		hql.append(" where p.cnpj = :cnpj ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cnpj", cnpj);
		query.setMaxResults(1);
		
		return (Long) query.uniqueResult();
	}
}
