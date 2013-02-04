package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Veiculo;

public interface VeiculoService {

	List<Veiculo> buscarVeiculos();
	
	Veiculo buscarVeiculoPorId(Long idVeiculo);
	
	void cadastarVeiculo(Veiculo veiculo);
	
	void excluirVeiculo(Long idVeiculo);
}