package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.ContagemDevolucaoService;

public class ContagemDevolucaoServiceImpl implements ContagemDevolucaoService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;  
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa) {
		
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
	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
	}

	/**
	 * Caso algum valor tiver sido corrigido, o valor da diferenca sera grava como novo registro de ConferenciaEncalheParcial.
	 * 
	 * @param listaContagemDevolucao
	 */
	public void inserirCorrecaoListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
	}

	
	
	private void inserirContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual) {
		
		ProdutoEdicao produtoEdicao = null;//TODO: find by codigoProduto e numeroEdicao
		
		Usuario usuario = null; //TODO: //obter usuario resp.
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(new Date());
		conferenciaEncalheParcial.setDataRecolhimentoDistribuidor(contagem.getDataRecolhimentoDistribuidor());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(contagem.getQtdNota());
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		
	}
	
	private void confirmarContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa) {
		
		//TODO
		
	}
	
	private void sinalizarRecolhimentoFinalizado() {

		
		
	}
	
	private void gerarNotaFiscalParcial() {
	
		//TODO
		
	}

	private void gerarNotaFiscalFinal() {
		
		//TODO
		
	}

	
	
}
