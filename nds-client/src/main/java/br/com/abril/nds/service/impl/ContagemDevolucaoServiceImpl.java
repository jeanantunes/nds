package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
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
	 * Carrega os valores de exemplar nota.
	 *  
	 * @param listaContagemDevolucao
	 */
	private void carregarValorExemplarNota(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		//TODO: code
		
	}

	/**
	 * Salva os dados parciais de devolução digitados pelo usuario.
	 * 
	 * @param listaContagemDevolucao
	 */
	private void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
	}
	
	
}
