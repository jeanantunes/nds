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

	/*
	 * Obs: Não é necessário passar a data da operação nesse metodo pois a pesquisa é feita
	 * em uma tabela que só tem os dados atuais.
	 */
	List<SuplementarFecharDiaDTO> obterDadosGridSuplementar();

}
