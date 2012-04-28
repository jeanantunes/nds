package br.com.abril.nds.service;

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
	 * @param listaChamadao - lista de consignados para o chamadão
	 * @param filtro - filtro para a pesquisa
	 * @param chamarTodos - indica se todas as publicações da cota serão recolhidas
	 * @param usuario - usuário
	 */
	void confirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
						   FiltroChamadaoDTO filtro,
						   boolean chamarTodos, Usuario usuario);
	
}
