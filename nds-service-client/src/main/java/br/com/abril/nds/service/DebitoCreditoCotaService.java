package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;

public interface DebitoCreditoCotaService {

	MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(DebitoCreditoDTO debitoCreditoDTO);
	
	/**
	 * Obtém dados pré-configurados com informações das Cotas do Box, Rota e Roteiro. Para lançamentos de débitos e/ou créditos
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return List<DebitoCreditoDTO>
	 */
	List<DebitoCreditoDTO> obterDadosLancamentoPorBoxRoteiroRota(Long idBox,Long idRoteiro,Long idRota,BigDecimal percentual,BaseCalculo baseCalculo,Date dataPeriodoInicial,Date dataPeriodoFinal);
	
	/**
	 * Obtém Quantidade de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return Número de Cotas encontradas
	 */
	int obterQuantidadeCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota);
	
	/**
	 * Obtém dados pré-configurados com informações da Cota para lançamento de débito e/ou crédito
	 * @param numeroCota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return DebitoCreditoDTO
	 */
	DebitoCreditoDTO obterDadosLancamentoPorCota(Integer numeroCota,BigDecimal percentual,BaseCalculo baseCalculo,Date dataPeriodoInicial,Date dataPeriodoFinal,Long index);
}
