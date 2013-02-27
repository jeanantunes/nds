package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TelefoneCotaRepository;

@Repository
public class TelefoneCotaRepositoryImpl extends AbstractRepositoryModel<TelefoneCota, Long> 
										implements TelefoneCotaRepository {

	public TelefoneCotaRepositoryImpl() {
		super(TelefoneCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone, telefonePessoa) ")
		   .append(" from TelefoneCota t, Telefone telefonePessoa ")
		   .append(" where t.cota.id = :idCota ")
		   .append("   and t.telefone.id = telefonePessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoTelefone ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@Override
	public void removerTelefonesCota(Collection<Long> listaTelefones) {
		StringBuilder hql = new StringBuilder("delete from TelefoneCota ");
		hql.append(" where telefone.id in (:ids) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", listaTelefones);
		
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Telefone> buscarTelefonesPessoaPorCota(Long idCota) {
		StringBuilder hql = new StringBuilder("select c.pessoa.telefones ");
		hql.append(" from Cota c ")
		   .append(" where c.id = :idCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return query.list();
	}
	
	@Override
	public TelefoneCota obterTelefonePrincipal(long idCota){
		Criteria criteria = getSession().createCriteria(TelefoneCota.class );
		criteria.add(Restrictions.eq("cota.id", idCota));

		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);
		
		return (TelefoneCota) criteria.uniqueResult();
		
	}
	
	@Override
	public Long obterQtdTelefoneAssociadoCota(Long idCota){
		
		StringBuilder hql = new StringBuilder("select distinct ");
		hql.append(" count(telCota.id) + count(telPes.id) ")
		   .append(" from Cota cota ")
		   .append(" join cota.telefones telCota ")
		   .append(" join cota.pessoa pesCota ")
		   .append(" left join pesCota.telefones telPes ")
		   .append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return (Long) query.uniqueResult();
	}
}