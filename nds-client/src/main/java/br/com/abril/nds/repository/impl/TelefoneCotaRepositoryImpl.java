package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.repository.TelefoneCotaRepository;

@Repository
public class TelefoneCotaRepositoryImpl extends AbstractRepository<TelefoneCota, Long> 
										implements TelefoneCotaRepository {

	public TelefoneCotaRepositoryImpl() {
		super(TelefoneCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone) ")
		   .append(" from TelefoneCota t ")
		   .append(" where t.cota.id = :idCota ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@Override
	public void removerTelefonesCota(Collection<Long> listaTelefonesCota) {
		StringBuilder hql = new StringBuilder("delete from TelefoneCota ");
		hql.append(" where id in (:idsCota) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idsCota", listaTelefonesCota);
		
		query.executeUpdate();
	}
}