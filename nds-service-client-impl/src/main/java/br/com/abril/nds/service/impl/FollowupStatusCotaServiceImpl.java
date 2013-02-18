package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.repository.FollowupStatusCotaRepository;
import br.com.abril.nds.service.FollowupStatusCotaService;

@Service
public class FollowupStatusCotaServiceImpl implements FollowupStatusCotaService {

	@Autowired
	private FollowupStatusCotaRepository followupStatusCotaRepository;
	
	@Override
	@Transactional
	public List<ConsultaFollowupStatusCotaDTO> obterStatusCota(
			FiltroFollowupStatusCotaDTO filtro) {
		 
		return this.followupStatusCotaRepository.obterConsignadosParaChamadao(filtro);
	}

}
