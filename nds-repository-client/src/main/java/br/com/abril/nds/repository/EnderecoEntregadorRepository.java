package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.EnderecoEntregador;

public interface EnderecoEntregadorRepository extends Repository<EnderecoEntregador, Long> {

	void removerEnderecosEntregadorPorIdEntregador(Long idEntregador);
	
}
