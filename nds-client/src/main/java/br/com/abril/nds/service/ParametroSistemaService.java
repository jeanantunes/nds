package br.com.abril.nds.service;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;

public interface ParametroSistemaService {

	ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema pathImagensPdv);

	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return  
	 */
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral();
	
}
