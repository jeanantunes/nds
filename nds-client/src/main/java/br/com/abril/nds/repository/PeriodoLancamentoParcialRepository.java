package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;

public interface PeriodoLancamentoParcialRepository  extends Repository<PeriodoLancamentoParcial, Long>  {

	/**
	 * Retorna DTOs de periodos parciais de acordo com o filtro
	 * 
	 * @param filtro
	 */
	List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro);

	/**
	 * Retorna COUNT total da pesquisa "obterPeriodosParciais"
	 * 
	 * @param filtro
	 * @return
	 */
	Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro);
	
	/**
	 * Obtém Periodo por id do Lancamento
	 * 
	 * @param idLancamento
	 * @return
	 */
	PeriodoLancamentoParcial obterPeriodoPorIdLancamento(Long idLancamento);

	/**
	 * Retorna se novo periodo é válido para o Lancamento
	 * 
	 * @param idLancamento - Id do lançamento a ser alterado
	 * @param dataLancamento - Nova data de Lancamento
	 * @param dataRecolhimento - Nova data de Recolhimento
	 * @return
	 */
	Boolean verificarValidadeNovoPeriodoParcial(Long idLancamento,
			Date dataLancamento, Date dataRecolhimento);
	
	List<ParcialVendaDTO> obterDetalhesVenda();
}
