package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;

/**
 * Interface que define os serviços referentes
 * ao chamadão de publicações.
 * 
 * @author Discover Technology
 */
public interface ChamadaoService {

	/**
	 * Obtém os consignados da cota para realizar o chamadão,
	 * o total de consignados da cota para realizar o chamadão,
	 * o resumo dos consignados da cota para realizar o chamadão.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<ConsignadoCotaChamadaoDTO>}
	 */
	ConsultaChamadaoDTO obterConsignados(FiltroChamadaoDTO filtro);
	
	/**
	 * Confima o chamadão das publicações da cota.
	 * 
	 * @param listaLancamento - lista de ids de lançamento
	 * @param numeroCota - número da cota
	 * @param dataChamadao - data do chamadão
	 * @param chamarTodos - indica se todas as publicações da cota serão recolhidas
	 */
	void confirmarChamacao(List<Long> listaLancamento, Integer numeroCota,
			  		  	   Date dataChamadao, boolean chamarTodos);
	
}
