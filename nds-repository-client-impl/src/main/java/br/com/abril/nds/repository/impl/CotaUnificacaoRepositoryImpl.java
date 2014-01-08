package br.com.abril.nds.repository.impl;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaUnificacaoRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.CotaUnificacao}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class CotaUnificacaoRepositoryImpl extends AbstractRepositoryModel<CotaUnificacao,Long> implements CotaUnificacaoRepository {

	/**
	 * Construtor padrão
	 */
	public CotaUnificacaoRepositoryImpl() {
		super(CotaUnificacao.class);
	}

	/**
	 * Obtem Unificacao de Cotas por Cota Centralizadora
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(
			Integer numeroCota) {
		
        StringBuilder hql = new StringBuilder();
		
        hql.append(" select cotaUnificacao ");
		
		hql.append(" from CotaUnificacao cotaUnificacao ");
		
		hql.append(" where cotaUnificacao.cota.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return (CotaUnificacao) query.uniqueResult();
	}

	/**
	 * Obtem Unificacao de Cotas por Cota Centralizadora
	 * 
	 * @param idCota
	 * @return CotaUnificacao
	 */
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Long idCota) {
		
        StringBuilder hql = new StringBuilder();
		
        hql.append(" select cotaUnificacao ");
		
		hql.append(" from CotaUnificacao cotaUnificacao ");
		
		hql.append(" where cotaUnificacao.cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return (CotaUnificacao) query.uniqueResult(); 
	}

	/**
	 * Obtem unificacao de Cotas por Cota Centralizada
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota) {
		
		    StringBuilder hql = new StringBuilder();
			
			hql.append(" select cotaUnificacao ");
			
			hql.append(" from CotaUnificacao cotaUnificacao ");
			
			hql.append(" join cotaUnificacao.cotas as cota ");
			
			hql.append(" where cota.numeroCota = :numeroCota ");
			
			Query query = this.getSession().createQuery(hql.toString());
			
			query.setParameter("numeroCota", numeroCota);
			
			return (CotaUnificacao) query.uniqueResult(); 
	}

	/**
     * Obtem unificacao de Cotas por Cota Centralizada
     * 
     * @param idCota
     * @return CotaUnificacao
     */
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Long idCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cotaUnificacao ");
		
		hql.append(" from CotaUnificacao cotaUnificacao ");
		
		hql.append(" join cotaUnificacao.cotas as cota ");
		
		hql.append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return (CotaUnificacao) query.uniqueResult(); 
	}
}