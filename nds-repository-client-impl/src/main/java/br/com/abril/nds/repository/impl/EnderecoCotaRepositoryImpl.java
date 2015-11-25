package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.vo.EnderecoVO;

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
	
	@Override
	public Long obterQtdEnderecoAssociadoCota(Long idCota){
		
		StringBuilder hql = new StringBuilder("select distinct ");
		hql.append(" count(endCota.id) + count(endPes.id) ")
		   .append(" from Cota cota ")
		   .append(" join cota.enderecos endCota ")
		   .append(" join cota.pessoa pesCota ")
		   .append(" left join pesCota.enderecos endPes ")
		   .append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemDTO<String, String>> buscarMunicipio() {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select endereco.cidade as key, endereco.cidade as value ")
		   .append(" from EnderecoCota enderecoCota ")
		   .append(" join enderecoCota.endereco endereco ")
		   .append(" group by endereco.cidade");

		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Endereco> enderecosIncompletos() {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Endereco e ")
		  .append(" where (e.bairro = 'NAO_INFORMADO' OR  e.bairro is null) ")
		  .append(" and e.cep is not null");
		  
		  Query query = this.getSession().createQuery(hql.toString());
		  
		  return query.list();
	}
}