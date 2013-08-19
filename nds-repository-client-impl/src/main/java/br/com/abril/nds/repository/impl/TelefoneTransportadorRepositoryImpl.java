package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneTransportador;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TelefoneTransportadorRepositoty;

@Repository
public class TelefoneTransportadorRepositoryImpl extends
		AbstractRepositoryModel<TelefoneTransportador, Long> implements
		TelefoneTransportadorRepositoty {

	public TelefoneTransportadorRepositoryImpl() {
		super(TelefoneTransportador.class);
	}

	@Override
	public Telefone pesquisarTelefonePrincipalTransportador(Long idTransportador) {
		
		Criteria criteria = this.getSession().createCriteria(TelefoneTransportador.class);
		criteria.add(Restrictions.eq("transportador.id", idTransportador));
		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);
		
		TelefoneTransportador telefoneTransportador = (TelefoneTransportador) criteria.uniqueResult();
		
		if (telefoneTransportador == null){
			
			this.getSession().createCriteria(TelefoneTransportador.class);
			criteria.add(Restrictions.eq("transportador.id", idTransportador));
			criteria.setMaxResults(1);
			
			telefoneTransportador = (TelefoneTransportador) criteria.uniqueResult();
		}
		
		return telefoneTransportador == null ? null : telefoneTransportador.getTelefone();
	}

	@Override
	public void removerTelefones(Set<Long> listaTelefoneRemover) {
		
		Query query = this.getSession().createQuery(
				"delete from TelefoneTransportador e where e.telefone.id in (:listaTelefoneRemover) ");
		
		query.setParameterList("listaTelefoneRemover", listaTelefoneRemover);
		
		query.executeUpdate();
	}

	@Override
	public TelefoneTransportador buscarTelefonePorTelefoneTransportador(
			Long idTelefone, Long idTransportador) {
		
		StringBuilder hql = new StringBuilder("select t from TelefoneTransportador t ");
		hql.append(" where t.telefone.id = :idTelefone ")
		   .append(" and   t.transportador.id = :idTransportador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTelefone", idTelefone);
		query.setParameter("idTransportador", idTransportador);
		query.setMaxResults(1);
		
		return (TelefoneTransportador) query.uniqueResult();
	}

	@Override
	public void excluirTelefonesTransportador(Long id) {
		
		Query query = this.getSession().createQuery("delete from TelefoneTransportador where transportador.id = :id");
		query.setParameter("id", id);
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesTransportador(Long id, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone, telefonePessoa) ")
		   .append(" from TelefoneTransportador t, Telefone telefonePessoa ")
		   .append(" where t.transportador.pessoaJuridica.id = :id ")
		   .append("   and t.telefone.id = telefonePessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoTelefone ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("id", id);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@Override
	public boolean verificarTelefonePrincipalTransportador(Long id,
			Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select t.id");
		hql.append(" from TelefoneTransportador t ")
		   .append(" where t.transportador.id = :id and t.principal = :indTrue ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("id", id);
		query.setParameter("indTrue", true);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		query.setMaxResults(1);
		
		return query.uniqueResult() != null;
	}
}