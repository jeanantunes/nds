package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
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
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
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
	 * Salva os dados parciais de devolução digitados pelo usuario.
	 * 
	 * @param listaContagemDevolucao
	 */
	@Transactional
	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, MockPerfilUsuario mockPerfilUsuario) {
		
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
	public void inserirCorrecaoListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, MockPerfilUsuario mockPerfilUsuario) {
		
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
		
		ProdutoEdicao produtoEdicao = null;//TODO: find by codigoProduto e numeroEdicao
		
		Usuario usuario = null; //TODO: //obter usuario resp.
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(new Date());
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
		
		ProdutoEdicao produtoEdicao = null;//TODO: find by codigoProduto e numeroEdicao
		
		Usuario usuario = null; //TODO: //obter usuario resp.
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(new Date());
		conferenciaEncalheParcial.setDataRecolhimentoDistribuidor(contagem.getDataRecolhimentoDistribuidor());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(correcao);
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	public void confirmarContagemDevolucao() {
		
		//Obter lista conferenciaEncalheParcial referente ao filtro do usuario...
		
		//Obs: Somente de registro que estejam com statusAprovacao = PENDENTE
		
		//Iterar a lista flagndo os registros com statusAprovacao = APROVADO
		
		//Usar a lista iterada para gerar os dados da notaFiscalParcial
		
		
	}
	
	private void sinalizarContagemDevolucaoFinalizada() {
		//TODO
	}
	
	private void gerarNotaFiscalParcial() {
		//TODO
	}

	private void gerarNotaFiscalFinal() {
		//TODO
	}

	
}
