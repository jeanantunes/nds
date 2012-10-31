package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.integracao.model.ParametroSistema;

public interface ParametroSistemaRepository extends Repository<ParametroSistema, Long> {

	String getParametro(String string);

}
