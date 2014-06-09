package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;

public interface TipoSegmentoProdutoService {

	List<TipoSegmentoProduto> obterTipoSegmentoProduto();
	
	TipoSegmentoProduto obterTipoProdutoSegmentoPorId(Long id);
	
	
}
