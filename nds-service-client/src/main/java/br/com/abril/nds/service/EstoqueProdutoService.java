package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.model.estoque.EstoqueProduto;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.EstoqueProduto}  
 * 
 * @author Discover Technology
 *
 */
public interface EstoqueProdutoService {
	
	/**
	 * Obtém o Estoque Produto pelo id do Produto Edição.
	 * 
	 * @param idProdutoEdicao
	 * @return
	 */
	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);

	
	/**
	 * Obtém os Produtos Edições e sua quantidade disponível no estoque Suplementar do Distribuidor,
	 * a partir dos movimentos de reparte de uma cota realizados na data parametrizada. 
	 * 
	 * @param data - data para busca dos movimentos de reparte
	 * @param idCota - id da cota
	 * @return
	 */
	List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarDisponivel(Date data, Long idCota);

}
