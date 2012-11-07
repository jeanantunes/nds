package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;


public interface ResumoEncalheFecharDiaRepository {

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);

	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacao);

	BigDecimal obterValorFaltasOuSobras(Date dataOperacao, StatusAprovacao status);
	
	

}
