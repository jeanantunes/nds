package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
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
	public InfoGeralExtratoEdicaoDTO obterInfoGeralExtratoEdicao(FiltroExtratoEdicaoDTO filtroExtratoEdicao){
		
		if (filtroExtratoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro para a consulta de extrato edição não pode ser nulo.");
		}
		
		filtroExtratoEdicao.setGruposExcluidos(obterGruposMovimentoEstoqueExtratoEdicao());
		
		List<ExtratoEdicaoDTO> listaExtratoEdicao = movimentoEstoqueRepository.obterListaExtratoEdicao(filtroExtratoEdicao, StatusAprovacao.APROVADO);
		
		BigInteger saldoTotalEdicao = BigInteger.ZERO;
		
		BigInteger saldoParcialEdicao = null;
		
		for( ExtratoEdicaoDTO extratoEdicao :  listaExtratoEdicao ) {
			
			BigInteger qtdEdicaoEntrada = extratoEdicao.getQtdEdicaoEntrada();
			
			BigInteger qtdEdicaoSaida = extratoEdicao.getQtdEdicaoSaida();
				
			saldoParcialEdicao = qtdEdicaoEntrada.subtract(qtdEdicaoSaida);
			
			saldoTotalEdicao = saldoTotalEdicao.add(saldoParcialEdicao);
				
			extratoEdicao.setQtdParcial(saldoTotalEdicao);
			
		}
		
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = new InfoGeralExtratoEdicaoDTO();
		
		infoGeralExtratoEdicao.setSaldoTotalExtratoEdicao(saldoTotalEdicao);
	
		infoGeralExtratoEdicao.setListaExtratoEdicao(listaExtratoEdicao);
		
		return infoGeralExtratoEdicao;
		
	}
	
	private List<GrupoMovimentoEstoque> obterGruposMovimentoEstoqueExtratoEdicao() {
		
		List<GrupoMovimentoEstoque> grupos = new ArrayList<GrupoMovimentoEstoque>();

		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE);
		
		return grupos;
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
