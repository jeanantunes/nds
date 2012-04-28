package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Motorista;

public interface MotoristaRepository extends Repository<Motorista, Long> {

	void removerPorId(Long idMotorista);
}