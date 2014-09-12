package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.model.estoque.TipoEstoque;

public interface EstoqueProdutoRespository extends Repository<EstoqueProduto, Long> {

	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);
	
	EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao);
	
	List<EstoqueProdutoDTO> buscarEstoquesProdutos();

	Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro);

	List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro);

	List<Date> obterDatasRecProdutosFechados();

	BigInteger buscarQtdEstoquePorProduto(String codigoProduto, List<Long> numeroEdicao);
	
	BigInteger buscarQtdEstoqueProdutoEdicao(Long idProdutoEdicao);
	
	public Long selectForUpdate(Long idProdutoEdicao);
	
	EstoqueProduto obterEstoqueProdutoParaAtualizar(Long idProdutoEdicao);
	
	void atualizarEstoqueProduto(Long idProdutoEdicao, TipoEstoque tipoEstoque, BigInteger qtde);
	
	BigInteger buscarQtdeEstoqueParaTransferenciaParcial(Long idProdutoEdicao);
	
	BigDecimal obterValorTotalSuplementar();
	
}
