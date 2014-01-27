package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;

public interface EstoqueProdutoRespository extends Repository<EstoqueProduto, Long> {

	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);
	
	EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao);
	
	List<EstoqueProdutoDTO> buscarEstoquesProdutos();

	Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro);

	List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro);

	List<Date> obterDatasRecProdutosFechados();

	BigInteger buscarQtdEstoquePorProduto(String codigoProduto, Long numeroEdicao);
}
