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
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.repository.TelefonePdvRepository;

@Repository
public class TelefonePdvRepositoryImpl extends AbstractRepositoryModel<TelefonePDV, Long> 
										  implements TelefonePdvRepository {

	public TelefonePdvRepositoryImpl() {
		super(TelefonePDV.class);
	}

	@Override
	public void removerTelefonesPdv(Collection<Long> listaTelefones) {
		
		StringBuilder hql = new StringBuilder("delete from TelefonePDV ");
		hql.append(" where telefone.id in (:ids) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", listaTelefones);
		
		query.executeUpdate();
	}

	@Override
	public Telefone pesquisarTelefonePrincipalPdv(Long idPdv) {
		
		Criteria criteria = this.getSession().createCriteria(TelefonePDV.class);
		criteria.add(Restrictions.eq("pdv.id", idPdv));
		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);
		
		TelefonePDV telefonePdv = (TelefonePDV) criteria.uniqueResult();
		
		if (telefonePdv == null){
			
			criteria = this.getSession().createCriteria(TelefonePDV.class);
			criteria.add(Restrictions.eq("pdv.id", idPdv));
			criteria.setMaxResults(1);
			
			telefonePdv = (TelefonePDV) criteria.uniqueResult();
		}
		
		return telefonePdv == null ? null : telefonePdv.getTelefone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesPdv(Long idPdv, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone, telefonePessoa) ")
		   .append(" from TelefonePDV t, Telefone telefonePessoa ")
		   .append(" where t.pdv.id = :idPdv ")
		   .append("   and t.telefone.id = telefonePessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoTelefone ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPdv", idPdv);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Telefone> buscarTelefonesPessoaPorPdv(Long idPdv) {
		StringBuilder hql = new StringBuilder("select telefones.telefone ");
		hql.append(" from PDV c ")
		   .append(" inner join c.telefones as telefones ")
		   .append(" where c.id = :idPdv");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPdv", idPdv);
		
		return query.list();
	}
	
	@Override
	public TelefonePDV obterTelefonePorTelefonePdv(Long idTelefone, Long idPdv){
		
		StringBuilder hql = new StringBuilder("select t from TelefonePDV t ");
		hql.append(" where t.telefone.id = :idTelefone ")
		   .append(" and   t.pdv.id   = :idPdv");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTelefone", idTelefone);
		query.setParameter("idPdv", idPdv);
		query.setMaxResults(1);
		
		return (TelefonePDV) query.uniqueResult();
	}
	
	@Override
	public void excluirTelefonesPdv(Long idPdv){
		
		Query query = this.getSession().createQuery("delete from TelefonePDV where pdv.id = :idPdv ");
		query.setParameter("idPdv", idPdv);
		
		query.executeUpdate();
	}
}