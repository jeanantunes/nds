package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.ItemDTO;
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
	
	List<EstoqueProdutoCota> buscarEstoqueProdutoCotaCompraSuplementar(Long idLancamento);
	
	BigDecimal obterConsignado(boolean cotaInadimplente);

	BigInteger obterTotalEmEstoqueProdutoCota(Long idCota, Long idProdutoEdicao);
	
	List<FixacaoReparteDTO> obterHistoricoEdicaoPorProduto(Produto produto, String classificacaoProduto, Integer numeroCota);
	
	List<FixacaoReparteDTO> obterHistoricoEdicaoPorCota(Cota cota, String codigoProduto, String classificacaoProduto);

	public abstract void updateById(Long id, BigInteger qtdDevolvida);

	public abstract ItemDTO<Long, BigInteger> loadIdAndQtdDevolvidaByIdProdutoEdicaoAndIdCota(
			final Long idProdutoEdicao, final Long idCota);

	BigDecimal obterVendaBaseadoNoEstoque(Long idProdutoEdicao);

	public abstract BigDecimal obterVendaCotaBaseadoNoEstoque(Long idProdEdicao,
			Integer numeroCota);
}
