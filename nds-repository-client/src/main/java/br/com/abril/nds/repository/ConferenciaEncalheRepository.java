package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.util.ItemAutoComplete;

public interface ConferenciaEncalheRepository extends Repository<ConferenciaEncalhe, Long> { 
	
	/**
	 * Obtém a quantidade de itens da conferencia de encalhe.
	 * 
	 * @param idConferenciaEncalhe
	 * @return
	 */
	public BigInteger obterQtdeEncalhe(Long idConferenciaEncalhe);
	
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
	 * 
	 * @return List- ConferenciaEncalheDTO
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota);

	
	/**
	 * Obtém lista de conferenciaEncalhe em contingência.
	 * 
	 * @param numeroCota
	 * @param dataRecolhimento
	 * @param indFechado
	 * @param indPostergado
	 * @param listaIdProdutoEdicao
	 * 
	 * @return List - ConferenciaEncalheDTO
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTOContingencia(
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
	 * Obtem dados relativos a um slip de produto edicao com chamada encalhe para
	 * a cota e data operação em questão porém ausentes na conferencia de encalhe.
	 * 
	 * @param idCota
	 * @param dataOperacao
	 * @param indPostergado
	 * @param listaIdProdutoEdicao
	 * 
	 * @return List<ProdutoEdicaoSlipDTO>
	 */
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipProdutoEdicaoAusenteConferenciaEncalhe(
			Long idCota,
			Date dataOperacao,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao);
	


	/**
	 * Obtém a chamada de encalhe 'fechada' relacionada à um movimento financeiro
	 * @param idMovimentoDevolucao
	 * @return ChamadaEncalheCota
	 */
	public ChamadaEncalheCota obterChamadaEncalheDevolucao(Long idMovimentoDevolucao);
	
	public BigInteger obterReparteConferencia(Long idCota, Long idControleConferenciaEncCota, Long produtoEdicaoId);
	
	List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(Integer numeroCota, String codigoBarras);
	
	boolean isParcialNaoFinal(Long idProdutoEdicao);
	
	boolean isCromoParcialNaoFinal(Long idProdutoEdicao);
}