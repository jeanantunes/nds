package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;

public interface ExcecaoSegmentoParciaisRepository {

	List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCotaPorSegmento(FiltroExcecaoSegmentoParciaisDTO filtro);
	
}
