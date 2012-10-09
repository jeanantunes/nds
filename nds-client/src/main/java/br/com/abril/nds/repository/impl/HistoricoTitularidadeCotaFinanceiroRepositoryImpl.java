package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.repository.HistoricoTitularidadeCotaFinanceiroRepository;

@Repository
public class HistoricoTitularidadeCotaFinanceiroRepositoryImpl extends AbstractRepositoryModel<HistoricoTitularidadeCotaFinanceiro, Long> implements HistoricoTitularidadeCotaFinanceiroRepository{

	public HistoricoTitularidadeCotaFinanceiroRepositoryImpl() {
		super(HistoricoTitularidadeCotaFinanceiro.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoTitularidadeCotaFinanceiro> pesquisarTodos() {
		return this.getSession().createQuery("select l from HistoricoTitularidadeCotaFinanceiro l").list();
	}
}