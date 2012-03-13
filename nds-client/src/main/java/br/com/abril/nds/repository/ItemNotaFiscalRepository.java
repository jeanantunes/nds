package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.ItemNotaFiscal;


public interface ItemNotaFiscalRepository extends Repository<ItemNotaFiscal, Long> {
	
	List<ItemNotaFiscal> buscarItensPorIdNota(Long idNotaFiscal);
	
}
