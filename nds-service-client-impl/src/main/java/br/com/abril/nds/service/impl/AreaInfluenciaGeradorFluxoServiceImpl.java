package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.dto.filtro.FiltroAreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.AreaInfluenciaGeradorFluxoRepository;
import br.com.abril.nds.service.AreaInfluenciaGeradorFluxoService;

@Service
public class AreaInfluenciaGeradorFluxoServiceImpl implements AreaInfluenciaGeradorFluxoService {

	@Autowired
	private AreaInfluenciaGeradorFluxoRepository areaInfluenciaGeradorFluxoRepository; 
	
	@Transactional(readOnly = true)
	@Override
	public List<AreaInfluenciaGeradorFluxoDTO> buscarPorAreaInfluencia(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {
		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		
		return areaInfluenciaGeradorFluxoRepository.buscarPorAreaInfluencia(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<AreaInfluenciaGeradorFluxoDTO> buscarPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {

		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		
		return areaInfluenciaGeradorFluxoRepository.buscarPorCota(filtro);
	}

}	
