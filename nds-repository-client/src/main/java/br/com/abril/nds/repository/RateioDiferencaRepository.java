package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.model.estoque.RateioDiferenca;

public interface RateioDiferencaRepository extends Repository<RateioDiferenca, Long>{
		
	void removerRateioDiferencaPorDiferenca(Long idDiferenca);
	
	List<RateioDiferencaCotaDTO> obterRateioDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro);
	
	DetalheDiferencaCotaDTO obterDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro);
	
	void removerRateiosNaoAssociadosDiferenca( Long idDiferenca, List<Long> idRateios);

	Collection<RateioDiferenca> obterRateiosPorDiferenca(Long id);
}
