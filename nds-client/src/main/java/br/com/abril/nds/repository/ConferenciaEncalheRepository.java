package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;

public interface ConferenciaEncalheRepository extends Repository<ConferenciaEncalhe, Long> { 
	
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
	 * @param dataInicial
	 * @param dataFinal
	 * @param indFechado
	 * @param indPostergado
	 * @param listaIdProdutoEdicao
	 * 
	 * @return List - ConferenciaEncalheDTO
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTOContingencia(
			Long idDistribuidor,
			Integer numeroCota,
			Date dataInicial,
			Date dataFinal,
			boolean indFechado,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao);
	
	
	/**
	 * Obtém o valorTotal de uma operação de conferência de encalhe. Para o calculo do valor
	 * é levado em conta o preco com desconto de acordo com a regra de comissão que verifica 
	 * desconto no níveis de produtoedicao, cota e distribuidor.
	 * 
	 * @param idControleConferenciaEncalhe
	 * @param idDistribuidor
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterValorTotalEncalheOperacaoConferenciaEncalhe(Long idControleConferenciaEncalhe, Long idDistribuidor);
	
	/**
	 * Obtem dados relativos a uma slip de acordo com ControleConferenciaEncalheCota 
	 * a que estes pertencem.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @param idDistribuidor
	 * 
	 * @return List - ProdutoEdicaoSlipDTO
	 */
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipConferenciaEncalhe(Long idControleConferenciaEncalheCota, Long idDistribuidor);


}
