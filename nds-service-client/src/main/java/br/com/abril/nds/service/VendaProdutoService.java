package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;

public interface VendaProdutoService {
	
	List<VendaProdutoDTO> buscaVendaPorProduto(FiltroVendaProdutoDTO filtro);
	
	List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroDetalheVendaProdutoDTO filtro);

}
