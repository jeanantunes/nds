package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface TipoClassificacaoProdutoService {

	List<TipoClassificacaoProduto> obterTodos();
	
	TipoClassificacaoProduto buscarPorId(Long id);

    TipoClassificacaoProduto obterPorClassificacao(String classificacao);
    
    Boolean validarClassificacao(String classificacao);
}
