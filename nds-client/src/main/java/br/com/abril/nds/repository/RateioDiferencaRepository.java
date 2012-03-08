package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.RateioDiferenca;

public interface RateioDiferencaRepository extends Repository<RateioDiferenca, Long>{

	boolean verificarExistenciaRateioDiferenca(Long idDiferenca);
	
	RateioDiferenca obterRateioDiferencaPorDiferenca(Long idDiferenca);
}
