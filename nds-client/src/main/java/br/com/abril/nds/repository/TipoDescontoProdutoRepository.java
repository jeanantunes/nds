package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}  
 * 
 *
 */
public interface TipoDescontoProdutoRepository extends Repository<TipoDescontoProduto,Long>{

	int obterSequencial();
	
	List<TipoDescontoCota> obterTipoDescontosCotas(EspecificacaoDesconto especificacaoDesconto);
	
}
