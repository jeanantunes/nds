package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.repository.FollowupCadastroRepository;
import br.com.abril.nds.service.FollowupCadastroService;

@Service
public class FollowupCadastroServiceImpl implements FollowupCadastroService {
	
	@Autowired
	private FollowupCadastroRepository followupCadastroRepository;

	@Override
	@Transactional
	public List<ConsultaFollowupCadastroDTO> obterCadastros(
			FiltroFollowupCadastroDTO filtro) {
		 
		return this.followupCadastroRepository.obterConsignadosParaChamadao(filtro);
	}

}
