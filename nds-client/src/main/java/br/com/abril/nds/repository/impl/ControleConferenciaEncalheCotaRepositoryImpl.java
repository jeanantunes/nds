package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;

@Repository
public class ControleConferenciaEncalheCotaRepositoryImpl extends
		AbstractRepositoryModel<ControleConferenciaEncalheCota, Long> implements ControleConferenciaEncalheCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public ControleConferenciaEncalheCotaRepositoryImpl() {
		super(ControleConferenciaEncalheCota.class);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository#obterControleConferenciaEncalheCota(java.lang.Integer, java.util.Date)
	 */
	public ControleConferenciaEncalheCota obterControleConferenciaEncalheCota(Integer numeroCota, Date dataOperacao) {
			
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select controleConferenciaEncalheCota  ");
		
		hql.append(" from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" controleConferenciaEncalheCota.cota.numeroCota = :numeroCota and ");
		
		hql.append(" controleConferenciaEncalheCota.dataOperacao = :dataOperacao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);

		query.setParameter("dataOperacao", dataOperacao);
		
		return (ControleConferenciaEncalheCota) query.uniqueResult();
		
	}
	
	
}
