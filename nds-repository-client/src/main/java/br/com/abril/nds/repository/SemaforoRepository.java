package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.Semaforo;

public interface SemaforoRepository extends Repository<Semaforo, Integer> {

	public Semaforo selectForUpdate(Integer numeroCota);
	
}
