package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;

public interface ParametroSistemaRepository extends Repository<ParametroSistema, Long>{

	ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema);
	
	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return Lista dos parâmetros do sistema que são considerados gerais. 
	 */
	public List<ParametroSistema> buscarParametroSistemaGeral();
	
}
