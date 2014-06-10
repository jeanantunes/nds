package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RecolhimentoService;

/**
 * Estratégia de balanceamento de recolhimento automático.
 * 
 * @author Discover Technology 
 *
 */
public class BalanceamentoRecolhimentoAutomaticoStrategy extends AbstractBalanceamentoRecolhimentoStrategy {

	@Autowired
	ProdutoService produtoService; 
	
	@Autowired
	RecolhimentoService recolhimentoService;
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BalanceamentoRecolhimentoDTO gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		List<ProdutoRecolhimentoDTO> todosProdutosRecolhimentoNaoBalanceados = new ArrayList<ProdutoRecolhimentoDTO>();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = 
			dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria();
		
		TreeSet<Date> datasRecolhimento = dadosRecolhimento.getDatasRecolhimentoDisponiveis();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaOrdenado =
			super.ordenarMapaExpectativaEncalhePorDatasRecolhimento(
				mapaExpectativaEncalheTotalDiaria, datasRecolhimento);
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				mapaExpectativaEncalheTotalDiariaOrdenado.entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			BigDecimal expectativaEncalheABalancear = entryExpectativaEncalheTotalDiaria.getValue();

			super.processarProdutosRecolhimentoNaoBalanceaveis(
				matrizRecolhimentoBalanceada, dadosRecolhimento, dataRecolhimentoPrevista);
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados =
				this.balancearProdutosRecolhimento(matrizRecolhimentoBalanceada, dataRecolhimentoPrevista, 
					datasRecolhimento, dadosRecolhimento, expectativaEncalheABalancear);
			
			if (produtosRecolhimentoNaoBalanceados != null 
					&& ! produtosRecolhimentoNaoBalanceados.isEmpty()) {
				
				todosProdutosRecolhimentoNaoBalanceados.addAll(produtosRecolhimentoNaoBalanceados);
			}
			/*
			if(!datasRecolhimento.contains(dataRecolhimentoPrevista)){
				if(matrizRecolhimentoBalanceada.containsKey(dataRecolhimentoPrevista)){
				 todosProdutosRecolhimentoNaoBalanceados.addAll(matrizRecolhimentoBalanceada.get(dataRecolhimentoPrevista));
				 matrizRecolhimentoBalanceada.remove(dataRecolhimentoPrevista);
				}
			}
			*/
		}
		
		if (!datasRecolhimento.isEmpty()) {
		
			this.gerenciarProdutosRecolhimentoNaoBalanceados(
				matrizRecolhimentoBalanceada, todosProdutosRecolhimentoNaoBalanceados,
				datasRecolhimento, dadosRecolhimento);
		}
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			super.gerarBalanceamentoRecolhimentoDTO(matrizRecolhimentoBalanceada,
													todosProdutosRecolhimentoNaoBalanceados,
													dadosRecolhimento);
			
		
		return balanceamentoRecolhimento;
	}
	
	/*
	 * Aloca os produtos na matriz de recolhimento.
	 */
	@SuppressWarnings("unchecked")
	private List<ProdutoRecolhimentoDTO> alocarProdutosMatrizRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
																			BigInteger capacidadeManuseio,
																		    List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis,
																		    List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData,
																		    BigDecimal expectativaEncalheTotalAtualNaData,
																		    Date dataBalanceamento,
																		    boolean permiteExcederCapacidadeManuseio) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("dataRecolhimentoDistribuidor"), true);
		comparatorChain.addComparator(new BeanComparator("expectativaEncalhe"), true);
		
		Collections.sort(produtosRecolhimentoBalanceaveis, comparatorChain);
		
		BigDecimal expectativaEncalheASerPreenchida =
			new BigDecimal(capacidadeManuseio).subtract(expectativaEncalheTotalAtualNaData);
		
		BigDecimal expectativaEncalheElegivel = BigDecimal.ZERO;
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoElegiveis = 
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		//TODO
		if(produtosRecolhimentoNaData==null){
			produtosRecolhimentoNaData = new ArrayList<>();
		}
		//
		for (int indice = 0; indice < produtosRecolhimentoBalanceaveis.size(); indice++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimentoBalanceavel = produtosRecolhimentoBalanceaveis.get(indice);
			
			expectativaEncalheElegivel = 
				expectativaEncalheElegivel.add(produtoRecolhimentoBalanceavel.getExpectativaEncalhe());
			
			if(expectativaEncalheElegivel.compareTo(produtoRecolhimentoBalanceavel.getExpectativaEncalhe())==0 && produtoRecolhimentoBalanceavel.getExpectativaEncalhe().longValue()> capacidadeManuseio.longValue()){
				
				produtosRecolhimentoElegiveis.add(produtoRecolhimentoBalanceavel);
				produtosRecolhimentoBalanceaveis.remove(indice--);
			}else {
			
			if (!permiteExcederCapacidadeManuseio
					&& (expectativaEncalheElegivel.doubleValue() > expectativaEncalheASerPreenchida.doubleValue())) {
				
				break;
			}

			produtosRecolhimentoElegiveis.add(produtoRecolhimentoBalanceavel);
			produtosRecolhimentoBalanceaveis.remove(indice--);
			}
		}
		
		super.atualizarMatrizRecolhimento(
			matrizRecolhimento, produtosRecolhimentoNaData, produtosRecolhimentoElegiveis, dataBalanceamento);
		
		return produtosRecolhimentoBalanceaveis;
	}
	
	/*
	 * Efetua o balanceamento de produtos de recolhimento.
	 */
	private List<ProdutoRecolhimentoDTO> balancearProdutosRecolhimento(
												Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada,
												Date dataRecolhimentoPrevista, TreeSet<Date> datasRecolhimento,
												RecolhimentoDTO dadosRecolhimento,
												BigDecimal expectativaEncalheABalancear) {

		Date dataBalanceamento = super.obterDataRecolhimentoPermitida(datasRecolhimento, dataRecolhimentoPrevista);

		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados = null;

		BigInteger capacidadeManuseio = new BigInteger(""+dadosRecolhimento.getMediaRecolhimentoDistribuidor());
		

		List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis = 
			super.obterProdutosRecolhimentoBalanceaveisPorData(
				dadosRecolhimento, dataRecolhimentoPrevista);

		if (dataBalanceamento == null) {
			
			return produtosRecolhimentoBalanceaveis;
		}
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData = matrizRecolhimentoBalanceada.get(dataBalanceamento);
		
		BigDecimal expectativaEncalheTotalAtualNaData = super.obterExpectativaEncalheTotal(produtosRecolhimentoNaData);
		
		BigDecimal excessoExpectativaEncalhe = 
			super.calcularExcessoExpectativaEncalhe(expectativaEncalheTotalAtualNaData, capacidadeManuseio, expectativaEncalheABalancear);

		if (super.validarLimiteCapacidadeRecolhimentoDistribuidor(excessoExpectativaEncalhe)) {

			super.atualizarMatrizRecolhimento(
				matrizRecolhimentoBalanceada, produtosRecolhimentoNaData,
				produtosRecolhimentoBalanceaveis, dataBalanceamento);

		} else {

			produtosRecolhimentoNaoBalanceados = 
				this.alocarProdutosMatrizRecolhimento(
					matrizRecolhimentoBalanceada, capacidadeManuseio, produtosRecolhimentoBalanceaveis,
						produtosRecolhimentoNaData, expectativaEncalheTotalAtualNaData, dataBalanceamento, false);
		}

		return produtosRecolhimentoNaoBalanceados;
	}
	
	/*
	 * Gerencia os produtos do recolhimento que não foram balanceados por excederem a capacidade de manuseio.
	 */
	private void gerenciarProdutosRecolhimentoNaoBalanceados(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
															 List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis,
															 TreeSet<Date> datasRecolhimento,
															 RecolhimentoDTO dadosRecolhimento) {
		
		//TODO: tratar produtos
		/*
		BigInteger capacidadeRecolhimentoExcedente =
			this.obterCapacidadeRecolhimentoExcedente(
				produtosRecolhimentoBalanceaveis, datasRecolhimento, dadosRecolhimento);
		*/
		BigInteger capacidadeRecolhimentoExcedente = new BigInteger(""+dadosRecolhimento.getMediaRecolhimentoDistribuidor());
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaAtual = null;
		
		long quantidadeProdutosBalancear = produtosRecolhimentoBalanceaveis.size();
		
		long quantidadeProdutosNaoBalanceados = 0;
		
		while (!produtosRecolhimentoBalanceaveis.isEmpty()
					&& quantidadeProdutosBalancear != quantidadeProdutosNaoBalanceados) {
			
			quantidadeProdutosBalancear = produtosRecolhimentoBalanceaveis.size();
			
			mapaExpectativaEncalheTotalDiariaAtual = 
				this.gerarMapaExpectativaEncalheTotalDiariaOrdenadoPelaMaiorData(matrizRecolhimento, dadosRecolhimento.getDatasRecolhimentoFornecedor());
			
			this.alocarSobrasMatrizRecolhimento(
				matrizRecolhimento, mapaExpectativaEncalheTotalDiariaAtual, 
				produtosRecolhimentoBalanceaveis, dadosRecolhimento,
				capacidadeRecolhimentoExcedente, false);
			
			quantidadeProdutosNaoBalanceados = produtosRecolhimentoBalanceaveis.size();
		}
		
		mapaExpectativaEncalheTotalDiariaAtual = 
			this.gerarMapaExpectativaEncalheTotalDiariaOrdenadoPelaMaiorData(matrizRecolhimento,dadosRecolhimento.getDatasRecolhimentoFornecedor());
		
		this.alocarSobrasMatrizRecolhimento(
			matrizRecolhimento, mapaExpectativaEncalheTotalDiariaAtual, 
			produtosRecolhimentoBalanceaveis, dadosRecolhimento,
			capacidadeRecolhimentoExcedente, true);
	}
	
	private BigInteger obterCapacidadeRecolhimentoExcedente(
									List<ProdutoRecolhimentoDTO> produtosRecolhimento,
									TreeSet<Date> datasRecolhimento,
									RecolhimentoDTO dadosRecolhimento) {

		BigDecimal encalheTotalBalancear = this.obterExpectativaEncalheTotal(produtosRecolhimento);

		BigInteger capacidadeRecolhimentoExcedente = null;

		//BigInteger capacidadeRecolhimento = dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor();
		//TODO
		//dadosRecolhimento.addMediaRecolhimentoDistribuidor(dadosRecolhimento.getProdutosRecolhimento().size());
		BigInteger capacidadeRecolhimento = new BigInteger(""+dadosRecolhimento.getMediaRecolhimentoDistribuidor());

		BigInteger totalDiasRecolhimento = BigInteger.valueOf(datasRecolhimento.size());

		BigInteger mediaEncalheExcedente =
			encalheTotalBalancear.toBigInteger().divide(totalDiasRecolhimento);

		capacidadeRecolhimentoExcedente = capacidadeRecolhimento.add(mediaEncalheExcedente);

		return capacidadeRecolhimentoExcedente;
	}
	
	/*
	 * Faz a alocação dos produtos de recolhimento em sobra no balanceamento (excedentes da capacidade de
	 * manuseio do distribuidor nos dias previstos).
	 */
	private List<ProdutoRecolhimentoDTO> alocarSobrasMatrizRecolhimento(
											    Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaAtual,
											    List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis,
											    RecolhimentoDTO dadosRecolhimento,
											    BigInteger capacidadeRecolhimento,
											    boolean permiteExcederCapacidadeManuseioDistribuidor) {
		
		if (produtosRecolhimentoBalanceaveis.isEmpty()) {
			
			return produtosRecolhimentoBalanceaveis;
		}
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiariaAtual :
				mapaExpectativaEncalheTotalDiariaAtual.entrySet()) {
			
			if (produtosRecolhimentoBalanceaveis.isEmpty()) {
				
				break;
			}
			
			Date dataBalanceamento = entryExpectativaEncalheTotalDiariaAtual.getKey();
			
			if (!dadosRecolhimento.getDatasRecolhimentoFornecedor().contains(dataBalanceamento)) {
				
				continue;
			}
			
			BigDecimal expectativaEncalheTotalAtualNaData = entryExpectativaEncalheTotalDiariaAtual.getValue();
				
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData = matrizRecolhimento.get(dataBalanceamento);
			
			long media = dadosRecolhimento.getMediaRecolhimentoDistribuidor();
			media = media+100;

			if(expectativaEncalheTotalAtualNaData.longValue() < media){
			
			produtosRecolhimentoBalanceaveis =
				this.alocarProdutosMatrizRecolhimento(
					matrizRecolhimento, capacidadeRecolhimento,
						produtosRecolhimentoBalanceaveis, produtosRecolhimentoNaData, 
							expectativaEncalheTotalAtualNaData, dataBalanceamento, 
								permiteExcederCapacidadeManuseioDistribuidor);
			}else{
				
			    // Se existir somente 1 produto e ele or que a media 
				if(produtosRecolhimentoNaData!=null && produtosRecolhimentoNaData.size()==1) {
					
					produtosRecolhimentoBalanceaveis =
							this.alocarProdutosMatrizRecolhimento(
								matrizRecolhimento, capacidadeRecolhimento,
									produtosRecolhimentoBalanceaveis, produtosRecolhimentoNaData, 
										expectativaEncalheTotalAtualNaData, dataBalanceamento, 
											permiteExcederCapacidadeManuseioDistribuidor);
					
				dadosRecolhimento.setMediaRecolhimentoDistribuidor(dadosRecolhimento.getMediaRecolhimentoDistribuidor()-(((expectativaEncalheTotalAtualNaData.longValue()-dadosRecolhimento.getMediaRecolhimentoDistribuidor())/dadosRecolhimento.getDatasRecolhimentoFornecedor().size())));
				}
			}
		}
		
		
		return produtosRecolhimentoBalanceaveis;
	}

}
