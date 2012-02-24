package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.NotaFiscal;

/**
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}  
 * 
 * @author william.machado
 *
 */
public interface NotaFiscalRepository extends Repository<NotaFiscal, Long> {

	void inserirNotaFiscal(NotaFiscal notaFiscal);
	
}
