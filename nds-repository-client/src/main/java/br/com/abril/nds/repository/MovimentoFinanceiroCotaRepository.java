package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCota;
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
	
	
	List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCota(Long idCota);	

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
	 * 
	 * @param idCobranca
	 * @param operacao
	 * @return BigDecimal valor
	 */
	BigDecimal obterSaldoCobrancaPorOperacao(Long idCobranca, OperacaoFinaceira operacao);
	
	/**
	 * Obtém Débito e Crédito sumarizados relativos a cota.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param tiposMovimentoFinanceiroIgnorados
	 * 
	 * @return List - DebitoCreditoCotaDTO
	 */
	List<DebitoCreditoCotaDTO> obterDebitoCreditoSumarizadosParaCotaDataOperacao(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados);


	/**
	 * Obtém faturamento das cotas por período
	 * @param cotas
	 * @param dataInicial
	 * @param dataFinal
	 * @return List<CotaFaturamentoDTO>: Faturamento das Cotas
	 */
	List<CotaFaturamentoDTO> obterFaturamentoCotasPorPeriodo(List<Cota> cotas, Date dataInicial, Date dataFinal);

	/**
	 * Obtém o valor totald entre Debito e Cédito dos movimentos financeiros.
	 * 
	 * @param filtro
	 * @param tiposMovimentoFinanceiroIgnorados
	 * @return
	 */
	List<DebitoCreditoCotaDTO> obterDebitoCreditoPorPeriodoOperacao(FiltroConsultaEncalheDTO filtro, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados);
	
	/**
	 * Obtém dados de transportador por periodo
	 * 
	 * @param dataDe
	 * @param dataAte
	 * @param idTransportador
	 * @param paginacaoVO 
	 * @return
	 */
	List<CotaTransportadorDTO> obterResumoTransportadorCota(FiltroRelatorioServicosEntregaDTO filtro); 
	
	/**
	 * Obtém detalhes Financeiros do envio de Reparte das Cotas de determinado Transportador
	 * 
	  * @param dataDe - Data de início da pesquisa
	 * @param dataAte - Data de fim da pesquisa
	 * @param idTransportador - Identificador do Transportador
	 * @param idCota - Identificador da cota
	 * @return
	 */
	List<MovimentoFinanceiroDTO> obterDetalhesTrasportadorPorCota(FiltroRelatorioServicosEntregaDTO filtro);
	
	Long obterCountResumoTransportadorCota(FiltroRelatorioServicosEntregaDTO filtro);
	
	BigDecimal obterSaldoDistribuidor(Date data, 
							 	      TipoCota tipoCota, 
							 	      OperacaoFinaceira operacaoFinaceira);


	List<Long> obterIdsMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);


	List<MovimentoFinanceiroCota> obterMovimentosFinanceirosCotaPorTipoMovimento(
			Long idCota,
			Collection<TipoMovimentoFinanceiro> tiposMovimentoPostergado);
}