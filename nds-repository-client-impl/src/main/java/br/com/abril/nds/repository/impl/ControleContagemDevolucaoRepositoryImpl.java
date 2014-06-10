package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;



@Repository
public class ControleContagemDevolucaoRepositoryImpl extends AbstractRepositoryModel<ControleContagemDevolucao,Long> implements ControleContagemDevolucaoRepository {
	
	public ControleContagemDevolucaoRepositoryImpl(){
		super(ControleContagemDevolucao.class);
	}

	/**
	 * Obtém um registro de ControleContagemDevolucao 
	 * referente a dataOperacao e idProdutoEdicao.
	 * 
	 * @param dataOperacao
	 * @param idProdutoEdicao
	 * 
	 * @return ControleContagemDevolucao
	 */
	public ControleContagemDevolucao obterControleContagemDevolucao(
			Date dataOperacao, Long idProdutoEdicao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select controleContagemDevolucao 							  ");		
		hql.append(" from ControleContagemDevolucao controleContagemDevolucao 	  ");
		hql.append(" where controleContagemDevolucao.data = :dataOperacao and 	  ");
		hql.append(" controleContagemDevolucao.produtoEdicao.id = :idProdutoEdicao ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (ControleContagemDevolucao) query.uniqueResult();
		
	}	
	
}
