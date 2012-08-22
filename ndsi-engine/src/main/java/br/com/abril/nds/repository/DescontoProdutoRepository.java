package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

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
	
	/**
	 * Método que retorna uma coleção com as cotas relacionadas ao tipo de desconto especificado.
	 * 
	 * @param idDescontoProduto - ID do Tipo de desconto.
	 * 
	 * @return - List<CotaDescontoProdutoDTO> - As cotas relacionadas.
	 */
	List<CotaDescontoProdutoDTO> obterCotasDoTipoDescontoProduto(Long idDescontoProduto, Ordenacao ordenacao);
    
	/**
	 * Método que retorna uma coleção de dados referentes aos tipos de desconto por produto cadastrados
	 * para determinada cota.
	 * 
	 * @param idCota - ID da Cota.
	 * 
	 * @return - List<TipoDescontoProdutoDTO>.
	 */
	List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota, String sortorder, String sortname);
	
	/**
	 * Retorna o ultimo desconto produto
	 * 
	 * @param idDesconto
	 * 
	 * @param cota
	 * 
	 * @param produtoEdicao
	 * 
	 * @return
	 */
	DescontoProduto buscarUltimoDescontoValido(Long idDesconto, Cota cota,ProdutoEdicao produtoEdicao);
	
	/**
	 * 
	 * Retorna o ultimo desconto produto
	 * 
	 * @param cota - cota 
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return DescontoProduto
	 */
	DescontoProduto buscarUltimoDescontoValido(Cota cota,ProdutoEdicao produtoEdicao);

}
