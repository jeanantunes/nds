package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface TipoClassificacaoProdutoRepository extends Repository<TipoClassificacaoProduto, Long> {
	
	public List<TipoClassificacaoProduto> obterTodos() ;

    public TipoClassificacaoProduto obterPorClassificacao(String classificacaoProduto);
    
    public Boolean validarClassificacao(String classificacaoProduto);
}
