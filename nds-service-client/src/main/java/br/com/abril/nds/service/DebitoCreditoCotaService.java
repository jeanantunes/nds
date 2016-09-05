package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;

public interface DebitoCreditoCotaService {

	/**
	 * Obtém uma lista de valores financeiros relativos a cota 
	 * cuja data de criação dos mesmos equivale a data informada 
	 * por parâmetro contanto que não tenham entrado no calculo 
	 * da cobrança da cota na data em questão.
	 * 
	 * @param cota
	 * @param dataOperacao
	 * 
	 * @return List - DebitoCreditoDTO
	 */
	List<DebitoCreditoCota> obterListaResumoCobranca(Cota cota, Date dataOperacao);
	
	MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(DebitoCreditoDTO debitoCreditoDTO);
	
	/**
	 * Obtém dados pré-configurados com informações das Cotas do Box, Rota e Roteiro. Para lançamentos de débitos e/ou créditos
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return List<DebitoCreditoDTO>
	 */
	List<DebitoCreditoDTO> obterDadosLancamentoPorBoxRoteiroRota(Long idBox,Long idRoteiro,Long idRota,BigDecimal percentual,BaseCalculo baseCalculo,Date dataPeriodoInicial,Date dataPeriodoFinal);
	
	/**
	 * Obtém Quantidade de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return Número de Cotas encontradas
	 */
	int obterQuantidadeCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota);
	
	/**
	 * Obtém dados pré-configurados com informações da Cota para lançamento de débito e/ou crédito
	 * @param numeroCota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return DebitoCreditoDTO
	 */
	DebitoCreditoDTO obterDadosLancamentoPorCota(Integer numeroCota,BigDecimal percentual,BaseCalculo baseCalculo,Date dataPeriodoInicial,Date dataPeriodoFinal,Long index);

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
	 * Obtem lista de Débitos e Créditos quem não pertencem à reparte ou encalhe
	 * @param cota
	 * @param datas
	 * @param idFornecedor
	 * @return List<DebitoCreditoCotaDTO>
	 */
	List<DebitoCreditoCota> obterListaDebitoCreditoCotaDTO(Cota cota,
			List<Date> datas, Long idFornecedor);

	/**
	 * Obtem Outros Valores
	 * 
	 * @param infoConfereciaEncalheCota
	 * @param cota
	 * @param datas
	 */
	void carregarDadosDebitoCreditoDaCota(
			InfoConferenciaEncalheCota infoConfereciaEncalheCota, Cota cota,
			List<Date> datas);

	/**
	 * Verifica se o Movimento Financeiro pode ser Editado
	 * Nao consolidado
	 * Lancamento automatico
	 * Data do movimento maior que a data de operação
	 * 
	 * @param movimentoFinanceiroCota
	 * @return boolean
	 */
	boolean isMovimentoEditavel(MovimentoFinanceiroCota movimentoFinanceiroCota);
	
	/**
	 * Processa os movimentos de debito para cota referente as entregas (Transportador/Entregador) de mercadoria.
	 * @param dataOperacao TODO
	 */
	void processarDebitoDeDistribuicaoDeEntregaDaCota(Date dataOperacao);
	
	MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroBoletoAvulsoDTO(BoletoAvulsoDTO debitoCredito);
}
