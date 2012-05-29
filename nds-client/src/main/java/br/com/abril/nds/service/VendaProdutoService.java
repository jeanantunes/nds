package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;

public interface VendaProdutoService {
	
	List<VendaProdutoDTO> buscaVendaporProduto(FiltroVendaProdutoDTO filtro);

}
