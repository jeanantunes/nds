package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;


public interface ItemNotaFiscalEntradaRepository extends Repository<ItemNotaFiscalEntrada, Long> {
	
	List<ItemNotaFiscalEntrada> buscarItensPorIdNota(Long idNotaFiscal);
	
}
