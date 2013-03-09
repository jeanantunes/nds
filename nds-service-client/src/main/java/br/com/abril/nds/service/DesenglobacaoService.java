package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.DesenglobaDTO;

public interface DesenglobacaoService {
	
	void obterDesenglobacaoPorCota();
	
	void inserirDesenglobacao(List<DesenglobaDTO> desenglobaDTO);

}
