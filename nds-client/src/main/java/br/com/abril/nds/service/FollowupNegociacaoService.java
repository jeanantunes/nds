package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;

public interface FollowupNegociacaoService {
	
	List<ConsultaFollowupNegociacaoDTO> obterNegociacoes(FiltroFollowupNegociacaoDTO filtro);
	
	void cancelarBaixaNegociacao(Long idNegociacao);
	
	Long obterQuantidadeNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro);
}
