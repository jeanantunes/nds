package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;

public interface EstoqueProdutoCotaJuramentoService {

	EstoqueProdutoCotaJuramentado obterEstoqueProdutoJuramentoPorProdutoEdicao(ProdutoEdicao produtoEdicao);
	
}
