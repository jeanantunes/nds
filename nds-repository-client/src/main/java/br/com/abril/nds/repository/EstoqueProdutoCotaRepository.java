package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;

public interface EstoqueProdutoCotaRepository extends Repository<EstoqueProdutoCota, Long>{

	EstoqueProdutoCota buscarEstoquePorProdutoECota(Long idProdutoEdicao, Long idCota);
	
	EstoqueProdutoCota buscarEstoquePorProdutEdicaoECota(Long idProdutoEdicao, Long idCota);
	
	/**
	 * Retorna a quantidade efetiva de produtos edição em estoque.
	 * 
	 * @param numeroEdicao - número edição
	 * @param codigoProduto - código do propduto
	 * @param numeroCota - número da cota
	 * @return BigDecimal 
	 */
	BigDecimal buscarQuantidadeEstoqueProdutoEdicao(Long numeroEdicao, String codigoProduto ,Integer numeroCota);	

	/**
	 * Obtém uma lista de estoque de produto da cota de acordo o parâmetro informado.
	 * 
	 * @param idLancamento - identificador do lancamento
	 * 
	 * @return {@link List<EstoqueProdutoCota>}
	 */
	List<EstoqueProdutoCota> buscarListaEstoqueProdutoCota(Long idLancamento);

	BigDecimal obterConsignado(boolean cotaInadimplente);

	BigInteger obterTotalEmEstoqueProdutoCota(Long idCota, Long idProdutoEdicao);
	
	List<FixacaoReparteDTO> obterHistoricoEdicaoPorProduto(Produto produto);
	
	List<FixacaoReparteDTO> obterHistoricoEdicaoPorCota(Cota cota, String codigoProduto);
}
