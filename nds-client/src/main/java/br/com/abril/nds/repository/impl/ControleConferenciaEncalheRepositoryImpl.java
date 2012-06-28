package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;

@Repository
public class ControleConferenciaEncalheRepositoryImpl extends AbstractRepositoryModel<ControleConferenciaEncalhe,Long> implements ControleConferenciaEncalheRepository {

	public ControleConferenciaEncalheRepositoryImpl() {
		super(ControleConferenciaEncalhe.class);
	}
	
	/**
	 * Obtem ControleConferenciaEncalhe pela data de operacao.
	 * 
	 * @param dataOperacao
	 * 
	 * @return ControleConferenciaEncalhe
	 */
	public ControleConferenciaEncalhe obterControleConferenciaEncalhe(Date dataOperacao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select c from ControleConferenciaEncalhe c ");		
		
		hql.append(" where ");		

		hql.append(" c.data = :dataOperacao ");		

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataOperacao", dataOperacao);
		
		return (ControleConferenciaEncalhe) query.uniqueResult();
		
	}

	@Override
	public StatusOperacao obterStatusConferenciaDataOperacao() {
		
		StringBuilder hql = new StringBuilder("select cce.status ");
		hql.append(" from ControleConferenciaEncalhe cce, Distribuidor d ")
		   .append(" where cce.data = d.dataOperacao");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setMaxResults(1);
		
		return (StatusOperacao) query.uniqueResult();
	}



	
}
