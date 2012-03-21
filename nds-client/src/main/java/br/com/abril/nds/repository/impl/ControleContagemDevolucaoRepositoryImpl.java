package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;



@Repository
public class ControleContagemDevolucaoRepositoryImpl extends AbstractRepository<ControleContagemDevolucao,Long> implements ControleContagemDevolucaoRepository {
	
	public ControleContagemDevolucaoRepositoryImpl(){
		super(ControleContagemDevolucao.class);
	}

	/**
	 * Obtém um registro de ControleContagemDevolucao referente a dataOperacao.
	 * 
	 * @param dataOperacao
	 * 
	 * @return ControleContagemDevolucao
	 */
	public ControleContagemDevolucao obterControleContagemDevolucao(Date dataOperacao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select controleContagemDevolucao 											");		
		hql.append(" from ControleContagemDevolucao controleContagemDevolucao 					");
		hql.append(" where controleContagemDevolucao.data = :dataOperacao						");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataOperacao", dataOperacao);
		
		return (ControleContagemDevolucao) query.uniqueResult();
		
	}	
	
}
