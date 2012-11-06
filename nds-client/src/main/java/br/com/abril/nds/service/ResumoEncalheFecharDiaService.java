package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;



public interface ResumoEncalheFecharDiaService {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);
	
	ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao);
	
	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacao);

}
