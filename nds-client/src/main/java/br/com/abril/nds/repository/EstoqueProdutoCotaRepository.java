package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
	 * Obtém o valor total de reparte para cota especifica 
	 * O valor do produto utilizado no calculo é o precoVenda subtraido
	 * do desconto. 
	 * 
	 * Sendo que o valor de desconto utilizado pode ser o 
	 * parametrizado na entidade ProdutoEdicao (campo desconto), Cota (campo fatorDesconto) ou 
	 * Distribuidor (campo fatorDesconto), sendo utilizado o valor que for encontrado primeiro
	 * nesta ordem.
	 * 
	 * @param numeroCota
	 * @param listaIdProdutoEdicao
	 * @param idDistribuidor
	 * 
	 * @return BigDecimal
	 */
	BigDecimal obterValorTotalReparteCota(
			Integer numeroCota, 
			List<Long> listaIdProdutoEdicao, 
			Long idDistribuidor);
	

	/**
	 * Obtém uma lista de estoque de produto da cota de acordo o parâmetro informado.
	 * 
	 * @param idsLancamento - identificadores de lancamento
	 * 
	 * @return {@link List<EstoqueProdutoCota>}
	 */
	List<EstoqueProdutoCota> buscarListaEstoqueProdutoCota(Set<Long> idsLancamento);

	BigDecimal obterConsignado(boolean cotaInadimplente);
}
