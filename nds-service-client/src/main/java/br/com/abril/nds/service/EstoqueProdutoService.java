package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;

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

	Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro);

	List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro);
}
