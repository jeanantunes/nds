package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}  
 * 
 *
 */
public interface TipoDescontoProdutoRepository extends Repository<TipoDescontoProduto,Long>{

	void incluirDesconto(TipoDescontoProduto tipoDescontoProduto);
	
	List<TipoDescontoProdutoDTO> obterTipoDescontoProduto(ProdutoEdicao produtoEdicao);	
	
}
