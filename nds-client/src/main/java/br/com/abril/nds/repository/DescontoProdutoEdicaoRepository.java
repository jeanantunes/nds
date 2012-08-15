package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */
public interface DescontoProdutoEdicaoRepository extends Repository<DescontoProdutoEdicao, Long>{

	List<DescontoProdutoEdicao> buscarDescontoProdutoEdicaoNotInTipoDesconto(
			TipoDesconto tipoDesconto, Fornecedor fornecedor);

	List<DescontoProdutoEdicao> buscarDescontoProdutoEdicaoNotInTipoDesconto(
			TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota);
	
	/**
	 * Busca o desconto de um produto edição.
	 * 
	 * @param fornecedor - fornecedor
	 * @param cota - cota
	 * @param produto - produto
	 * 
	 * @return {@link DescontoProdutoEdicao}
	 */
	DescontoProdutoEdicao buscarDescontoProdutoEdicao(Fornecedor fornecedor, Cota cota, ProdutoEdicao produto);
	
	/**
	 * Obtém desconto de produto edição de um fornecedor.
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return {@link Set} de {@link DescontoProdutoEdicao}
	 */
	Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor);
}
