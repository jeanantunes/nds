package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;

public interface DebitoCreditoCotaService {

	MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(DebitoCreditoDTO debitoCreditoDTO);
	
	/**
	 * Obtém dados pré-configurados com informações da Cota para lançamentos de débitos e/ou créditos
	 * @return List<DebitoCreditoDTO>
	 */
	List<DebitoCreditoDTO> obterDadosLancamentoPorBoxRoteiroRota(Long idBox,Long idRoteiro,Long idRota);
	
}
