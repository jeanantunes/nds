package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.estoque.AtualizacaoEstoqueGFS;

public interface AtualizacaoEstoqueGFSRepository extends Repository<AtualizacaoEstoqueGFS, Long> {

	List<AtualizacaoEstoqueGFS> obterPorProdutoEdicao(Long idProdutoEdicao);
	
}
