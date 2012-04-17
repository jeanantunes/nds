package br.com.abril.nds.repository;

import java.util.Collection;

import br.com.abril.nds.model.cadastro.Endereco;

/**
 * Interface que define as regras para repositório referente a entidade
 * {@link br.com.abril.nds.model.cadastro.Endereco}
 * 
 * @author Discover Technology
 *
 */
public interface EnderecoRepository extends Repository<Endereco, Long> {

	/**
	 * Método responsável por remover os endereços de acordos com seus ids.
	 * 
	 * @param idsEndereco - Coleção com os ids dos endereços
	 */
	void removerEnderecos(Collection<Long> idsEndereco);
}
