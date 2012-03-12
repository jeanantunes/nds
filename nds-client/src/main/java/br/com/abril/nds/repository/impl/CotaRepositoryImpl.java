package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.CotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class CotaRepositoryImpl extends AbstractRepository<Cota, Long> implements CotaRepository {

	/**
	 * Construtor.
	 */
	public CotaRepositoryImpl() {
		
		super(Cota.class);
	}

	public Cota obterPorNumerDaCota(Integer numeroCota) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.add(Restrictions.eq("numeroCota", numeroCota));
		
		criteria.setMaxResults(1);
		
		return (Cota) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasPorNomePessoa(String nome) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.createAlias("pessoa", "pessoa");
		
		criteria.add(
			Restrictions.or(
				Restrictions.ilike("pessoa.nome", nome, MatchMode.ANYWHERE),
				Restrictions.ilike("pessoa.razaoSocial", nome, MatchMode.ANYWHERE)
			)
		);
		
		return criteria.list();
	}

	public Cota obterPorNome(String nome) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.createAlias("pessoa", "pessoa");
		
		criteria.add(
			Restrictions.or(
				Restrictions.eq("pessoa.nome", nome),
				Restrictions.eq("pessoa.razaoSocial", nome)
			)
		);
		
		criteria.setMaxResults(1);
		
		return (Cota) criteria.uniqueResult();
	}

}
