package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;

public interface HistoricoTitularidadeCotaFinanceiroRepository extends Repository<HistoricoTitularidadeCotaFinanceiro, Long>{

	List<HistoricoTitularidadeCotaFinanceiro> pesquisarTodos();
}