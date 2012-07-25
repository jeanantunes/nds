package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}
 * 
 */
public interface TipoDescontoCotaService {

	void incluirDesconto(TipoDescontoCota tipoDescontoCota);	
	
	void excluirDesconto(TipoDescontoCota tipoDescontoCota);
	
	Integer buscarTotalDescontosPorCota();
	
	List<TipoDescontoCotaDTO> obterTipoDescontosCota();
	
}
	
