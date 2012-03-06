package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;

public interface ParametroSistemaRepository extends Repository<ParametroSistema, Long>{

	ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema);
}
