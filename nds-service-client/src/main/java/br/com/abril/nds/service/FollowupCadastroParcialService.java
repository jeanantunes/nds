package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;

public interface FollowupCadastroParcialService {
	List<ConsultaFollowupCadastroParcialDTO> obterCadastrosParcial(FiltroFollowupCadastroParcialDTO filtro);
}
