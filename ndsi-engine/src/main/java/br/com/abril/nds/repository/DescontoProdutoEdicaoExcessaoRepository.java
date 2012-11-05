package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicaoExcessao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */
public interface DescontoProdutoEdicaoExcessaoRepository extends Repository<DescontoProdutoEdicaoExcessao, Long>{

	/**
	 * Obtém descontos de produto edição diferentes do tipo de desconto informado.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return {@link List} {@link DescontoProdutoEdicaoExcessao}
	 */
	List<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor);
	
	/**
	 * Obtém descontos de produto edição diferentes do tipo de desconto informado.
	 * 
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @return {@link List} {@link DescontoProdutoEdicaoExcessao}
	 */
	List<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota);
	
	/**
	 * Obtém o desconto de um produto edição.
	 * 
	 * @param fornecedor - fornecedor
	 * @param cota - cota
	 * @param produto - produto
	 * 
	 * @return {@link DescontoProdutoEdicaoExcessao}
	 */
	DescontoProdutoEdicaoExcessao buscarDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota, ProdutoEdicao produto);

	/**
	 * Obtém descontos de produtos edição de um fornecedor.
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return {@link Set} de {@link DescontoProdutoEdicaoExcessao}
	 */
	Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor);
	
	/**
	 * Obtém descontos de produtos edição de um fornecedor e uma cota.
	 * 
	 * @param fornecedor - fornecedor
	 * @param cota - cota
	 * 
	 * @return {@link Set} de {@link DescontoProdutoEdicaoExcessao}
	 */
	Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota);
	
	/**
	 * Obtém descontos de produtos edição de uma cota.
	 * 
	 * @param cota - cota
	 * 
	 * @return {@link Set} de {@link DescontoProdutoEdicaoExcessao}
	 */
	Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(Cota cota);
	
	/**
	 * Obtém descontos do produto edição.
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link Set} de {@link DescontoProdutoEdicaoExcessao}
	 */
	Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao);
	
	/**
	 * 
	 * Obtém descontos de produtos edição de fornecedor, cota e tipo de desconto.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @return {@link Set} {@link DescontoProdutoEdicaoExcessao}
	 */
	Set<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota);
	
	/**
	 * 
	 * Obtém descontos de produtos edição de fornecedor, cota e tipo de desconto.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link Set} {@link DescontoProdutoEdicaoExcessao}
	 */
	Set<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota,ProdutoEdicao produtoEdicao);
	
	/**
     * Recupera o percentual de desconto a ser aplicado para o produto edição de acordo com a
     * cota, edição do produto e fornecedor 
     * 
     * 
     * @param idCota identificador da cota para recuperação do desconto
     * @param idProdutoEdicao identificador do produto edição para recuperação do desconto
     * @param idFornecedor identificador do fornecedor para recuperação do desconto
     * @return peercentual de desconto a ser utilizado
     */
	BigDecimal obterDescontoPorCotaProdutoEdicao(Long idCota, Long idProdutoEdicao, Long idFornecedor);
	
	/**
	 * Salva uma lista de descontosProdutoEdicao
	 * @param lista
	 */
	public void salvarListaDescontoProdutoEdicaoExcessao(List<DescontoProdutoEdicaoExcessao> lista);
	
}
