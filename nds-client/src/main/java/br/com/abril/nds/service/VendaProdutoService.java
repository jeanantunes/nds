package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;

public interface VendaProdutoService {
	
	List<VendaProdutoDTO> buscaVendaPorProduto(FiltroVendaProdutoDTO filtro);
	
	List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroVendaProdutoDTO filtro);
	
	List<VendaEncalheDTO> buscarVendasProduto(FiltroVendaEncalheDTO filtro);
	
	Long buscarQntVendasProduto(FiltroVendaEncalheDTO filtro);

}
