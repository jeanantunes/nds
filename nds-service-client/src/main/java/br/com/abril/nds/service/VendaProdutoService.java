package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface VendaProdutoService {
	
	List<VendaProdutoDTO> buscaVendaPorProduto(FiltroVendaProdutoDTO filtro);
	
//	List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroVendaProdutoDTO filtro);
	
	List<TipoClassificacaoProduto> buscarClassificacaoProduto();
	List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroDetalheVendaProdutoDTO filtro);
	

}
