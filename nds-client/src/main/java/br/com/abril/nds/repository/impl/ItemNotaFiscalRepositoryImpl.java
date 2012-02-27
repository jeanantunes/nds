package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemNotaFiscalRepositoryImpl extends
		AbstractRepository<ItemNotaFiscal, Long> implements ItemNotaFiscalRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalRepositoryImpl() {
		super(ItemNotaFiscal.class);
	}

}
