package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Fornecedor;

/**
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}  
 * 
 * @author william.machado
 *
 */
public interface FornecedorRepository extends Repository<Fornecedor, Long> {

	List<Fornecedor> obterFornecedoresAtivos();
	
	List<Fornecedor> obterFornecedores();
	
}
