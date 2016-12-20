package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.client.vo.ProcessamentoFinanceiroCotaVO;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;

public interface MovimentoFinanceiroCotaService {
	
	/**
	 * Gera Movimentos Financeiro para a Cota
	 * @param movimentoFinanceiroCotaDTO
	 * @param movimentosEstoqueCota
	 * @return MovimentoFinanceiroCota
	 */
	List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO);
	
	/**
	 * Gera Movimento Financeiro para a Cota
	 * @param movimentoFinanceiroCotaDTO
	 * @param movimentosEstoqueCota
	 * @return MovimentoFinanceiroCota
	 */
	MovimentoFinanceiroCota gerarMovimentoFinanceiroCota(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO,
			                                             List<MovimentoEstoqueCota> movimentosEstoqueCota);
	
	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCotaUsuario(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	void removerMovimentoFinanceiroCota(Long idMovimento);
	
	MovimentoFinanceiroCota obterMovimentoFinanceiroCotaPorId(Long idMovimento);
	
	BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	void processarRegistrohistoricoFinanceiro(
			HistoricoFinanceiroInput vendaInput, Date dataOperacao);

	boolean existeOutrosMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCredito, Long idMovimentoFinanceiroAtual);

	void removerPostergadosDia(Long idCota,
			List<TipoMovimentoFinanceiro> tiposMovimentoPostergado, Date dataOperacao);
	
	List<GrupoMovimentoFinaceiro> getGrupoMovimentosFinanceirosDebitosCreditos();
	
	/**
	 * Remove movimentos financeiros do consolidado ou postergado
	 * Referentes à encalhe ou reparte da cota
	 * @param mfcs
	 */
	void removerMovimentosFinanceirosCota(List<MovimentoFinanceiroCota> mfcs);
	
	/**
	 * Obtém valores dos faturamentos bruto ou liquido das cotas no período
	 * @param cotas
	 * @param baseCalculo
	 * @param dataInicial
	 * @param dataFinal
	 * @return Map<Long,BigDecimal>: Faturamentos das cotas
	 */
	Map<Long,BigDecimal> obterFaturamentoCotasPeriodo(List<Cota> cotas, BaseCalculo baseCalculo, Date dataInicial, Date dataFinal);

	/**
	 * Gera Financeiro para Movimentos de Estoque da Cota à Vista referentes à Envio de Reparte.
	 * @param cota
	 * @param fornecedor
	 * @param movimentosEstoqueCotaOperacaoEnvioReparte
	 * @param movimentosEstoqueCotaOperacaoEstorno
	 * @param dataOperacao
	 * @param usuario
	 */
	void gerarMovimentoFinanceiroCotaAVista(Cota cota,
											Fornecedor fornecedor,
											List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEnvioReparte,
											List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno,
											Date dataOperacao, Usuario usuario);



	/**
	 * Gera Movimentos Financeiros das Cotas
	 * @param cotas
	 * @param dataOperacao
	 * @param usuario
	 */
	void gerarMovimentoFinanceiroCota(List<Cota> cotas,
	                                  Date dataOperacao, 
	                                  Usuario usuario);
	
	/**
	 * Gera Movimentos Financeiros da Cota
	 * @param cota
	 * @param dataOperacao
	 * @param usuario
	 */
	void gerarMovimentoFinanceiroCota(Cota cota,
			                          Date dataOperacao, 
			                          Usuario usuario);
	
	/**
	 * Gera movimento financeiro para cota na Conferencia de Encalhe
	 * @param idControleConferenciaEncalheCota
	 */
	void gerarMovimentoFinanceiroCota(Cota cota,
									  List<Date> datas,
									  Usuario usuario,
									  Long idControleConferenciaEncalheCota,
									  Integer diasPostergacao);
	
	/**
	 * Obtem Quantidade de Informações para o processamento financeiro (Geração de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
	 * @param numeroCota
	 * @param data
	 * @return int
	 */
	int obterQuantidadeProcessamentoFinanceiroCota(Integer numeroCota, ProdutoEdicao produtoEdicao);
	
	/**
	 * Obtem Informações para o processamento financeiro (Geração de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
	 * @param numeroCota
	 * @param data
	 * @param sortorder
	 * @param sortname
	 * @param initialResult
	 * @param maxResults
	 * @return List<ProcessamentoFinanceiroCotaVO>
	 */
	List<ProcessamentoFinanceiroCotaVO> obterProcessamentoFinanceiroCota(Integer numeroCota,
																		 ProdutoEdicao produtoEdicao,
	                                                                     Date data, 
	                                                                     String sortorder, 
	                                                                     String sortname,
	                                                                     int initialResult, 
	                                                                     int maxResults);

	/**
	 * Remove Movimentos Financeiros da Cota referentes a Conferencia na Data nao Consolidados
	 * @param numeroCota
	 * @param dataOperacao
	 */
	void removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(Integer numeroCota, Date dataOperacao);

	public abstract void removerMovimentosFinanceirosCota(final Long idConsolidado);

    void removerMovimentosFinanceirosCotaPorDataCota(Date dataOperacao, Long idCota);
    
    /**
     * Retorna o faturamento da cota em um determinado periodo
     * @param idCota
     * @param baseCalculo
     * @param dataInicial
     * @param dataFinal
     * @return BigDecimal - faturamento da cota
     */
    BigDecimal obterFaturamentoDaCotaNoPeriodo(final Long idCota,final BaseCalculo baseCalculo,
            final Date dataInicial, final Date dataFinal);
    
    /**
     * Gera movimento de Débito para cota referentes as distribuições(entrega) de produtos.
     * @param tipoMovimento
     * @param usuario
     * @param cota
     * @param dataVencimento
     * @param dataOperacao
     * @param valorDebito
     * @param observacaoMovimento TODO
     */
    void gerarMovimentoFinanceiroDebitoCota(final TipoMovimentoFinanceiro tipoMovimento,
								  		  final Usuario usuario,final Cota cota,
								  		  final Date dataVencimento,
								  		  final Date dataOperacao,
								  		  final BigDecimal valorDebito, String observacaoMovimento);
    
    void processarCreditosParaCotasNoProcessoDeFuroDeProdutoContaFirme(final Long idLancamento, 
															 		   final Long idUsuario);
    
    void processarDebitosParaCotasNoProcessoDeExpedicaoDeProdutoContaFirme(final ExpedicaoDTO expedicaoDTO, 
			  															   final List<MovimentoEstoqueCotaDTO>movimentosEstoqueCota);
    
    List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCreditoCobrancaGerada(final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO);

}