package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.model.integracao.ParametroDistribuidor;
public interface ParametroDistribuidorRepository extends Repository<ParametroDistribuidor, Long> {

	ParametroDistribuidor findByCodigoDinapFC(String codigo);

}
