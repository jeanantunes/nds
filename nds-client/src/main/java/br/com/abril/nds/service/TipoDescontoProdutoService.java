package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoProduto}
 * 
 */
public interface TipoDescontoProdutoService {

	void incluirDesconto(TipoDescontoProduto tipoDescontoProduto);
	
	List<TipoDescontoProdutoDTO> obterTipoDescontoProduto(ProdutoEdicao produtoEdicao);
	
	void excluirDesconto(TipoDescontoProduto tipoDescontoProduto);
	
}
	
