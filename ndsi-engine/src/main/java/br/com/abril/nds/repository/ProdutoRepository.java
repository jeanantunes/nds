package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;

public interface ProdutoRepository extends Repository<Produto, Long> {
	
	/**
	 * Obtém o Grupo ao qual o produto pertence.
	 * 
	 * @param codigoProduto
	 * 
	 * @return GrupoProduto
	 */
	GrupoProduto obterGrupoProduto(String codigoProduto);
	
}
