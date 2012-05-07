package br.com.abril.nds.repository;

import java.util.Set;

import br.com.abril.nds.model.cadastro.EnderecoTransportador;

public interface EnderecoTransportadorRepository extends Repository<EnderecoTransportador, Long> {

	EnderecoTransportador buscarEnderecoPorEnderecoTransportador(Long idEndereco, Long idTransportador);

	void removerEnderecosTransportador(Set<Long> listaEnderecosRemover);
}