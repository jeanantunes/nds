package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.Semaforo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.SemaforoRepository;

@Repository
public class SemaforoRepositoryImpl extends
		AbstractRepositoryModel<Semaforo, Integer> implements
		SemaforoRepository {

	public SemaforoRepositoryImpl() {
		super(Semaforo.class);
	}

	public Semaforo selectForUpdate(Integer numeroCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT S.*			");
		hql.append(" FROM SEMAFORO S	");
		hql.append(" WHERE S.NUMERO_COTA = :numeroCota FOR UPDATE ");

		Query query = this.getSession().createSQLQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		((org.hibernate.SQLQuery) query).addEntity(Semaforo.class);

		return (Semaforo) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Semaforo> obterSemaforosAtualizadosEm(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select semaforo from Semaforo semaforo ");
		hql.append(" join fetch semaforo.usuario ");
		hql.append(" where semaforo.dataOperacao = :data ");
		hql.append(" order by semaforo.dataInicio desc ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return query.list();
	}

}
