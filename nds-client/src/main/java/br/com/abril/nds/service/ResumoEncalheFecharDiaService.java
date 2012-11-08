package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;



public interface ResumoEncalheFecharDiaService {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);
	
	ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao);
	
	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacao);
	
	BigDecimal obterValorFaltas(Date dataOperacao);

	BigDecimal obterValorSobras(Date dataOperacao);
	
	/**
	 * Retorna os dados das vendas de encalhe referentes o fechamento do dia
	 * 
	 * @param dataOperacao - data de operação
	 * 
	 * @return List<VendaFechamentoDiaDTO>
	 */
	List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao);

}
