package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;

/**
 * Estratégia de balanceamento de recolhimento automático.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoAutomaticoStrategy implements BalanceamentoRecolhimentoStrategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BalanceamentoRecolhimentoDTO balancear(RecolhimentoDTO dadosRecolhimento) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = new BalanceamentoRecolhimentoDTO();
		
		if (!validarDadosRecolhimento(dadosRecolhimento)) {
			
			return balanceamentoRecolhimento; 
		}
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = this.obterMatrizRecolhimento(dadosRecolhimento);
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
		
		return balanceamentoRecolhimento;
	}
	
	/*
	 * Obtém a matriz de recolhimento
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> obterMatrizRecolhimento(RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = 
			this.gerarMatrizRecolhimentoBalanceada(dadosRecolhimento);
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaBalanceado = 
			this.gerarMapaExpectativaEncalheTotalDiaria(matrizRecolhimento);

		// Separar quantidade de produtos-edição para não quebrar entre os dias
		
		// Verificar qual quantidade de produto melhor se encaixa em cada dia com capacidade disponível
		// (ordenar pela menor quantidade mas verificar qual melhor completa o total da capacidade)
		
		// Gerenciar sobras (jogar nos dias que não excedem a capacidade de manuseio)
		
		// Quando todas capacidades estiverem excedidas dar preferência para maior PEB 
		// (verificar dia de recolhimento previsto do produto)
		
		return matrizRecolhimento;
	}
	
	/*
	 * Gera a matriz de recolhimento balanceada nas datas determinadas.
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria();
		
		Set<Date> datasRecolhimentoFornecedor = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				mapaExpectativaEncalheTotalDiaria.entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			BigDecimal expectativaEncalheABalancear = entryExpectativaEncalheTotalDiaria.getValue();
			
			Date dataBalanceamento = 
				this.obterDataRecolhimentoPermitida(datasRecolhimentoFornecedor, dataRecolhimentoPrevista);

			List<ProdutoRecolhimentoDTO> produtosRecolhimentoFiltrados = 
				this.obterProdutosRecolhimentoPorData(dadosRecolhimento.getProdutosRecolhimento(), dataRecolhimentoPrevista);
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData = 
				matrizRecolhimentoBalanceada.get(dataBalanceamento);
			
			// Verificar se a expectativa de encalhe excede a capacidade diária de manuseio
			
			if (produtosRecolhimentoNaData != null) {
				
				BigDecimal capacidadeManuseio = dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor();
				
				BigDecimal expectativaEncalheNaData = this.obterExpectativaEncalheTotal(produtosRecolhimentoNaData);
				
				if (expectativaEncalheNaData.add(expectativaEncalheABalancear).compareTo(capacidadeManuseio) > 0) {
					
					
				}
			}
			
			matrizRecolhimentoBalanceada.put(dataBalanceamento, produtosRecolhimentoFiltrados);
		}
		
		return matrizRecolhimentoBalanceada;
	}
	
	/*
	 * Obtém a expectativa de encalhe total dos produtos para recolhimento.
	 */
	private BigDecimal obterExpectativaEncalheTotal(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		BigDecimal expectativaEncalheTotal = BigDecimal.ZERO;
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			if (produtoRecolhimento.getExpectativaEncalhe() != null) {
			
				expectativaEncalheTotal = 
					expectativaEncalheTotal.add(produtoRecolhimento.getExpectativaEncalhe());
			}
		}
		
		return expectativaEncalheTotal;
	}
	
	/*
	 * Obtém os produtos de recolhimento de uma determinada data.
	 */
	private List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoPorData(List<ProdutoRecolhimentoDTO> produtosRecolhimento, 
																		  Date dataRecolhimentoDesejada) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoFiltrados = new ArrayList<ProdutoRecolhimentoDTO>();
		
		if (produtosRecolhimento == null 
				|| produtosRecolhimento.isEmpty()
				|| dataRecolhimentoDesejada == null) {
			
			return produtosRecolhimentoFiltrados;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			if (produtoRecolhimento.getDataRecolhimentoDistribuidor().equals(dataRecolhimentoDesejada)) {
				
				produtosRecolhimentoFiltrados.add(produtoRecolhimento);
			}
		}
		
		return produtosRecolhimentoFiltrados;
	}
	
	/*
	 * Gera o mapa de expectativa de encalhe total diária de acordo com a matriz de recolhimento.
	 */
	private Map<Date, BigDecimal> gerarMapaExpectativaEncalheTotalDiaria(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = new TreeMap<Date, BigDecimal>();
		
		if (matrizRecolhimento == null || matrizRecolhimento.isEmpty()) {
			
			return mapaExpectativaEncalheTotalDiaria;
		}

		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entryMatrizRecolhimento : matrizRecolhimento.entrySet()) {
			
			Date dataRecolhimento = entryMatrizRecolhimento.getKey();
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = entryMatrizRecolhimento.getValue();
			
			BigDecimal expectativaEncalheTotalDiaria = BigDecimal.ZERO;
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if (produtoRecolhimento.getExpectativaEncalhe() != null) {
				
					expectativaEncalheTotalDiaria = 
						expectativaEncalheTotalDiaria.add(produtoRecolhimento.getExpectativaEncalhe());
				}
			}
			
			mapaExpectativaEncalheTotalDiaria.put(dataRecolhimento, expectativaEncalheTotalDiaria);
		}
		
		return mapaExpectativaEncalheTotalDiaria;
	}
	
	/*
	 *  Obtém uma data de recolhimento de acordo as datas de recolhimento permitidas, efetuando
	 *  a aproximação de datas se necessário.
	 */
	private Date obterDataRecolhimentoPermitida(Set<Date> datasRecolhimentoPermitidas, Date dataRecolhimentoPrevista) {

		Date dataRecolhimentoPermitida = null;
		
		if (!datasRecolhimentoPermitidas.contains(dataRecolhimentoPrevista)) {
			
			for (Date dataRecolhimentoFornecedor : datasRecolhimentoPermitidas) {
				
				if (dataRecolhimentoPrevista.compareTo(dataRecolhimentoFornecedor) < 0) {
					
					dataRecolhimentoPermitida = dataRecolhimentoFornecedor;
					
					break;
				}
			}
			
			if (dataRecolhimentoPermitida == null) {
				
				throw new RuntimeException(
					"Data de recolhimento fora da semana de recolhimento: " + dataRecolhimentoPrevista);
			}
			
		} else {
			
			dataRecolhimentoPermitida = dataRecolhimentoPrevista;
		}
			
		return dataRecolhimentoPermitida;
	}
	
	/*
	 * Valida os dados de recolhimento.
	 */
	private boolean validarDadosRecolhimento(RecolhimentoDTO dadosRecolhimento) {
		
		return !(dadosRecolhimento == null
					|| dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor() == null
					|| dadosRecolhimento.getDatasRecolhimentoDistribuidor() == null
					|| dadosRecolhimento.getDatasRecolhimentoFornecedor() == null
					|| dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria() == null
					|| dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria().isEmpty()
					|| dadosRecolhimento.getProdutosRecolhimento() == null 
					|| dadosRecolhimento.getProdutosRecolhimento().isEmpty());
	}

}
