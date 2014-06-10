package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.dto.filtro.FiltroAreaInfluenciaGeradorFluxoDTO;

public interface AreaInfluenciaGeradorFluxoService {

	List<AreaInfluenciaGeradorFluxoDTO> buscarPorAreaInfluencia(FiltroAreaInfluenciaGeradorFluxoDTO filtro);
	
	List<AreaInfluenciaGeradorFluxoDTO> buscarPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro);
	
}
