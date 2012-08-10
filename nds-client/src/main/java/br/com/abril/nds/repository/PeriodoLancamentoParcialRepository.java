package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
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
	
	
	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoEdicao
	 * @return List<ParcialVendaDTO>
	 */
	List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento, Date dataRecolhimento, Long idProdutoEdicao);

	/**
	 * Obtém lançamento de periodo parcial posterior.
	 * @param dataRecolhimento 
	 * 
	 * @param idLancamento
	 * @return
	 */
	Lancamento obterLancamentoPosterior(Long idProdutoEdicao, Date dataRecolhimento);

	/**
	 * Obtém lançamento de periodo parcial anterior.
	 * @param dataRecolhimento 
	 * 
	 * @param idLancamento
	 * @return
	 */
	Lancamento obterLancamentoAnterior(Long idProdutoEdicao, Date dataLancamento);

}
