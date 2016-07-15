package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.ProcessamentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;


public interface MovimentoFinanceiroCotaRepository extends Repository<MovimentoFinanceiroCota, Long> {

	
	/**
	 * Obtem dados financeiros de débito da cota,
	 * relativos a negociação não avulsa adicionado
	 * de encargos.
	 * 
	 * 
	 * @param numeroCota
	 * @param datas
	 * @param tiposCota
	 * @param idFornecedor
	 * 
	 * @return List - DebitoCreditoCotaDTO
	 */
	public List<DebitoCreditoCota> obterValorFinanceiroNaoConsolidadoDeNegociacaoNaoAvulsaMaisEncargos(
	        Integer numeroCota, List<Date> datas, Long idFornecedor);
	
	/**
	 * Obtem movimentos financeiros da cota ainda nao processados e não consolidados
	 * 
	 * @param idCota
	 * @param dataOperacao
	 * @param tiposCota
	 * @return List<MovimentoFinanceiroCota> 
	 */
	List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCota(Long idCota, Date dataOperacao, List<TipoCota> tiposCota);	

	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);	
	
	Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	/**
	 * Obtém os débitos e crédito relativos a uma cota para determinada data de operação.
	 * 
	 * @param numeroCota
	 * @param datas
	 * @param tiposMovimentoFinanceiroIgnorados
	 * 
	 * @return List - DebitoCreditoCotaDTO
	 */
	public List<DebitoCreditoCota> obterDebitoCreditoCotaDataOperacao(Integer numeroCota, List<Date> datas, 
			List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados, Long idFornecedor);
	
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
	List<DebitoCreditoCota> obterDebitoCreditoSumarizadosParaCotaDataOperacao(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados);


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
	List<DebitoCreditoCota> obterDebitoCreditoPorPeriodoOperacao(FiltroConsultaEncalheDTO filtro, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados);
	
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
	
	/**
	 * Obtém o movimento financeiro relativo a uma operação de conferencia de encalhe.
	 * 
	 * @param idControleConfEncalheCota
	 * 
	 * @return MovimentoFinanceiroCota
	 */
	List<MovimentoFinanceiroCota> obterMovimentoFinanceiroDaOperacaoConferenciaEncalhe(Long idControleConfEncalheCota);
	
	BigDecimal obterSaldoDistribuidor(Date data, 
							 	      TipoCota tipoCota, 
							 	      OperacaoFinaceira operacaoFinanceira);


	List<Long> obterIdsMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);


	List<MovimentoFinanceiroCota> obterMovimentosFinanceirosCotaPorTipoMovimento(
			Long idCota, Long idConsolidado,
			Collection<TipoMovimentoFinanceiro> tiposMovimento,
			Date dataCriacao);

	List<DebitoCreditoCota> obterCreditoDebitoCota(Long idConsolidado,
			Date dataCriacao, Integer numeroCota, List<TipoMovimentoFinanceiro> tiposDebitoCredito, String sortorder, String sortname);

	BigDecimal obterSomatorioTipoMovimentoPorConsolidado(Long idConsolidado,
			Date dataCriacao, Integer numeroCota, Collection<TipoMovimentoFinanceiro> tiposMovimento);
	  
   /**
    * Obtem Quantidade de Informações para o processamento financeiro (Geração de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
    * @param numeroCota
    * @return Long
    */
    Long obterQuantidadeProcessamentoFinanceiroCota(Integer numeroCota, ProdutoEdicao produtoEdicao);
	
   /**
    * Obtem Informações para o processamento financeiro (Geração de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
    * @param numeroCota
    * @param data
    * @param sortorder
    * @param sortname
    * @param initialResult
    * @param maxResults
    * @return List<ProcessamentoFinanceiroCotaDTO>
    */
    List<ProcessamentoFinanceiroCotaDTO> obterProcessamentoFinanceiroCota(Integer numeroCota, ProdutoEdicao produtoEdicao, 
    		                                                              Date data,
	                                                                      String sortorder, 
	                                                                      String sortname,
	                                                                      int initialResult, 
	                                                                      int maxResults);

    /**
	 * Obtem Movimentos Financeiros da Cota na data que ainda nao foram consolidados
	 * Movimentos de reparte ou encalhe
	 * @param numeroCota
	 * @param dataOperacao
	 * @return List<MovimentoFinanceiroCota>
	 */
    List<MovimentoFinanceiroCota> obterMovimentosFinanceirosCotaConferenciaNaoConsolidados(Integer numeroCota, Date dataOperacao);

    /**
	 * Obtem valor total de Débitos pendentes da Cota
	 * @param numeroCota
	 * @return BigDecimal
	 */
	BigDecimal obterTotalDebitoCota(Integer numeroCota, Date dataOperacao);

	/**
	 * Obtem valor total de Créditos pendentes da Cota
	 * @param numeroCota
	 * @return BigDecimal
	 */
	BigDecimal obterTotalCreditoCota(Integer numeroCota, Date dataOperacao);
	
	/**
     * Obtem saldo das cotas com tipo À Vista na data de operação atual
     * @param numeroCota
     * @param data
     * @return BigDecimal
     */
	BigDecimal obterSaldoCotasAVista(Integer numeroCota, Date data);

	List<MovimentoFinanceiroDTO> obterDetalhesVendaDia(Integer numeroCota,
			Long idConsolildado, List<Long> tiposMovimento, Date data);

	/**
	 * Verifica existência de MovimentoFinanceiroCota Consolidado por id
	 * @param idMovimentoFinanceiroCota
	 * @return boolean
	 */
	boolean isMovimentoFinanceiroCotaConsolidado(Long idMovimentoFinanceiroCota);
	
	/**
	 * Obtém lista de movimento financeiro cota relativo ao id de consolidado.
	 * 
	 * @param idConsolidado
	 * 
	 * @return List - MovimentoFinanceiroCota
	 */
    public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDeConsolidado(final Long idConsolidado);

	public abstract void removeByIdConsolidadoAndGrupos(Long idConsolidado,
			List<String> grupoMovimentoFinaceiros);

    public void removeByCotaAndDataOpAndGrupos(Long idCota, Date dataOperacao, List<String> grupoMovimentoFinaceiros);
    
    void adicionarEmLoteDTO(final List<MovimentoFinanceiroCotaDTO> movimentoFinanceiroCota);

}