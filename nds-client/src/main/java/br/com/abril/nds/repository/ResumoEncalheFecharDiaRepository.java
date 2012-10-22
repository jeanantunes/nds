package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;


public interface ResumoEncalheFecharDiaRepository {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);
	
	

}
