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
import br.com.abril.nds.model.cadastro.TelefoneFiador;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TelefoneFiadorRepository;

@Repository
public class TelefoneFiadorRepositoryImpl extends AbstractRepositoryModel<TelefoneFiador, Long> 
										  implements TelefoneFiadorRepository {

	public TelefoneFiadorRepositoryImpl() {
		super(TelefoneFiador.class);
	}

	@Override
	public void removerTelefonesFiador(Collection<Long> listaTelefones) {
		
		StringBuilder hql = new StringBuilder("delete from TelefoneFiador ");
		hql.append(" where telefone.id in (:ids) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", listaTelefones);
		
		query.executeUpdate();
	}

	@Override
	public Telefone pesquisarTelefonePrincipalFiador(Long idFiador) {
		
		Criteria criteria = this.getSession().createCriteria(TelefoneFiador.class);
		criteria.add(Restrictions.eq("fiador.id", idFiador));
		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);
		
		TelefoneFiador telefoneFiador = (TelefoneFiador) criteria.uniqueResult();
		
		if (telefoneFiador == null){
			
			criteria = this.getSession().createCriteria(TelefoneFiador.class);
			criteria.add(Restrictions.eq("fiador.id", idFiador));
			criteria.setMaxResults(1);
			
			telefoneFiador = (TelefoneFiador) criteria.uniqueResult();
		}
		
		return telefoneFiador == null ? null : telefoneFiador.getTelefone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFiador(Long idFiador, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone, telefonePessoa) ")
		   .append(" from TelefoneFiador t, Telefone telefonePessoa ")
		   .append(" where t.fiador.id = :idFiador ")
		   .append("   and t.telefone.id = telefonePessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoTelefone ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Telefone> buscarTelefonesPessoaPorFiador(Long idFiador) {
		StringBuilder hql = new StringBuilder("select c.pessoa.telefones ");
		hql.append(" from Fiador c ")
		   .append(" where c.id = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return query.list();
	}
	
	@Override
	public TelefoneFiador obterTelefonePorTelefoneFiador(Long idTelefone, Long idFiador){
		
		StringBuilder hql = new StringBuilder("select t from TelefoneFiador t ");
		hql.append(" where t.telefone.id = :idTelefone ")
		   .append(" and   t.fiador.id   = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTelefone", idTelefone);
		query.setParameter("idFiador", idFiador);
		query.setMaxResults(1);
		
		return (TelefoneFiador) query.uniqueResult();
	}
	
	@Override
	public void excluirTelefonesFiador(Long idFiador){
		
		Query query = this.getSession().createQuery("delete from TelefoneFiador where fiador.id = :idFiador ");
		query.setParameter("idFiador", idFiador);
		
		query.executeUpdate();
	}

	@Override
	public boolean verificarTelefonePrincipalFiador(Long id, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select t.id");
		hql.append(" from TelefoneFiador t ")
		   .append(" where t.fiador.id = :id and t.principal = :indTrue ");
		
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