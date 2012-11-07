package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;


public interface ResumoSuplementarFecharDiaRepository {

	BigDecimal obterValorEstoqueLogico(Date dataOperacao);

	BigDecimal obterValorTransferencia(Date dataOperacao);

	BigDecimal obterValorVenda(Date dataOperacao);

	BigDecimal obterValorFisico(Date dataOperacao);

	List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao);

	List<SuplementarFecharDiaDTO> obterDadosGridSuplementar();

}
