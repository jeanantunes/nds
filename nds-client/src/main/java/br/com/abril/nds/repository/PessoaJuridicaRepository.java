package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaJuridicaRepository extends Repository<PessoaJuridica, Long> {	
	
	PessoaJuridica buscarPorCnpj(String cnpj);
	PessoaJuridica buscarCnpjPorFornecedor(String nomeFantasia);
	PessoaJuridica buscarPorId(Long id);

}
