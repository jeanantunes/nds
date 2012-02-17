package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface ProdutoEdicaoRepository extends Repository<ProdutoEdicao, Long> {

	List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto);
}
