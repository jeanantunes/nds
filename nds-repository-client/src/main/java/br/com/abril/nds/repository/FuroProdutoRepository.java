package br.com.abril.nds.repository;

import br.com.abril.nds.model.movimentacao.FuroProduto;

public interface FuroProdutoRepository extends Repository<FuroProduto, Long>{

	FuroProduto obterFuroProdutoPor(Long lancamentoId, Long produtoEdicaoId);
	
}
