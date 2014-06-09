package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaQueNaoRecebeExcecaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;

public interface ExcecaoSegmentoParciaisService {

	List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro);

	List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro);

	List<CotaQueRecebeExcecaoDTO> obterCotasQueRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro);
	
	void inserirListaExcecao(List<ExcecaoProdutoCota> listaExcessaoProdutoCota);

	void excluirExcecaoProduto(Long id);

	List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro);
	
	List<CotaQueNaoRecebeExcecaoDTO> autoCompletarPorNomeCotaQueNaoRecebeExcecao(FiltroExcecaoSegmentoParciaisDTO filtro);
}
