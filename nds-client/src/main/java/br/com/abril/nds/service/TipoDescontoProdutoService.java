package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoProduto}
 * 
 */
public interface TipoDescontoProdutoService {

	void incluirDesconto(TipoDescontoProduto tipoDescontoProduto);
	
	List<TipoDescontoCotaVO> obterTipoDescontoCota(EspecificacaoDesconto especificacaoDesconto);
	
	void excluirDesconto(TipoDescontoCota tipoDescontoCota);
	
	TipoDescontoCota obterTipoDescontoCotaPorId(long idDesconto);
	
}
	
