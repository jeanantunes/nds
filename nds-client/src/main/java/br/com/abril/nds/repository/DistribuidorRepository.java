package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Distribuidor;

public interface DistribuidorRepository extends Repository<Distribuidor, Long> {
	
	Distribuidor obter();

}
