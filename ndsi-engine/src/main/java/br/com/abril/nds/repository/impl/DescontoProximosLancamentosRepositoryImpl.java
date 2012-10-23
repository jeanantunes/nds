package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<DescontoProximosLancamentos> obterDescontoProximosLancamentosPor(
			Long idProduto, Date dataLancamento) {
		
		StringBuilder jpql = new StringBuilder();
			jpql.append(" SELECT dpl FROM DescontoProximosLancamentos dpl ")
				.append(" WHERE dpl.produto.id = :idProduto ")
				.append(" dpl.quantidadeProximosLancamaentos > 0 ")
				.append(" dpl.dataInicioDesconto > :dataLancamento ");

		Query query = this.getSession().createQuery(jpql.toString());	
		
		query.setParameter("idProduto", idProduto);
		query.setParameter("dataLancamento", dataLancamento);
		
		
		return query.list();
	}

}
