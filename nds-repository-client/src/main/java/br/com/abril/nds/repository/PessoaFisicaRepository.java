package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.PessoaFisica;

public interface PessoaFisicaRepository extends Repository<PessoaFisica, Long> {

	/**
	 * Método que realiza uma busca de pessoa física através de seu CPF.
	 * 
	 * @param cpf - CPF da pessoa física.
	 * 
	 * @return PessoaFisica encontrada.
	 */
	PessoaFisica buscarPorCpf(String cpf);
	
}
