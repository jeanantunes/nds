package br.com.abril.nds.service;

import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;

public interface DebitoCreditoCotaService {

	MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(DebitoCreditoDTO debitoCreditoDTO);
	
}
