package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;



public interface ResumoEncalheFecharDiaService {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);
	
	

}
