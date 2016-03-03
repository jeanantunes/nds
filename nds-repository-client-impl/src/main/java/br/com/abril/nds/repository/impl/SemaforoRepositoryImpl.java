package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.Semaforo;
import br.com.abril.nds.model.estoque.StatusProcessoEncalhe;
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
		hql.append(" where semaforo.dataOperacao = :data and semaforo.statusProcessoEncalhe <> 'FINALIZADO' ");
		hql.append(" order by semaforo.statusProcessoEncalhe desc,semaforo.dataFim desc ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return query.list();
	}
	
	
	public Long obterTotalSemaforosAtualizadosEm(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(*) from  semaforo ");
		hql.append(" where data_operacao = :data ");
		
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("data", data);
		
		BigInteger total =(BigInteger) query.uniqueResult();
		
		return total.longValue() ;
	}
	
	@Override
	public void atualizarStatusProcessoEncalheIniciadoEm(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" update Semaforo s");
		hql.append(" set s.statusProcessoEncalhe = :statusInterrompido ");
		hql.append(" where s.statusProcessoEncalhe = :statusIniciado ");
		if(data != null) {
			
			hql.append(" and s.dataOperacao = :data ");
		} else {
			
			hql.append(" and s.dataOperacao = (select dataOperacao from Distribuidor) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if(data != null) {
			
			query.setParameter("data", data);
		}
		query.setParameter("statusInterrompido", StatusProcessoEncalhe.INTERROMPIDO);
		query.setParameter("statusIniciado", StatusProcessoEncalhe.INICIADO);
		
		query.executeUpdate();
	}

}