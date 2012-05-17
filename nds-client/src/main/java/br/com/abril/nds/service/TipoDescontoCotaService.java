package br.com.abril.nds.service;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}
 * 
 */
public interface TipoDescontoCotaService {

	void incluirDescontoGeral(TipoDescontoCota tipoDescontoCota);
	
	void atualizarDistribuidos(Long desconto);
}
	
