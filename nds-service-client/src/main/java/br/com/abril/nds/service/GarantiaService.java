package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Garantia;

public interface GarantiaService {

	List<Garantia> obterGarantiasFiador(Long idFiador, Set<Long> idsIgnorar);
	
	Garantia buscarGarantiaPorId(Long idGarantia);
	
	void salvarGarantias(List<Garantia> listaGarantias, Fiador fiador);
	
	void removerGarantias(Set<Long> idsGarantias);
	
	void removerGarantiasPorFiador(Long idFiador);
}