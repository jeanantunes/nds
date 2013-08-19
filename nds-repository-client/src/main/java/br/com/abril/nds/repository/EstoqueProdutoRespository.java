package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.EstoqueProduto;

public interface EstoqueProdutoRespository extends Repository<EstoqueProduto, Long> {

	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);
	
	EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao);

}
