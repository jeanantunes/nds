package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;

public interface ExcecaoSegmentoParciaisService {

	List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro);
	
}
