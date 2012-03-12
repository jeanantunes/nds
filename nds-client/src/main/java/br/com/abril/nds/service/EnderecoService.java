package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.Endereco;

public interface EnderecoService {

	void removerEndereco(Endereco endereco);

	Endereco salvarEndereco(Endereco endereco);
	
	Endereco obterEnderecoPorId(Long idEndereco);
}
