package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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
	public List<TelefoneCota> buscarTelefonesCota(Long idCota) {
		StringBuilder hql = new StringBuilder("from TelefoneCota ");
		hql.append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return query.list();
	}

	@Override
	public void removerTelefonesCota(List<Long> listaTelefonesCota) {
		StringBuilder hql = new StringBuilder("delete from TelefoneCota ");
		hql.append(" where id in (:idsCota) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idsCota", listaTelefonesCota);
		
		query.executeUpdate();
	}
}