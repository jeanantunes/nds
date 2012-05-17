package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}
 * 
 */
public interface TipoDescontoCotaService {

	void incluirDescontoGeral(TipoDescontoCota tipoDescontoCota);
	
	void atualizarDistribuidores(Long desconto);
	
	List<TipoDescontoCotaVO> obterTipoDescontoGeral();

	Cota obterCota(int numeroCota);
}
	
