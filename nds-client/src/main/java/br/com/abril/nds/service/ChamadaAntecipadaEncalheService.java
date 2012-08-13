package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.InfoChamdaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;

/**
 * Interface de serviços referentes a serviços de chamada antecipada de encalhe. 
 *   
 * 
 * @author Discover Technology
 */
public interface ChamadaAntecipadaEncalheService {
	
	/**
	 * 
	 * Retorna as informações das possíveis cotas que poderão sofrer chamadas antecipadas 
	 * de encalhe de um determinado produto edição
	 * 
	 * @param filtro - filtro com as opções de paginação, ordenação e consulta
	 * 
	 * @return InfoChamdaAntecipadaEncalheDTO
	 */
	InfoChamdaAntecipadaEncalheDTO obterInfoChamdaAntecipadaEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * 
	 * Retorna as informações das possíveis cotas que poderão sofrer chamadas antecipadas 
	 * de encalhe de um determinado produto edição, com a sumarização das informações.
	 * 
	 * @param filtro - filtro com as opções de consulta
	 * 
	 * @return InfoChamdaAntecipadaEncalheDTO
	 */
	InfoChamdaAntecipadaEncalheDTO obterInfoChamdaAntecipadaEncalheSumarizado(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * Retorna a data de recolhimento prevista de encalhe de um produto edição
	 * 
	 * @param codigoProduto - código do produto
	 * @param numeroEdicao - numero da edição do produto
	 * 
	 * @return Date
	 */
	Date obterDataRecolhimentoPrevista(String codigoProduto, Long numeroEdicao);
	
	/**
	 *  Retorna a quantidade efetiva de produtos edição em estoque.
	 * 
	 * @param filtro - filtro com as opções de consulta
	 * 
	 * @return BigDecimal
	 */
	BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * 
	 * Grava as informações da chamada de encalhe antecipada de todas as cotas referente aos 
	 * parâmetros informados no filtro de pesquisa.
	 * 
	 * @param filtro - filtro com os parâmetros informados para consulta de um determinado produto edição
	 */
	void gravarChamadaAntecipacaoEncalheProduto(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * 
	 * Grava as informações da chamada de encalhe antecipada das cotas selecionadas na tela de consulta. 
	 * 
	 * @param infoEncalheDTO - objeto com as informaçõe de cotas e produto edição.
	 */
	void gravarChamadaAntecipacaoEncalheProduto(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO);
	
	/**
	 * Efetua o cancelamento de uma chamada de encalhe antecipada
	 * 
	 * @param infoEncalheDTO - objeto com as informaçõe de cotas e produto edição.
	 */
	void cancelarChamadaAntecipadaCota(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO);
	
	/**
	 * Efetua o cancelamento de uma chamada de encalhe antecipada
	 * 
	 * @param filtro - filtro com os parâmetros informados para consulta de um determinado produto edição
	 */
	void cancelarChamadaAntecipadaCota(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * 
	 * Efetua a reprogramação da chamada de encalhe antecipada de todas as cotas referente aos 
	 * parâmetros informados no filtro de pesquisa.
	 * 
	 * @param filtro - filtro com os parâmetros informados para consulta de um determinado produto edição
	 */
	void reprogramarChamadaAntecipacaoEncalheProduto(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * 
	 * Efetua a reprogramação da chamada de encalhe antecipada das cotas selecionadas na tela de consulta. 
	 * 
	 * @param infoEncalheDTO - objeto com as informaçõe de cotas e produto edição.
	 */
	void reprogramarChamadaAntecipacaoEncalheProduto(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO);
	
	/**
	 * Retorna quantidade de exemplares prevista de uma cota com programação de chamada de encalhe antecipada
	 * 
	 * @param filtro - filtro com os parâmetros informados para consulta de um determinada cota
	 * 
	 * @return BigDecimal
	 */
	BigDecimal obterQntExemplaresComProgramacaoAntecipadaEncalheCota(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * Retorna um objeto com os dados de uma chamada de encalhe antecipada
	 *  
	 * @param filtro - filtro com os parâmetros informados para consulta de uma determinada chamada de encalhe antecipada
	 * 
	 * @return ChamadaAntecipadaEncalheDTO
	 */
	ChamadaAntecipadaEncalheDTO obterChamadaEncalheAntecipada(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	
}
