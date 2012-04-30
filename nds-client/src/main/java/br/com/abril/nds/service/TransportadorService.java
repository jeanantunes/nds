package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Transportador;

public interface TransportadorService {

	Transportador buscarTransportadorPorId(Long idTransportador);
	
	List<Transportador> buscarTransportadores();
	
	void cadastrarTransportador(Transportador transportador);
	
	void excluirTransportador(Long idTransportador);
}