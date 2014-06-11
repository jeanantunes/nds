package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupDistribuicaoDTO;

public interface FollowupDistribuicaoService {
	List<ConsultaFollowupDistribuicaoDTO> obterCotas (ConsultaFollowupDistribuicaoDTO dto);
}
