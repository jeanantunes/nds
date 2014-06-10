package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;

public interface ProdutoBaseSugeridaService {

	List<ProdutoBaseSugeridaDTO> obterBaseSugerida(Long idEstudo);
	
}
