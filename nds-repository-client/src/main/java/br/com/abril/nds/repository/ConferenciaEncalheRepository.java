package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ComposicaoCobrancaSlipDTO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

public interface ConferenciaEncalheRepository extends Repository<ConferenciaEncalhe, Long> { 
	
	/**
	 * Obtem uma lista com numeroCota e razao social das cotas que
	 * possuem conferencia de encalhe salvo mas não finalizado na 
	 * data pesquisada.
	 * 
	 * @param dataOperacao
	 * 
	 * @return List - CotaDTO
	 */
	public List<CotaDTO> obterListaCotaConferenciaNaoFinalizada(Date dataOperacao);
	
	/**
	 * Obtém uma lista de conferenciaEncalheDTO específicos de uma cota
	 * e relacionados a um registro de ControleConferenciaEncalheCota.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @param idDistribuidor
	 * 
	 * @return List- ConferenciaEncalheDTO
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota, Long idDistribuidor);

	
	/**
	 * Obtém lista de conferenciaEncalhe em contingência.
	 * 
	 * @param idDistribuidor
	 * @param numeroCota
	 * @param dataRecolhimento
	 * @param indFechado
	 * @param indPostergado
	 * @param listaIdProdutoEdicao
	 * 
	 * @return List - ConferenciaEncalheDTO
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTOContingencia(
			Long idDistribuidor,
			Integer numeroCota,
			Date dataRecolhimento,
			boolean indFechado,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao);
	
	/**
	 * Obtém o valorTotal de uma operação de conferência de encalhe. Para o calculo do valor
	 * é levado em conta o preco com desconto de acordo com a regra de comissão que verifica 
	 * desconto no níveis de produtoedicao, cota.
	 * 
	 * @param idControleConferenciaEncalhe
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterValorTotalEncalheOperacaoConferenciaEncalhe(Long idControleConferenciaEncalhe);

	/**
	 * Obtem dados relativos a uma slip de acordo com ControleConferenciaEncalheCota 
	 * a que estes pertencem.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * 
	 * @return List - ProdutoEdicaoSlipDTO
	 */
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipConferenciaEncalhe(Long idControleConferenciaEncalheCota);

    /**
     * Obtém composição de cobrança da cota na data de operação para a exibição no Slip
     * @param numeroCota
     * @param dataOperacao
     * @param tiposMovimentoFinanceiroIgnorados
     * @return List<ComposicaoCobrancaSlipDTO>
     */
	public List<ComposicaoCobrancaSlipDTO> obterComposicaoCobrancaSlip(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados);


	/**
	 * Obtém a chamada de encalhe 'fechada' relacionada à um movimento financeiro
	 * @param idMovimentoDevolucao
	 * @return ChamadaEncalheCota
	 */
	public ChamadaEncalheCota obterChamadaEncalheDevolucao(Long idMovimentoDevolucao);
	
	public BigInteger obterReparteConferencia(Long idCota, Long idControleConferenciaEncCota, Long produtoEdicaoId);
}