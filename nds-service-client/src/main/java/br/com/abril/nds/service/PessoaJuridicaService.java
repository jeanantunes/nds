package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaJuridicaService {

	PessoaJuridica buscarPorCnpj(String cnpj);
	PessoaJuridica buscarCnpjPorFornecedor(String nomeFantasia);
	PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica);
	PessoaJuridica buscarPorId(Long id);

}
