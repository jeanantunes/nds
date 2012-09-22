package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define os serviços referentes
 * ao chamadão de publicações.
 * 
 * @author Discover Technology
 */
public interface ChamadaoService {

	/**
	 * Obtém os consignados, total de consignados e o resumo dos consignados
	 * da cota para realização do chamadão.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<ConsignadoCotaChamadaoDTO>}
	 */
	ConsultaChamadaoDTO obterConsignados(FiltroChamadaoDTO filtro);
	
	/**
	 * Obtém os consignados, total de consignados e o resumo dos consignados
	 * da cota que já possuem chamadão.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<ConsignadoCotaChamadaoDTO>}
	 */
	ConsultaChamadaoDTO obterConsignadosComChamadao(FiltroChamadaoDTO filtro);
	
	/**
	 * Confima o chamadão das publicações da cota.
	 * 
	 * @param listaChamadao - lista de consignados para o chamadão
	 * @param filtro - filtro para a pesquisa
	 * @param chamarTodos - indica se todas as publicações da cota serão recolhidas
	 * @param usuario - usuário
	 * @param novaDataChamadao - nova data para chamadão
	 */
	void confirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
						   FiltroChamadaoDTO filtro,
						   boolean chamarTodos,
						   Usuario usuario,
						   Date novaDataChamadao);
	
	/**
	 * Cancela um chamadão já realizado anteriormente.
	 * 
	 * @param listaChamadao - lista de consignados para o chamadão
	 * @param filtro - filtro para a pesquisa
	 * @param chamarTodos - indica se todas as publicações da cota serão recolhidas
	 */
	void cancelarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
						  FiltroChamadaoDTO filtro,
						  boolean chamarTodos);
	
}
