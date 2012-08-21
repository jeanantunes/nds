package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaJuridicaRepository extends Repository<PessoaJuridica, Long> {	
	
	PessoaJuridica buscarPorCnpj(String cnpj);
	PessoaJuridica buscarCnpjPorFornecedor(String nomeFantasia);
	PessoaJuridica buscarPorId(Long id);
	
	/**
	 * Verifica a existencia de pessoa juridica com inscricao estadual e diferente do id pessoa.
	 * @param inscricaoEstadual
	 * @param idPessoa (Opcional) 
	 * @return
	 */
	public abstract boolean hasInscricaoEstadual(String inscricaoEstadual, Long idPessoa);
	/**
	 * Verifica a existencia de pessoa juridica com cnpj e diferente do id pessoa.
	 * @param cnpj
	 * @param idPessoa (Opcional) 
	 * @return
	 */
	public abstract boolean hasCnpj(String cnpj, Long idPessoa);

}
