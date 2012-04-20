package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaService {

	List<PessoaJuridica> buscarPorCnpj(String cnpj);

	void salvarPessoa(Pessoa pessoa);
	
	List<PessoaFisica> obterSociosPorFiador(Long idFiador, Set<Long> idsIgnorar);
	
	PessoaFisica buscarPessoaFisicaPorId(Long idPessoa);

	PessoaFisica buscarPessoaPorCPF(String cpf, boolean isFiador, String cpfConjuge);

	PessoaJuridica buscarPessoaPorCNPJ(String cnpj);
}
