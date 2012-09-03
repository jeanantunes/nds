package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ExtratoEdicaoService;


@Service
public class ExtratoEdicaoServiceImpl implements ExtratoEdicaoService {

	@Autowired
	MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	FornecedorRepository fornecedorRepository;
	
	
	@Transactional
	@Override
	public InfoGeralExtratoEdicaoDTO obterInfoGeralExtratoEdicao(String codigoProduto, Long numeroEdicao){
		
		List<ExtratoEdicaoDTO> listaExtratoEdicao = movimentoEstoqueRepository.obterListaExtratoEdicao(codigoProduto, numeroEdicao, StatusAprovacao.APROVADO);
		
		BigDecimal saldoTotalEdicao = new BigDecimal(0.0D);
		
		BigDecimal saldoParcialEdicao = null;
		
		for( ExtratoEdicaoDTO extratoEdicao :  listaExtratoEdicao ) {
			
			BigDecimal qtdEdicaoEntrada = extratoEdicao.getQtdEdicaoEntrada();
			
			BigDecimal qtdEdicaoSaida = extratoEdicao.getQtdEdicaoSaida();
				
			saldoParcialEdicao = qtdEdicaoEntrada.subtract(qtdEdicaoSaida);
			
			saldoTotalEdicao = saldoTotalEdicao.add(saldoParcialEdicao);
				
			extratoEdicao.setQtdParcial(saldoTotalEdicao);
			
		}
		
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = new InfoGeralExtratoEdicaoDTO();
		
		infoGeralExtratoEdicao.setSaldoTotalExtratoEdicao(saldoTotalEdicao);
	
		infoGeralExtratoEdicao.setListaExtratoEdicao(listaExtratoEdicao);
		
		return infoGeralExtratoEdicao;
		
	}
	
	@Transactional
	public ProdutoEdicao obterProdutoEdicao(String codigoProduto, Long numeroEdicao) {
		
		Produto produto = new Produto();
		produto.setCodigo(codigoProduto);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		
		List<ProdutoEdicao> listaProdutoEdicao = produtoEdicaoRepository.obterListaProdutoEdicao(produto, produtoEdicao);
		
		if(listaProdutoEdicao != null && listaProdutoEdicao.size() == 1) {
			return listaProdutoEdicao.get(0);
		}
		
		return null;
		
	}
	
	@Transactional
	public String obterRazaoSocialFornecedorDeProduto(String codigoProduto) {
		
		List<Fornecedor> listaFornecedor =
			fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, null);
		
		if(listaFornecedor != null && listaFornecedor.size() == 1) {
			
			Fornecedor fornecedor = listaFornecedor.get(0);
			
			String razao = (fornecedor.getJuridica() != null) ? fornecedor.getJuridica().getRazaoSocial() : "" ;
			
			return razao;
			
		}
		
		return "";
		
	}

	
	
}
