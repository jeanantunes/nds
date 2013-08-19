package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.PessoaFisica;

/**
 * Serviço responsável pela lógica da entidade 
 * {@link br.com.abril.nds.model.cadastro.PessoaFisica}
 * 
 * @author Discover Technology
 *
 */
public interface PessoaFisicaService {

	/**
	 * Método que realiza o cadastro de uma pessoa física.
	 * 
	 * @param pessoaFisica - Pessoa a cadastrar.
	 * 
	 * @return PessoaFisica cadastrada.
	 */
	PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica);
	
	/**
	 * Método que realiza uma busca de pessoa física através de seu CPF.
	 * 
	 * @param cpf - CPF da pessoa física.
	 * 
	 * @return PessoaFisica encontrada.
	 */
	PessoaFisica buscarPorCpf(String cpf);
}
