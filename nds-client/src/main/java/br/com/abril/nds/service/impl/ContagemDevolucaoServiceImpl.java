package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.ContagemDevolucaoService;
import br.com.abril.nds.util.MockPerfilUsuario;

public class ContagemDevolucaoServiceImpl implements ContagemDevolucaoService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;  
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private ConferenciaEncalheParcialRepository conferenciaEncalheParcialRepository;

	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Autowired
	private ControleContagemDevolucaoRepository controleContagemDevolucaoRepository;

	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Transactional
	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, MockPerfilUsuario mockPerfilUsuario) {
		
		InfoContagemDevolucaoDTO info = new InfoContagemDevolucaoDTO();
		
		Integer qtdTotalRegistro = movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(filtroPesquisa);
		info.setQtdTotalRegistro(qtdTotalRegistro);

		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				filtroPesquisa, 
				tipoMovimentoEstoque, 
				true);
		info.setListaContagemDevolucao(listaContagemDevolucao);
		
		BigDecimal valorTotalGeral = movimentoEstoqueCotaRepository.obterValorTotalGeralContagemDevolucao(filtroPesquisa, tipoMovimentoEstoque);
		info.setValorTotalGeral(valorTotalGeral);
		
		carregarDadosAdicionais(info, listaContagemDevolucao);
		
		return info;
	
	}
	
	/**
	 * Calcula dados adicionais.
	 * 
	 * @param listaContagemDevolucao
	 */
	private void carregarDadosAdicionais(InfoContagemDevolucaoDTO info, List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			BigDecimal precoVenda = (contagem.getPrecoVenda() == null) ? new BigDecimal(0.0D) : contagem.getPrecoVenda();
			
			BigDecimal qtdMovimento = (contagem.getQtdDevolucao() == null) ? new BigDecimal(0.0D) : contagem.getQtdDevolucao();
			
			BigDecimal qtdNota = (contagem.getQtdNota() == null) ? new BigDecimal(0.0D) : contagem.getQtdNota();
			
			
			BigDecimal diferenca = qtdMovimento.subtract(qtdNota);
			contagem.setDiferenca(diferenca);
			
			BigDecimal valorTotal = qtdMovimento.multiply(precoVenda);
			contagem.setValorTotal(valorTotal);
			
		}
		
		
	}

	/**
	 * Insere os dados parciais de devolução digitados pelo usuario.
	 */
	@Transactional
	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, MockPerfilUsuario mockPerfilUsuario) {
		
		if(MockPerfilUsuario.USUARIO_ENCARREGADO.equals(mockPerfilUsuario)) {
			inserirCorrecaoListaContagemDevolucao(listaContagemDevolucao);
		} else {
			inserirListaContagemDevolucao(listaContagemDevolucao);
		}
		
	}
	
	
	/**
	 * Salva os dados parciais de devolução digitados pelo usuario.
	 * 
	 * @param listaContagemDevolucao
	 */
	private void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirContagemDevolucao(contagem, dataAtual);
		
		}
		
		
	}

	/**
	 * Caso algum valor tiver sido corrigido, o valor da diferenca sera grava como novo registro de ConferenciaEncalheParcial.
	 * 
	 * @param listaContagemDevolucao
	 */
	private void inserirCorrecaoListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual);
			
		}
		
				
	}
	
	private void inserirContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual) {
		
		if(contagem.getQtdNota() == null) {
			return;
		}
		
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao	= contagem.getNumeroEdicao();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		Usuario usuario = null; //TODO: //obter usuario resp.
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataRecolhimentoDistribuidor(contagem.getDataRecolhimentoDistribuidor());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(contagem.getQtdNota());
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	
	private void inserirCorrecaoContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual) {
		
		Date dataRecolhimentoDistribuidor = contagem.getDataRecolhimentoDistribuidor();
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		BigDecimal qtdTotalConferenciaEncalheParcialOld = conferenciaEncalheParcialRepository.obterQtdTotalEncalheParcial(
				StatusAprovacao.PENDENTE,
				dataRecolhimentoDistribuidor, 
				codigoProduto, 
				numeroEdicao);
		
		BigDecimal qtdTotalConferenciaEncalheParcialNew = contagem.getQtdNota();
		
		if( qtdTotalConferenciaEncalheParcialNew == null ) {
			return;
		}
		
		BigDecimal correcao = null;
		
		if( qtdTotalConferenciaEncalheParcialOld != null ) {
			
			if(qtdTotalConferenciaEncalheParcialOld.compareTo(qtdTotalConferenciaEncalheParcialNew) == 0) {
				return;
			}
			
			correcao = qtdTotalConferenciaEncalheParcialNew.subtract(qtdTotalConferenciaEncalheParcialOld);
			
		} else {
			
			correcao = qtdTotalConferenciaEncalheParcialNew;
			
		}
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		Usuario usuario = null; //TODO: //obter usuario resp.
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataRecolhimentoDistribuidor(contagem.getDataRecolhimentoDistribuidor());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(correcao);
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	@Transactional
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		Date dataAtual = new Date();
		
		List<ContagemDevolucaoDTO> listaContagemAgrupada =  new LinkedList<ContagemDevolucaoDTO>();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual);
			
			Date dataRecolhimentoDistribuidor = contagem.getDataRecolhimentoDistribuidor();
			String codigoProduto = contagem.getCodigoProduto();
			Long numeroEdicao = contagem.getNumeroEdicao();
			
			List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
					conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
					StatusAprovacao.PENDENTE, 
					dataRecolhimentoDistribuidor, 
					codigoProduto, 
					numeroEdicao);
			
			agruparListaConferenciaEncalheParcial(listaContagemAgrupada, listaConferenciaEncalheParcial);
			
			aprovarConferenciaEncalheParcial(listaConferenciaEncalheParcial, dataAtual, usuario);
			
		}
		
		//TODO
		
	}
	
	/**
	 * Agrupa os registro da lista de ConferenciaEncalheParcial
	 * em um unico objeto ContagemDevolucaoDTO que será adicionado
	 * a lista listaContagemAgrupada.
	 * 
	 * @param listaContagemAgrupada
	 * @param listaConferenciaEncalheParcial
	 */
	private void agruparListaConferenciaEncalheParcial(
			List<ContagemDevolucaoDTO> listaContagemAgrupada, 
			List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial) {
		
		//TODO...
		
	}
	
	/**
	 * Aprova os registros de Status Conferencia Encalhe Parcial.
	 * 
	 * @param listaConferenciaEncalheParcial
	 * @param dataAtual
	 * @param usuario
	 */
	private void aprovarConferenciaEncalheParcial(List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial, Date dataAtual, Usuario usuario) {
		
		for(ConferenciaEncalheParcial parcial :  listaConferenciaEncalheParcial) {
			
			parcial.setStatusAprovacao(StatusAprovacao.APROVADO);
			parcial.setResponsavel(usuario);
			parcial.setDataAprovacao(dataAtual);
			
			conferenciaEncalheParcialRepository.alterar(parcial);
			
		}
		
	}
	
	private void sinalizarControleContagemDevolucaoFinalizada() {
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = 
				controleConferenciaEncalheRepository.obterControleConferenciaEncalhe(null);
		
		//TODO
		ControleContagemDevolucao controleContagemDevolucao = 
				controleContagemDevolucaoRepository.obterControleContagemDevolucao(null);

		
	}
	
	private void gerarNotaFiscalParcial(List<ContagemDevolucaoDTO> listaContagemAgrupada) {
		//TODO
	}


	
}
