package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;

@Repository
public class ControleConferenciaEncalheRepositoryImpl extends AbstractRepository<ControleConferenciaEncalhe, Long> 
													  implements ControleConferenciaEncalheRepository {

	public ControleConferenciaEncalheRepositoryImpl() {
		super(ControleConferenciaEncalhe.class);
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