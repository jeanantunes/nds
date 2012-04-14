package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaService {

	List<PessoaJuridica> buscarPorCnpj(String cnpj);
	
	List<PessoaFisica> obterSociosPorFiador(Long idFiador);
	
	PessoaFisica buscarPessoaFisicaPorId(Long idPessoa);

	PessoaFisica buscarPessoaPorCPF(String cpf, boolean isFiador, String cpfConjuge);

	PessoaJuridica buscarPessoaPorCNPJ(String cnpj);
}
