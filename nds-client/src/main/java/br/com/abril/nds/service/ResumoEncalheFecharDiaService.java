package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;



public interface ResumoEncalheFecharDiaService {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);
	
	ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao);

}
