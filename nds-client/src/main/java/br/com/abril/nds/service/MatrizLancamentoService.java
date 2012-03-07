package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoLancamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;

public interface MatrizLancamentoService {

	List<LancamentoDTO> buscarLancamentosBalanceamento(FiltroLancamentoDTO filtro);
	
	SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores);

	List<ResumoPeriodoLancamentoDTO> obterResumoPeriodo(Date dataInicial,
			List<Long> fornecedores);
	
}
