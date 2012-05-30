package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;


public interface MovimentoFinanceiroCotaRepository extends Repository<MovimentoFinanceiroCota, Long> {


	/**
	 * Obtem MovimentoFinaceiroCota relativo a um registro de MovimentoEstoqueCota. 
	 * 
	 * @param idMovimentoEstoqueCota
	 * 
	 * @return MovimentoFinanceiroCota;
	 */
	MovimentoFinanceiroCota obterMovimentoFinanceiroCotaParaMovimentoEstoqueCota(Long idMovimentoEstoqueCota);
	
	
	List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDataOperacao(Long idCota, Date dataAtual);	

	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);	
	
	Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	

	Long obterQuantidadeMovimentoFinanceiroDataOperacao(Date dataAtual);


	BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	/**
	 * Obtém os débitos e crédito relativos a uma cota para determinada data de operação.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param tiposMovimentoFinanceiroIgnorados
	 * 
	 * @return List - DebitoCreditoCotaDTO
	 */
	List<DebitoCreditoCotaDTO> obterDebitoCreditoCotaDataOperacao(
			Integer numeroCota, 
			Date dataOperacao, 
			List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados);
	
	/**
	 * Obtém movimentos financeiros de uma cota por operação
	 * @param numeroCota
	 * @param operacao
	 * @return BigDecimal valor
	 */
	BigDecimal obterSaldoCotaPorOperacao(Integer numeroCota, OperacaoFinaceira operacao);
	
	
	/**
	 * Obtém o movimentos de uma cobrança
	 * @param idCobranca
	 * @return List<MovimentoFinanceiroCota>
	 */
	List<MovimentoFinanceiroCota> obterMovimentosFinanceirosPorCobranca(Long idCobranca);
	
	
	/**
	 * Obtém movimentos financeiros de uma cobrança por operação
	 * @param idCobranca
	 * @param operacao
	 * @return BigDecimal valor
	 */
	BigDecimal obterSaldoCobrancaPorOperacao(Long idCobranca, OperacaoFinaceira operacao);
}
