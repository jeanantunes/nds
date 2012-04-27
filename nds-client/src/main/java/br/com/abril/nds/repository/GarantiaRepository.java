package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Garantia;

public interface GarantiaRepository extends Repository<Garantia, Long> {

	List<Garantia> obterGarantiasFiador(Long idFiador, Set<Long> idsIgnorar);
	
	void removerGarantias(Set<Long> idsGarantias);
	
	void removerGarantiasPorFiador(Long idFiador);
}