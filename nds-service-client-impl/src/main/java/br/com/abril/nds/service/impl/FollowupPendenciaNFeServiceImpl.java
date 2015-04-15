package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.repository.FollowupPendenciaNFeRepository;
import br.com.abril.nds.service.FollowupPendenciaNFeService;

@Service
public class FollowupPendenciaNFeServiceImpl implements FollowupPendenciaNFeService {
	
	@Autowired
	private FollowupPendenciaNFeRepository followupPendenciaNFeRepository;

	@Override
	@Transactional
	public List<ConsultaFollowupPendenciaNFeDTO> consultaPendenciaNFEEncalhe(FiltroFollowupPendenciaNFeDTO filtro) {
		return this.followupPendenciaNFeRepository.consultaPendenciaNFEEncalhe(filtro);
	}

	@Override
	@Transactional
	public Long qtdeRegistrosPendencias(FiltroFollowupPendenciaNFeDTO filtro) {
		return this.followupPendenciaNFeRepository.qtdeRegistrosPendencias(filtro);
	}

}
