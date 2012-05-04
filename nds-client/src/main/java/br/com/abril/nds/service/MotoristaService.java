package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Motorista;

public interface MotoristaService {

	List<Motorista> buscarMotoristas();
	
	Motorista buscarMotoristaPorId(Long idMotorista);
	
	void cadastarMotorista(Motorista motorista);
	
	void excluirMotorista(Long idMotorista);
}