package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do produto
 * 
 * @author Discover Technology
 */
public interface DescontoProdutoRepository extends Repository<DescontoProduto, Long>{

	/**
	 * Método que retorna uma coleção de dados referentes aos tipos de desconto cadastrados
	 * para determinados produtos.
	 * 
	 * @param filtro - Filtro que será utilizado na pesquisa.
	 * 
	 * @return List<TipoDescontoProdutoDTO> - tipos de desconto por produto, baseado no filtro.
	 */
	List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro);
	
	/**
	 * Método que retorna a contagem de dados referentes aos tipos de desconto cadastrados
	 * para determinados produtos.
	 * 
	 * @param filtro - Filtro que será utilizado na pesquisa.
	 * 
	 * @return Integer - Contagem dos dados referentes ao tipo de desconto por produto.
	 */
	Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro);
}
