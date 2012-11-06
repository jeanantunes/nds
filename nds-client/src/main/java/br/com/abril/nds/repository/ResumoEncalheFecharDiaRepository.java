package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;


public interface ResumoEncalheFecharDiaRepository {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);

	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacao);
	
	

}
