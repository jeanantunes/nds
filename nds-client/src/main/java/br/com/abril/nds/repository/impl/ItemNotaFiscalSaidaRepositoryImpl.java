package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;

@Repository
public class ItemNotaFiscalSaidaRepositoryImpl extends
		AbstractRepository<ItemNotaFiscalSaida, Long> implements ItemNotaFiscalSaidaRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalSaidaRepositoryImpl() {
		super(ItemNotaFiscalSaida.class);
	}
	

}
