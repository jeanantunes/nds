package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Pessoa;

public interface PessoaService {

	List<Pessoa> buscaPorCnpj(String cnpj);
	
}
