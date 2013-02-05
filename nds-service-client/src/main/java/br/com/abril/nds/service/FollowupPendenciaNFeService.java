package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;

public interface FollowupPendenciaNFeService {
	List<ConsultaFollowupPendenciaNFeDTO> obterPendencias(FiltroFollowupPendenciaNFeDTO filtro);
}
