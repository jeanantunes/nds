package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;

public interface EstoqueProdutoRespository extends Repository<EstoqueProduto, Long> {

	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);
	
	EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao);
	
	List<EstoqueProdutoDTO> buscarEstoquesProdutos();

}
