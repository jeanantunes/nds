package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;
import br.com.abril.nds.repository.FollowupCadastroParcialRepository;
import br.com.abril.nds.service.FollowupCadastroParcialService;

@Service
public class FollowupCadastroParcialServiceImpl implements FollowupCadastroParcialService {
	
	@Autowired
	private FollowupCadastroParcialRepository repository;


	@Override
	@Transactional
	public List<ConsultaFollowupCadastroParcialDTO> obterCadastrosParcial(
			FiltroFollowupCadastroParcialDTO filtro) {
		return this.repository.obterConsignadosParaChamadao(filtro);
	}

}
