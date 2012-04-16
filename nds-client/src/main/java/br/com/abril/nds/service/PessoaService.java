package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaService {

	List<PessoaJuridica> buscarPorCnpj(String cnpj);

	void salvarPessoa(Pessoa pessoa);
}
