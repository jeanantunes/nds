package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de consignados do chamadão.
 * 
 * @author Discover Technology
 */
public interface ChamadaoRepository extends Repository<Cota,Long> {

	/**
	 * Obtém os consignados para o chamadão
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<ConsignadoCotaChamadaoDTO>}
	 */
	List<ConsignadoCotaChamadaoDTO> obterConsignadosParaChamadao(FiltroChamadaoDTO filtro);
	
	/**
	 * Obtém o total de consignados para o chamadão
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return total de consignados para o chamadão
	 */
	Long obterTotalConsignadosParaChamadao(FiltroChamadaoDTO filtro);
	
	/**
	 * Obtém o resumo dos consignados para o chamadão
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link ResumoConsignadoCotaChamadaoDTO}
	 */
	ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosParaChamadao(FiltroChamadaoDTO filtro);

}
