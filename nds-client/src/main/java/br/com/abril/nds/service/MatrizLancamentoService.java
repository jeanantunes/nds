package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.LancamentoDTO;

public interface MatrizLancamentoService {

	List<LancamentoDTO> buscarLancamentosBalanceamento(Date inicio, Date fim, Long... idsFornecedores);
	
}
