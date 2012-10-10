package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ResumoFecharDiaRepository;

@Repository
public class ResumoFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoFecharDiaRepository {

	@Override
	public BigDecimal obterValorReparte(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT SUM(pe.precoVenda) from Lancamento l ");
		hql.append(" JOIN l.produtoEdicao as pe ");
		hql.append(" WHERE l.dataLancamentoPrevista = :dataOperacaoDistribuidor ");
		hql.append(" AND l.status IN ( :listaStatus )");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusLancamento> listaStatus = new ArrayList<>();
		
		listaStatus.add(StatusLancamento.CONFIRMADO);
		listaStatus.add(StatusLancamento.BALANCEADO);
		listaStatus.add(StatusLancamento.ESTUDO_FECHADO);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaStatus", listaStatus);
		
		return (BigDecimal) query.uniqueResult();
	}

}
