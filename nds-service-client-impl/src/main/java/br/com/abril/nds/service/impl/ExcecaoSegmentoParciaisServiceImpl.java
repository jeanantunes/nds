package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaQueNaoRecebeExcecaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;

@Service
public class ExcecaoSegmentoParciaisServiceImpl implements ExcecaoSegmentoParciaisService {

	@Autowired
	private ExcecaoSegmentoParciaisRepository excecaoSegmentoParciaisRepository; 
	
	@Transactional(readOnly = true)
	@Override
	public List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {

		return excecaoSegmentoParciaisRepository.obterProdutosRecebidosPelaCota(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
		return excecaoSegmentoParciaisRepository.obterProdutosNaoRecebidosPelaCota(filtro);
	}

	@Transactional
	@Override
	public void inserirListaExcecao(List<ExcecaoProdutoCota> listaExcessaoProdutoCota) {
		for (ExcecaoProdutoCota excessaoProdutoCota : listaExcessaoProdutoCota) {
			excecaoSegmentoParciaisRepository.adicionar(excessaoProdutoCota);
		}
	}

	@Transactional
	@Override
	public void excluirExcecaoProduto(Long id) {
		excecaoSegmentoParciaisRepository.removerPorId(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaQueRecebeExcecaoDTO> obterCotasQueRecebemExcecaoPorProduto(	FiltroExcecaoSegmentoParciaisDTO filtro) {
		return excecaoSegmentoParciaisRepository.obterCotasQueRecebemExcecaoPorProduto(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
		return excecaoSegmentoParciaisRepository.obterCotasQueNaoRecebemExcecaoPorProduto(filtro);
	}
}
