package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaService {

	void salvarPessoa(Pessoa pessoa);
	
	List<PessoaFisica> obterSociosPorFiador(Long idFiador, Set<Long> idsIgnorar, Set<String> cpfsIgnorar);
	
	PessoaFisica buscarPessoaFisicaPorId(Long idPessoa);
	
	Pessoa buscarPessoaPorId(Long idPessoa);

	PessoaFisica buscarPessoaPorCPF(String cpf, boolean isFiador, String cpfConjuge);

	PessoaJuridica buscarPessoaPorCNPJ(String cnpj);
	
	List<Pessoa> obterPessoasPorNome(String nomePessoa);
	
	List<Pessoa> obterPessoasPorNome(String nomePessoa, Integer qtdMaxResult);
	
	void validarCPF(String cpf);
	
	void validarCNPJ(String cnpj);
}
