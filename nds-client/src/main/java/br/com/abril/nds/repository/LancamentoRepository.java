package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ResumoPeriodoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.planejamento.Lancamento;

public interface LancamentoRepository extends Repository<Lancamento, Long> {
	
	List<Lancamento> obterLancamentosBalanceamentoMartriz(FiltroLancamentoDTO filtro);

	void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista);

	List<ResumoPeriodoLancamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores);
}
