package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
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
	 * 
	 * @param listaContagemDevolucao
	 * @param usuario
	 * @param mockPerfilUsuario
	 */
	@Transactional
	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario, MockPerfilUsuario mockPerfilUsuario) {
		
		if(MockPerfilUsuario.USUARIO_ENCARREGADO.equals(mockPerfilUsuario)) {
			inserirCorrecaoListaContagemDevolucao(listaContagemDevolucao, usuario);
		} else {
			inserirListaContagemDevolucao(listaContagemDevolucao, usuario);
		}
		
	}
	
	
	/**
	 * Salva os dados parciais de devolução digitados pelo usuario.
	 * 
	 * @param listaContagemDevolucao
	 * @param usuario
	 */
	private void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirContagemDevolucao(contagem, dataAtual, usuario);
		
		}
		
		
	}
	
	/**
	 * Caso algum valor tiver sido corrigido, o valor da diferenca sera 
	 * grava como novo registro de ConferenciaEncalheParcial.
	 * 
	 * @param listaContagemDevolucao
	 * @param usuario
	 */
	private void inserirCorrecaoListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual, usuario);
			
		}
		
				
	}
	
	private void inserirContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario) {
		
		if(contagem.getQtdNota() == null) {
			return;
		}
		
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao	= contagem.getNumeroEdicao();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataMovimento(contagem.getDataMovimento());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(contagem.getQtdNota());
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		conferenciaEncalheParcial.setNfParcialGerada(false);
		conferenciaEncalheParcial.setDiferencaApurada(false);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	
	private void inserirCorrecaoContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario) {
		
		Date dataMovimento = contagem.getDataMovimento();
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		BigDecimal qtdTotalConferenciaEncalheParcialOld = conferenciaEncalheParcialRepository.obterQtdTotalEncalheParcial(
				StatusAprovacao.PENDENTE,
				dataMovimento, 
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
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataMovimento(contagem.getDataMovimento());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(correcao);
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		conferenciaEncalheParcial.setDiferencaApurada(false);
		conferenciaEncalheParcial.setNfParcialGerada(false);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	@Transactional
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual, usuario);
			
			Date dataMovimento = contagem.getDataMovimento();
			String codigoProduto = contagem.getCodigoProduto();
			Long numeroEdicao = contagem.getNumeroEdicao();
			
			List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
					conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
					false, 
					false,
					StatusAprovacao.PENDENTE, 
					dataMovimento, 
					codigoProduto, 
					numeroEdicao);
			
			aprovarConferenciaEncalheParcial(listaConferenciaEncalheParcial, dataAtual, usuario);
			
		}
		
		gerarNotaFiscalParcial();

		verificarConferenciaEncalheFinalizada();
		
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
	
	/**
	 * Sinaliza nos registros de ConferenciaEncalheParcial pertinentes 
	 * (statusAprovacao = APROVADO, nfParcialGerada = false, diferencaApurada = false)
	 * que um registro de NF parcial foi gerado referente aos mesmos.	 
	 */
	private void sinalizarNFParcialGerada() {
		
		List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
				conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
				false, 
				false, 
				StatusAprovacao.APROVADO, 
				null, 
				null, 
				null);
		
		for(ConferenciaEncalheParcial parcial :  listaConferenciaEncalheParcial) {
			
			parcial.setNfParcialGerada(true);
			
			conferenciaEncalheParcialRepository.alterar(parcial);
			
		}
		
	}
	
	private void ajustarDiferencaConferenciaEncalheContagemDevolucao(ContagemDevolucaoDTO contagem) {
		
		BigDecimal qtdMovimento = (contagem.getQtdDevolucao() == null) ? new BigDecimal(0.0D) : contagem.getQtdDevolucao();
		
		BigDecimal qtdNota = (contagem.getQtdNota() == null) ? new BigDecimal(0.0D) : contagem.getQtdNota();
		
		
		BigDecimal diferenca = qtdMovimento.subtract(qtdNota);
		
		contagem.setDiferenca(diferenca);

		//TODO: ajustar diferenca
		
		sinalizarDiferencaApurada(contagem);
		
		//TODO: sinalizar nos registros de movimento estoque cota para 
		// que não seja mais apresentados na grid de digitacao contagem devolucaso.
		
	}
	
	private void sinalizarDiferencaApurada(ContagemDevolucaoDTO contagem) {
		//TODO: sinalizar diferenca apurada...
	}
	
	private boolean verificarConferenciaEncalheFinalizadaParaData(Map<Date, StatusOperacao> mapaControleConferencia, Date dataMovimento) {
	
		if( mapaControleConferencia.get(dataMovimento) == null ) {

			ControleConferenciaEncalhe controleConferenciaEncalhe = controleConferenciaEncalheRepository.obterControleConferenciaEncalhe(dataMovimento);
			
			if(controleConferenciaEncalhe == null || controleConferenciaEncalhe.getStatus() == null) {
				mapaControleConferencia.put(dataMovimento, StatusOperacao.EM_ANDAMENTO);
				return false;
			}
			
			StatusOperacao statusOperacao = controleConferenciaEncalhe.getStatus();
			
			mapaControleConferencia.put(dataMovimento, statusOperacao);
		
			return StatusOperacao.CONCLUIDO.equals(statusOperacao);
			
		} else {
		
			return StatusOperacao.CONCLUIDO.equals(mapaControleConferencia.get(dataMovimento));
			
		}
		
	}
	
	/**
	 * Obtém uma lista de ContagemDevolucao a partir de registros
	 * de ConferenciaEncalheParcial que estejam com o seguinte formato
	 * (statusAprovacao = APROVADO, diferencaApurada = false, nfParcialGerada = true) 
	 * e verifica para cada registro se a conferencia de encalhe do mesmo foi 
	 * finalizada, caso positivo, aponta as diferencas entre qtde do movimentoEstoqueCota 
	 * do mesmo e a qtde conferenciaEncalheParcial para o mesmo.
	 * 
	 */
	private void verificarConferenciaEncalheFinalizada() {
		
		Map<Date, StatusOperacao> mapaControleConferencia = new HashMap<Date, StatusOperacao>();

		List<ContagemDevolucaoDTO> listaContagemDevolucao = conferenciaEncalheParcialRepository.obterListaContagemDevolucao(false, true, StatusAprovacao.APROVADO);
		
		for(ContagemDevolucaoDTO contagemDevolucaoDTO : listaContagemDevolucao) {
			
			boolean indConferenciaEncalheFinalizada = verificarConferenciaEncalheFinalizadaParaData(mapaControleConferencia, contagemDevolucaoDTO.getDataMovimento());
			
			if(indConferenciaEncalheFinalizada) {
				ajustarDiferencaConferenciaEncalheContagemDevolucao(contagemDevolucaoDTO);
			}
			
		}
				
	
	}
	
	private void gerarNotaFiscalParcial() {

		List<ContagemDevolucaoDTO> listaContagemDevolucao = conferenciaEncalheParcialRepository.obterListaContagemDevolucao(false, false, StatusAprovacao.APROVADO);
		
		//TODO: gerar registro de nf parcial...
		
		sinalizarNFParcialGerada();
		
	}


	
}
