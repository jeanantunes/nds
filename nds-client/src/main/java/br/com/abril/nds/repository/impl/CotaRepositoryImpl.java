package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
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

	@Override
	public Cota obterPorNumerDaCota(Integer numeroCota) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.add(Restrictions.eq("numeroCota", numeroCota));
		
		criteria.setMaxResults(1);
		
		return (Cota) criteria.uniqueResult();
	}

	/**
	 * @see br.com.abril.nds.repository.CotaRepository#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select enderecoCota.endereco as endereco, ")
		   .append(" enderecoCota.principal as enderecoPrincipal, ")
		   .append(" enderecoCota.tipoEndereco as tipoEndereco ")
		   .append(" from EnderecoCota enderecoCota ")
		   .append(" where enderecoCota.cota.id = :idCota ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(EnderecoAssociacaoDTO.class);
		
		query.setResultTransformer(resultTransformer);
		
		query.setParameter("idCota", idCota);
		
		return query.list();
	}
}
