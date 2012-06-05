package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;

public interface DistribuicaoFornecedorRepository extends Repository<DistribuicaoFornecedor, Long>{

	List<Integer> obterDiasSemanaDistribuicao(String codigoProduto, Long idProdutoEdicao);
	
	boolean verificarDistribuicaoDiaSemana(String codigoProduto, Long idProdutoEdicao, DiaSemana diaSemana);
	
	boolean verificarRecolhimentoDiaSemana(String codigoProduto,Long idProdutoEdicao, DiaSemana diaSemana);
	
	List<DistribuicaoFornecedor> obterTodosOrdenadoId();

	void excluirDadosFornecedor(Fornecedor fornecedor);
}
