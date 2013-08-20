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
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ExtratoEdicaoService;
import br.com.abril.nds.util.Util;


@Service
public class ExtratoEdicaoServiceImpl implements ExtratoEdicaoService {

	@Autowired
	MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;
	
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
		
		List<ExtratoEdicaoDTO> listaExtratoEdicao =  movimentoEstoqueRepository.obterListaExtratoEdicao(filtroExtratoEdicao, StatusAprovacao.APROVADO);
		
		if (listaExtratoEdicao.isEmpty()){
			
			return null;
		}

		for(int i = 0; i < listaExtratoEdicao.size(); i++) {	
			
			ExtratoEdicaoDTO itemExtratoEdicao = listaExtratoEdicao.get(i);
			
			BigInteger qtdEntrada = BigInteger.ZERO;
			
			qtdEntrada = itemExtratoEdicao.getQtdEdicaoEntrada();
			
			BigInteger qtdSaida = BigInteger.ZERO;
			
			qtdSaida = itemExtratoEdicao.getQtdEdicaoSaida();
			
			BigInteger qtdeParcial = qtdEntrada.subtract(qtdSaida);
		
			if (i > 0) {

				itemExtratoEdicao.setQtdParcial(
					listaExtratoEdicao.get(i - 1).getQtdParcial().add(qtdeParcial));
				
			} else {
			
				itemExtratoEdicao.setQtdParcial(qtdeParcial);
			}
		}
			
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = new InfoGeralExtratoEdicaoDTO();

		infoGeralExtratoEdicao.setSaldoTotalExtratoEdicao(listaExtratoEdicao.get(listaExtratoEdicao.size() - 1).getQtdParcial());
	
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
		
		codigoProduto = Util.padLeft(codigoProduto, "0", 8);
		
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
