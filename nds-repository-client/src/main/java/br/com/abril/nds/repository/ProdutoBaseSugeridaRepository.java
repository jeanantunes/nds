package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;

public interface ProdutoBaseSugeridaRepository {

	List<ProdutoBaseSugeridaDTO> obterBaseSugerida(Long idEstudo);

}
