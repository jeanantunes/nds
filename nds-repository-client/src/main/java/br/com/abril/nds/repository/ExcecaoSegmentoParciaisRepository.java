package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaQueNaoRecebeExcecaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;

public interface ExcecaoSegmentoParciaisRepository extends Repository<ExcecaoProdutoCota, Long> {

	List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro);

	List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro);
	
	List<CotaQueRecebeExcecaoDTO> obterCotasQueRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro);
	
	List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro);
	
	List<CotaQueNaoRecebeExcecaoDTO> autoCompletarPorNomeCotaQueNaoRecebeExcecao(FiltroExcecaoSegmentoParciaisDTO filtro);
}