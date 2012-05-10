package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Motorista;

public interface MotoristaRepository extends Repository<Motorista, Long> {

	List<Motorista> buscarMotoristasPorTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder);
	
	void removerPorId(Long idMotorista);

	void removerMotoristas(Long idTransportador, Set<Long> listaMotoristasRemover);
}