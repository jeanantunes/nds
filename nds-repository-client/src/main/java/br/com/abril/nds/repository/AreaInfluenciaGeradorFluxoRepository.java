package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.AreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.dto.filtro.FiltroAreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface AreaInfluenciaGeradorFluxoRepository extends Repository<Cota, Long> {

	List<AreaInfluenciaGeradorFluxoDTO> buscarPorAreaInfluencia(FiltroAreaInfluenciaGeradorFluxoDTO filtro);
	
	List<AreaInfluenciaGeradorFluxoDTO> buscarPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro);
	
}
