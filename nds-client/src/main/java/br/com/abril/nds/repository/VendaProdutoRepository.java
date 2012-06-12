package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;

public interface VendaProdutoRepository extends Repository<MovimentoEstoque, Long>{

	List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro);
	
	List<LancamentoPorEdicaoDTO> buscarLancamentoPorEdicao(FiltroVendaProdutoDTO filtro);
	
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
	List<VendaEncalheDTO> buscarVendasEncalhe(FiltroVendaEncalheDTO filtro);
	
	/**
	 * Retorna uma  vendas de encalhe em função do parâmetro informado
	 * @param idVendaProduto - identificador da venda de produto/encalhe
	 * @return VendaEncalheDTO
	 */
	VendaEncalheDTO buscarVendaProdutoEncalhe(Long idVendaProduto);

}
