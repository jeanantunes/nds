package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;

@Repository
public class DescontoProximosLancamentosRepositoryImpl extends AbstractRepositoryModel<DescontoProximosLancamentos,Long> 
implements DescontoProximosLancamentosRepository{

	public DescontoProximosLancamentosRepositoryImpl() {
		super(DescontoProximosLancamentos.class);
	}

	
	@Override
	public DescontoProximosLancamentos obterDescontoProximosLancamentosPor(
			Long idProduto, Date dataLancamento) {
		
		StringBuilder jpql = new StringBuilder();
			jpql.append(" SELECT dpl FROM DescontoProximosLancamentos dpl ")
				.append(" WHERE dpl.produto.id = :idProduto and ")
				.append(" dpl.quantidadeProximosLancamaentos > 0 and ")
				.append(" dpl.dataInicioDesconto > :dataLancamento ");

		Query query = this.getSession().createQuery(jpql.toString());	
		
		query.setParameter("idProduto", idProduto);
		query.setParameter("dataLancamento", dataLancamento);
		
		try {
			return (DescontoProximosLancamentos) query.uniqueResult();
		} catch(HibernateException e) {
			return null;
		}
		
	}

}
