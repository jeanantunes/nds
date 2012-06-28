package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;

public interface FollowupStatusCotaService {
	List<ConsultaFollowupStatusCotaDTO> obterStatusCota(FiltroFollowupStatusCotaDTO filtro);
}
