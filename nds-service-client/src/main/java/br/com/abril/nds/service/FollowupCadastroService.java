package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;

public interface FollowupCadastroService {
	List<ConsultaFollowupCadastroDTO> obterCadastros(FiltroFollowupCadastroDTO filtro);
}
