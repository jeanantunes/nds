package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;

public interface FollowupChamadaoService {
	List<ConsultaFollowupChamadaoDTO> obterConsignados(FiltroFollowupChamadaoDTO filtro);
}
