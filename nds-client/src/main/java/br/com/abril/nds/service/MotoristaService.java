package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Motorista;

public interface MotoristaService {

	List<Motorista> buscarMotoristasPorTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder);
	
	Motorista buscarMotoristaPorId(Long idMotorista);
	
	void cadastarMotorista(Motorista motorista);
	
	void excluirMotorista(Long idMotorista);
}