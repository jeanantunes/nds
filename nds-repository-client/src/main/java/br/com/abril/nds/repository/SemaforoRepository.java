package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.estoque.Semaforo;

public interface SemaforoRepository extends Repository<Semaforo, Integer> {

	Semaforo selectForUpdate(Integer numeroCota);
	
	List<Semaforo> obterSemaforosAtualizadosEm(Date data);

	void atualizarStatusProcessoEncalheIniciadoEm(Date data);
	
}