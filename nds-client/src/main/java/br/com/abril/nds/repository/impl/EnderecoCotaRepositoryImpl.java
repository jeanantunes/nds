package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.repository.EnderecoCotaRepository;

@Repository
public class EnderecoCotaRepositoryImpl extends AbstractRepositoryModel<EnderecoCota, Long>
		implements EnderecoCotaRepository {

	public EnderecoCotaRepositoryImpl() {
		super(EnderecoCota.class);
	}

	/**
	 * @see br.com.abril.nds.repository.EnderecoCotaRepository#removerEnderecosCota(java.util.List)
	 */
	@Override
	public void removerEnderecosCota(Long idCota, List<Endereco> listaEndereco) {

		StringBuilder hql = new StringBuilder();

		hql.append(" delete from EnderecoCota enderecoCota ")
		   .append(" where enderecoCota.cota.id = :idCota ")
		   .append(" and endereco in (:enderecos) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idCota", idCota);
		query.setParameterList("enderecos", listaEndereco);

		query.executeUpdate();
	}
	
	@Override
	public EnderecoCota getPrincipal(Long idCota){
		Criteria criteria = getSession().createCriteria(EnderecoCota.class);
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);
		
		return (EnderecoCota) criteria.uniqueResult();
	}
	
	@Override
	public EnderecoCota obterEnderecoPorTipoEndereco(Long idCota, TipoEndereco tipoEndereco){
		
		Criteria criteria = getSession().createCriteria(EnderecoCota.class);
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("tipoEndereco", tipoEndereco));
		criteria.setMaxResults(1);
		
		return (EnderecoCota) criteria.uniqueResult();
	}
}