package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ParametrosDistribuidorFaltasSobras;

public interface ParametrosDistribuidorFaltasSobrasRepository extends Repository<ParametrosDistribuidorFaltasSobras, Long> {

	public void alterarOuCriar(ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras);
	
}
