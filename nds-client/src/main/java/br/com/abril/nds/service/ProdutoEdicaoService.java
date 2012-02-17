package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface ProdutoEdicaoService {

	List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto);
}
