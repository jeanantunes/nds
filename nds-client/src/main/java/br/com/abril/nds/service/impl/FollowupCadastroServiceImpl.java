package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.service.FollowupCadastroService;

@Service
public class FollowupCadastroServiceImpl implements FollowupCadastroService {

	@Override
	public List<ConsultaFollowupCadastroDTO> obterCadastros(
			FiltroFollowupCadastroDTO filtro) {
		 
		return null;
	}

}
