package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.model.estoque.VendaProduto;

public interface VendaProdutoEncalheRepository extends Repository<VendaProduto, Long> {
	
	/**
	 * Retorna a quantidade de vendas de encalhe em função dos parâmetros informados no FiltroVendaEncalheDTO
	 * @param filtro - filtro com as opções de consulta
	 * @return Long
	 */
	Long buscarQntVendaEncalhe(FiltroVendaEncalheDTO filtro);
	
	/**
	 *  Retorna as vendas de encalhe em função dos parâmetros informados no FiltroVendaEncalheDTO
	 * @param filtro - filtro com as opções de consulta
	 * @return List<VendaEncalheDTO>
	 */
	List<VendaEncalheDTO> buscarVendasEncalheDTO(FiltroVendaEncalheDTO filtro);
	
	/**
	 * Retorna uma  vendas de encalhe em função do parâmetro informado
	 * @param idVendaProduto - identificador da venda de produto/encalhe
	 * @return VendaEncalheDTO
	 */
	VendaEncalheDTO buscarVendaProdutoEncalhe(Long idVendaProduto);
	
	/**
	 * Retorna as vendas de encalhe em função dos parâmetros informados no FiltroVendaEncalheDTO
	 * @param filtro
	 * @return  List<VendaProduto>
	 */
	List<VendaProduto> buscarVendasEncalhe(FiltroVendaEncalheDTO filtro);
	
	List<VendaProduto> buscarCotaPeriodoVenda(FiltroVendaEncalheDTO filtro);
}
