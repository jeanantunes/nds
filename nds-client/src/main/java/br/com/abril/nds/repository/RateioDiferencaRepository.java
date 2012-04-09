package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.RateioDiferenca;

public interface RateioDiferencaRepository extends Repository<RateioDiferenca, Long>{
	
	RateioDiferenca obterRateioDiferencaPorDiferenca(Long idDiferenca);
	
	void removerRateioDiferencaPorDiferenca(Long idDiferenca);
}
