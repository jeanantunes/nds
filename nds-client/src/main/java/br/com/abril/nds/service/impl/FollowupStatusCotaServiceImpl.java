package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.service.FollowupStatusCotaService;

@Service
public class FollowupStatusCotaServiceImpl implements FollowupStatusCotaService {

	@Override
	public List<ConsultaFollowupStatusCotaDTO> obterStatusCota(
			FiltroFollowupStatusCotaDTO filtro) {
		 
		return null;
	}

}
