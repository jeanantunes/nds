package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.Util;

@Service
public class ProdutoEdicaoServiceImpl implements ProdutoEdicaoService {

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Override
	@Transactional
	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto) {
		return produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto(nomeProduto);
	}

	@Override
	@Transactional
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		
		if (codigo == null || codigo.isEmpty()){
			throw new IllegalArgumentException("Código é obrigatório.");
		}
		
		if (edicao == null){
			throw new IllegalArgumentException("Edição é obrigatório.");
		}
		
		if (dataLancamento == null){
			throw new IllegalArgumentException("Data Lançamento é obrigatório.");
		}
		
		return produtoEdicaoRepository.
				obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
						codigo, nomeProduto, edicao, dataLancamento);
	}
	
	@Override
	@Transactional
	public boolean validarNumeroEdicao(String codigoProduto, String numeroEdicao) {

		if (codigoProduto == null || codigoProduto.isEmpty()){
			throw new IllegalArgumentException("Código é obrigatório.");
		}
		
		if (numeroEdicao == null || numeroEdicao.isEmpty()){
			throw new IllegalArgumentException("Número edição é obrigatório.");
		}

		if(!Util.isValidNumber(numeroEdicao)) {
			throw new IllegalArgumentException("Número edição é inválido.");
		}
		
		ProdutoEdicao produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto,
																			 Long.parseLong(numeroEdicao));
		
		return (produtoEdicao != null) ? true : false;
	}
}