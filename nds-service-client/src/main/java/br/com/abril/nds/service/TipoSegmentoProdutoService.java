package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface TipoSegmentoProdutoService {

	List<TipoSegmentoProduto> obterTipoSegmentoProduto();
	
	TipoSegmentoProduto obterTipoProdutoSegmentoPorId(Long id);

	List<TipoSegmentoProduto> obterTipoSegmentoProdutoOrdenados(Ordenacao ordem);
	
	
}
