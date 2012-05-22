package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}
 * 
 */
public interface TipoDescontoCotaService {

	void incluirDesconto(TipoDescontoCota tipoDescontoCota);
	
	void atualizarDistribuidores(BigDecimal desconto);
	
	int obterUltimoSequencial();
	
	List<TipoDescontoCotaVO> obterTipoDescontoCota(EspecificacaoDesconto especificacaoDesconto);
	
	void excluirDesconto(TipoDescontoCota tipoDescontoCota);
	
	TipoDescontoCota obterTipoDescontoCotaPorId(long idDesconto);
	
}
	
