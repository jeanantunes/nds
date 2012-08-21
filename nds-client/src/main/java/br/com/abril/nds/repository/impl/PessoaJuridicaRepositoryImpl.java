package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
		AbstractRepositoryModel<PessoaJuridica, Long> implements
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
	
	
	private boolean hasCnpjOrIE(String inscricaoEstadual, String cnpj, Long idPessoa){
		Criteria criteria = getSession().createCriteria(PessoaJuridica.class);
		if(cnpj != null){
			criteria.add(Restrictions.eq("cnpj", cnpj));
		}
		
		if(inscricaoEstadual != null){
			criteria.add(Restrictions.eq("inscricaoEstadual", inscricaoEstadual));
		}
		
		if(idPessoa != null){
			criteria.add(Restrictions.not(Restrictions.idEq(idPessoa)));
		}
		
		criteria.setProjection(Projections.rowCount());
		
		return (Long)criteria.uniqueResult() > 0;
		
	}
	
	@Override
	public boolean hasCnpj( String cnpj, Long idPessoa){
		return hasCnpjOrIE(null, cnpj, idPessoa);
	}
	
	@Override
	public boolean hasInscricaoEstadual(String inscricaoEstadual,Long idPessoa){
		return hasCnpjOrIE(inscricaoEstadual, null, idPessoa);
	}

}
