package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.Lancamento;

public interface LancamentoRepository extends Repository<Lancamento, Long> {
	
	List<Lancamento> obterLancamentosBalanceamentoMartriz(Date inicio, Date fim, Long[] idsFornecedores);

}
