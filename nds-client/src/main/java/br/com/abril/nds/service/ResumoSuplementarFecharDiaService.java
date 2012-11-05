package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaSuplementarDTO;



public interface ResumoSuplementarFecharDiaService {

	BigDecimal obterValorEstoqueLogico(Date dataOperacao);

	BigDecimal obterValorTransferencia(Date dataOperacao);

	BigDecimal obterValorVenda(Date dataOperacao);

	BigDecimal obterValorFisico(Date dataOperacao);

	List<VendaSuplementarDTO> obterVendasSuplementar(Date date);
	
	ResumoSuplementarFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacional);

}
