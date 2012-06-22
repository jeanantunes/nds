package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.DadosBalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	private static final String FORMATO_DATA_LANCAMENTO = "dd/MM/yyyy";
	
	@Autowired
	protected LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;
	
	private static final Integer NUMERO_REPROGRAMACOES_LIMITE = 2;

	@Override
	@Transactional(readOnly = true)
	public BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro, boolean configuracaoInicial) {
	
		this.validarFiltro(filtro);
		
		// TODO: verificar necessidade de alterar o mapa de expectativa de reparte
		
		// TODO: verificar balanceamento por periodicidade e fornecedores de produtos
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			this.obterDadosLancamento(filtro, configuracaoInicial);
		
		return this.balancear(dadosBalanceamentoLancamento);
	}
		
	@Override
	@Transactional
	public void confirmarMatrizLancamento(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		// TODO: confirmar matriz de lançamento
	}
	
	/**
	 * Valida o filtro informado para realizar o balanceamento.
	 */
	private void validarFiltro(FiltroLancamentoDTO filtro) {
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Os dados do filtro devem ser informados!");
			
		} else {
		
			List<String> mensagens = new ArrayList<String>();
			
			if (filtro.getData() == null) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (filtro.getIdsFornecedores() == null
					|| filtro.getIdsFornecedores().isEmpty()) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (!mensagens.isEmpty()) {
				
				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
				
				throw new ValidacaoException(validacaoVO);
			}
		}
	}
	
	/**
	 * Efetua todas as etapas para a realização do balanceamento da matriz de lançamento.
	 */
	private BalanceamentoLancamentoDTO balancear(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		this.validarDadosEntradaBalanceamento(dadosBalanceamentoLancamento);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = new BalanceamentoLancamentoDTO();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento = null;
		
		matrizLancamento = this.gerarMatrizBalanceamentoLancamento(dadosBalanceamentoLancamento);
		
		this.configurarMatriz(matrizLancamento);
		
		balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
		
		balanceamentoLancamento.setCapacidadeDistribuicao(
			dadosBalanceamentoLancamento.getCapacidadeDistribuicao());
		
		return balanceamentoLancamento;
	}
	
	/**
	 * Valida os dados de entrada para realização do balanceamento.
	 */
	private void validarDadosEntradaBalanceamento(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		if (dadosBalanceamentoLancamento == null
				|| dadosBalanceamentoLancamento.getCapacidadeDistribuicao() == null
				|| dadosBalanceamentoLancamento.getDatasDistribuicaoFornecedorDistribuidor() == null
				|| dadosBalanceamentoLancamento.getMapaExpectativaReparteTotalDiario() == null
				|| dadosBalanceamentoLancamento.getProdutosLancamento() == null
				|| dadosBalanceamentoLancamento.getQtdDiasLimiteParaReprogLancamento() == null) {
			
			throw new RuntimeException("Dadas para efetuar balanceamento inválidos!");
		}
	}
	
	/**
	 * Gera o mapa contendo a matriz de balanceamento de lançamento.
	 */
	private TreeMap<Date, List<ProdutoLancamentoDTO>> gerarMatrizBalanceamentoLancamento(
												DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			new TreeMap<Date, List<ProdutoLancamentoDTO>>();
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceadosTotal = 
			new ArrayList<ProdutoLancamentoDTO>();
		
		TreeSet<Date> datasDistribuicao =
			dadosBalanceamentoLancamento.getDatasDistribuicaoFornecedorDistribuidor();
		
		Map<Date, BigDecimal> mapaExpectativaTotalReparteDiario =
			dadosBalanceamentoLancamento.getMapaExpectativaReparteTotalDiario();
		
		Map<Date, BigDecimal> mapaExpectativaTotalReparteDiarioOrdenado =
			ordenarMapaExpectativaRepartePorDatasDistribuicao(mapaExpectativaTotalReparteDiario,
															  datasDistribuicao);
		
		this.processarProdutosLancamentoNaoBalanceaveis(matrizLancamento,
														dadosBalanceamentoLancamento);
		
		for (Map.Entry<Date, BigDecimal> entry :
				mapaExpectativaTotalReparteDiarioOrdenado.entrySet()) {
			
			Date dataLancamentoPrevista = entry.getKey();
			
			List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
				this.processarProdutosLancamentoBalanceaveis(matrizLancamento,
															 datasDistribuicao,
															 dataLancamentoPrevista,
															 dadosBalanceamentoLancamento);
			
			if (produtosLancamentoNaoBalanceados != null
					&& !produtosLancamentoNaoBalanceados.isEmpty()) {
				
				produtosLancamentoNaoBalanceadosTotal.addAll(produtosLancamentoNaoBalanceados);
			}
		}
		
		if (!produtosLancamentoNaoBalanceadosTotal.isEmpty()) {
		
			this.processarProdutosLancamentoNaoBalanceados(matrizLancamento,
														   produtosLancamentoNaoBalanceadosTotal,
														   datasDistribuicao,
														   dadosBalanceamentoLancamento);
		}
		
		return matrizLancamento;
	}
	
	/**
	 * Efetua a ordenação do mapa de expectativa de reparte de acordo com as datas
	 * de distribuição passadas como parâmetro.
	 */
	private Map<Date, BigDecimal> ordenarMapaExpectativaRepartePorDatasDistribuicao(
													Map<Date, BigDecimal> mapaExpectativaReparte, 
													TreeSet<Date> datasDistribuicao) {
		
		Map<Date, BigDecimal> mapaExpectativaReparteOrdenado =
			new LinkedHashMap<Date, BigDecimal>();
		
		for (Date dataDistribuicao : datasDistribuicao) {

			BigDecimal expectativaReparte = mapaExpectativaReparte.get(dataDistribuicao);
			
			if (expectativaReparte != null) {
				
				mapaExpectativaReparteOrdenado.put(
					dataDistribuicao, mapaExpectativaReparte.remove(dataDistribuicao));
			}
		}
		
		mapaExpectativaReparteOrdenado.putAll(mapaExpectativaReparte);
		
		return mapaExpectativaReparteOrdenado;
	}
	
	/**
	 * Processa os produtos para lançamento que não devem ser balanceados
	 * e adiciona os mesmos no mapa da matriz de balanceamento.
	 */
	private void processarProdutosLancamentoNaoBalanceaveis(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														    DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
		
		Map<Date, List<ProdutoLancamentoDTO>> mapaProdutosLancamentoNaoBalanceaveisNaData =
			new TreeMap<Date, List<ProdutoLancamentoDTO>>();
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			dadosLancamentoBalanceamento.getProdutosLancamento();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			Date dataLancamentoDistribuidor = produtoLancamento.getDataLancamentoDistribuidor();
			
			if (!this.isProdutoBalanceavel(produtoLancamento,
										   dadosLancamentoBalanceamento)) {
				
				this.adicionarMapaProdutosNaoBalanceaveisData(
														mapaProdutosLancamentoNaoBalanceaveisNaData,
														produtoLancamento,
														dataLancamentoDistribuidor);
			
			}
		}
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry :
				mapaProdutosLancamentoNaoBalanceaveisNaData.entrySet()) {
			
			Date dataLancamentoDistribuidor = entry.getKey();
			
			List<ProdutoLancamentoDTO> produtosLancamentoMapa = entry.getValue();
			
			adicionarProdutoLancamentoNaMatriz(matrizLancamento,
											   produtosLancamentoMapa,
											   dataLancamentoDistribuidor);
		}
	}
	
	/**
	 * Adiciona o produto informado no mapa de não balanceáveis por data.
	 */
	private void adicionarMapaProdutosNaoBalanceaveisData(
			Map<Date, List<ProdutoLancamentoDTO>> mapaProdutosLancamentoNaoBalanceaveisNaData,
			ProdutoLancamentoDTO produtoLancamento,
			Date dataLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoMapa =
			mapaProdutosLancamentoNaoBalanceaveisNaData.get(dataLancamento);
		
		if (produtosLancamentoMapa == null) {
		
			produtosLancamentoMapa = new ArrayList<ProdutoLancamentoDTO>();
		}
		
		produtosLancamentoMapa.add(produtoLancamento);
		
		mapaProdutosLancamentoNaoBalanceaveisNaData.put(dataLancamento,
														produtosLancamentoMapa);
	}

	/**
	 * Processa os produtos para lançamento que devem ser balanceados
	 * e adiciona os mesmos no mapa da matriz de balanceamento.
	 */
	private List<ProdutoLancamentoDTO> processarProdutosLancamentoBalanceaveis(
											    TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
												TreeSet<Date> datasDistribuicao,
												Date dataLancamentoPrevista,
												DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		Date dataLancamentoEscolhida =
			this.obterDataDistribuicaoEscolhida(matrizLancamento,
											    datasDistribuicao,
											    dataLancamentoPrevista);
		
		List<ProdutoLancamentoDTO> produtosLancamentoDataEscolhida =
			matrizLancamento.get(dataLancamentoEscolhida);
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
			new ArrayList<ProdutoLancamentoDTO>();
		
		List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveisDataPrevista = 
			this.obterProdutosLancamentoBalanceaveisPorData(dadosBalanceamentoLancamento,
															dataLancamentoPrevista);
		
		BigDecimal expectativaReparteDataEscolhida =
			this.obterExpectativaReparteTotal(produtosLancamentoDataEscolhida);
		
		produtosLancamentoNaoBalanceados =
			this.balancearProdutosLancamento(matrizLancamento,
											 produtosLancamentoBalanceaveisDataPrevista,
											 dadosBalanceamentoLancamento,
											 expectativaReparteDataEscolhida,
											 dataLancamentoEscolhida,
											 false);
		
		return produtosLancamentoNaoBalanceados;
	}
	
	/**
	 * Obtém uma data de distribuição de acordo as datas de distribuição permitidas.
	 * Ordem de tentativa de escolha da data:
	 * 1º Data igual a data prevista
	 * 2º Menor data que ainda não possui nenhum produto balanceado
	 * 3º Data que possui menor quantidade de expectativa de reparte balanceado
	 */
	private Date obterDataDistribuicaoEscolhida(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
			  									TreeSet<Date> datasDistribuicao,
			  									Date dataLancamentoPrevista) {
		
		Date dataLancamentoEscolhida = null;
		
		if (datasDistribuicao.contains(dataLancamentoPrevista)) {
			
			dataLancamentoEscolhida = dataLancamentoPrevista;
			
		} else {
			
			for (Date dataDistribuicao : datasDistribuicao) {
				
				List<ProdutoLancamentoDTO> produtosLancamento =
					matrizLancamento.get(dataDistribuicao);
				
				if (produtosLancamento == null) {
					
					dataLancamentoEscolhida = dataDistribuicao;
					
					break;
				}
			}
		}
		
		if (dataLancamentoEscolhida == null) {
			
			BigDecimal menorExpectativaReparte = null;
			
			for (Date dataDistribuicao : datasDistribuicao) {
				
				List<ProdutoLancamentoDTO> produtosLancamento =
					matrizLancamento.get(dataDistribuicao);
				
				BigDecimal expectativaReparteData = this.obterExpectativaReparteTotal(produtosLancamento);
				
				if (menorExpectativaReparte == null
						|| expectativaReparteData.compareTo(menorExpectativaReparte) == -1) {
					
					menorExpectativaReparte = expectativaReparteData;
					
					dataLancamentoEscolhida = dataDistribuicao;
				}
			}
		}
		
		return dataLancamentoEscolhida;
	}
	
	/**
	 * Obtém os produtos balanceáveis de uma determinada data.
	 */
	private List<ProdutoLancamentoDTO> obterProdutosLancamentoBalanceaveisPorData(
													DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento, 
													Date dataLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoFiltrados = new ArrayList<ProdutoLancamentoDTO>();
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			dadosBalanceamentoLancamento.getProdutosLancamento();
		
		if (produtosLancamento == null 
				|| produtosLancamento.isEmpty()
				|| dataLancamento == null) {
			
			return produtosLancamentoFiltrados;
		}
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			if (this.isProdutoBalanceavel(produtoLancamento,
										  dadosBalanceamentoLancamento)) {
				
				if (produtoLancamento.getDataLancamentoDistribuidor().equals(dataLancamento)) {
					
					produtosLancamentoFiltrados.add(produtoLancamento);
				}
			}
		}
		
		return produtosLancamentoFiltrados;
	}
	
	/**
	 * Obtém a expectativa de reparte total dos produtos para lançamento.
	 */
	private BigDecimal obterExpectativaReparteTotal(List<ProdutoLancamentoDTO> produtosLancamento) {
		
		BigDecimal expectativaReparteTotal = BigDecimal.ZERO;
		
		if (produtosLancamento != null) {
			
			for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
				
				if (produtoLancamento.getRepartePrevisto() != null) {
				
					expectativaReparteTotal = 
						expectativaReparteTotal.add(produtoLancamento.getRepartePrevisto());
				}
			}
		}
		
		return expectativaReparteTotal;
	}
	
	/**
	 * Método que efetua o balanceamento de acordo com os parâmetros informados.
	 */
	private List<ProdutoLancamentoDTO> balancearProdutosLancamento(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveis,
											DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
											BigDecimal expectativaReparteDataAtual,
											Date dataLancamento,
											boolean permiteExcederCapacidadeDistribuicao) {
		
		BigDecimal capacidadeDistribuicao = dadosBalanceamentoLancamento.getCapacidadeDistribuicao();
		
		Integer qtdDiasLimiteParaReprogLancamento =
			dadosBalanceamentoLancamento.getQtdDiasLimiteParaReprogLancamento();
		
		List<ProdutoLancamentoDTO> produtosLancamentoElegiveis =
			new ArrayList<ProdutoLancamentoDTO>();
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
			new ArrayList<ProdutoLancamentoDTO>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamentoBalanceaveis) {
			
			if (permiteExcederCapacidadeDistribuicao
					|| (!this.excedeLimiteDataReprogramacao(
							produtoLancamento, qtdDiasLimiteParaReprogLancamento, dataLancamento)
						&& !this.excedeCapacidadeDistribuidor(
								expectativaReparteDataAtual, produtoLancamento, capacidadeDistribuicao))) {
				
				expectativaReparteDataAtual =
					expectativaReparteDataAtual.add(produtoLancamento.getRepartePrevisto());
				
				produtosLancamentoElegiveis.add(produtoLancamento);
			
			} else {
				
				produtosLancamentoNaoBalanceados.add(produtoLancamento);
			}
		}
		
		if (!produtosLancamentoElegiveis.isEmpty()) {
			
			adicionarProdutoLancamentoNaMatriz(matrizLancamento,
											   produtosLancamentoElegiveis,
											   dataLancamento);
		}
		
		return produtosLancamentoNaoBalanceados;
	}
	
	/**
	 * Valida se a data de lançamento excede a data limite para reprogramação do produto.
	 */
	private boolean excedeLimiteDataReprogramacao(ProdutoLancamentoDTO produtoLancamento,
												  Integer qtdDiasLimiteParaReprogLancamento,
												  Date dataLancamento) {
		
		Date dataLimiteReprogramacao =
			DateUtil.subtrairDias(produtoLancamento.getDataRecolhimentoPrevista(),
								  qtdDiasLimiteParaReprogLancamento);
		
		return (dataLancamento.compareTo(dataLimiteReprogramacao) == 1);
	}
	
	/**
	 * Valida se o produto informado excede a capacidade de distribuição no dia.
	 */
	private boolean excedeCapacidadeDistribuidor(BigDecimal expectativaReparteDataAtual,
											     ProdutoLancamentoDTO produtoLancamento,
											     BigDecimal capacidadeDistribuicao) {
		
		expectativaReparteDataAtual = 
			expectativaReparteDataAtual.add(produtoLancamento.getRepartePrevisto());
		
		return (expectativaReparteDataAtual.compareTo(capacidadeDistribuicao) == 1);
	}
	
	/**
	 * Processa os produtos que não puderam ser balanceados
	 * pois estes excederam a capacidade de distribuição.
	 */
	private void processarProdutosLancamentoNaoBalanceados(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														   List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados,
														   TreeSet<Date> datasDistribuicao,
														   DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		Map<Date, BigDecimal> mapaExpectativaReparteTotalDiariaAtual = 
			this.gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(matrizLancamento,
																		datasDistribuicao);
		
		this.ordenarProdutosLancamentoPorDataExpectativaReparte(produtosLancamentoNaoBalanceados);
		
		produtosLancamentoNaoBalanceados =
			alocarSobrasMatrizLancamento(matrizLancamento,
										 produtosLancamentoNaoBalanceados,
										 mapaExpectativaReparteTotalDiariaAtual,
										 dadosBalanceamentoLancamento,
										 false);
		
		mapaExpectativaReparteTotalDiariaAtual = 
			this.gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(matrizLancamento,
																		datasDistribuicao);
		
		this.ordenarProdutosLancamentoPorDataExpectativaReparte(produtosLancamentoNaoBalanceados);
		
		alocarSobrasMatrizLancamento(matrizLancamento,
									 produtosLancamentoNaoBalanceados,
									 mapaExpectativaReparteTotalDiariaAtual,
									 dadosBalanceamentoLancamento,
									 true);
	}
	
	/**
	 * Ordena os produtos informados pela data de lançamento de forma decrescente
	 * e pela expectativa de reparte de forma crescente.
	 */
	@SuppressWarnings("unchecked")
	private void ordenarProdutosLancamentoPorDataExpectativaReparte(
														List<ProdutoLancamentoDTO> produtosLancamento) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("dataLancamentoDistribuidor"));
		comparatorChain.addComparator(new BeanComparator("repartePrevisto"));
		
		Collections.sort(produtosLancamento, comparatorChain);
	}

	/**
	 * Gera o mapa de expectativa de reparte diário ordenado pela maior data.
	 */
	private Map<Date, BigDecimal> gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(
														TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														TreeSet<Date> datasDistribuicao) {
		
		Map<Date, BigDecimal> mapaExpectativaReparteTotalDiaria = 
			new TreeMap<Date, BigDecimal>();
		
		for (Date dataDistribuicao : datasDistribuicao) {
			
			List<ProdutoLancamentoDTO> produtosLancamento =
				matrizLancamento.get(dataDistribuicao);
			
			BigDecimal expectativaReparteDiario = BigDecimal.ZERO;
			
			if (produtosLancamento != null) {
				
				for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
					
					expectativaReparteDiario =
						expectativaReparteDiario.add(produtoLancamento.getRepartePrevisto());
				}
			}
			
			mapaExpectativaReparteTotalDiaria.put(dataDistribuicao, expectativaReparteDiario);
		}
		
		return mapaExpectativaReparteTotalDiaria;
	}

	/**
	 * Aloca na matriz de balanceamento os produtos que não foram balanceados anteriormente. 
	 */
	private List<ProdutoLancamentoDTO> alocarSobrasMatrizLancamento(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveis,
											Map<Date, BigDecimal> mapaExpectativaReparteTotalDiariaAtual,
											DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
											boolean permiteExcederCapacidadeDistribuicao) {
		
		for (Map.Entry<Date, BigDecimal> entry : mapaExpectativaReparteTotalDiariaAtual.entrySet()) {
			
			if (produtosLancamentoBalanceaveis == null 
					|| produtosLancamentoBalanceaveis.isEmpty()) {
				
				break;
			}
			
			Date dataLancamento = entry.getKey();
			
			BigDecimal expectativaReparteDataAtual = entry.getValue();
			
			produtosLancamentoBalanceaveis =
				balancearProdutosLancamento(matrizLancamento, produtosLancamentoBalanceaveis,
											dadosBalanceamentoLancamento, expectativaReparteDataAtual,
											dataLancamento, permiteExcederCapacidadeDistribuicao);
		}
		
		return produtosLancamentoBalanceaveis;
	}
	
	/**
	 * Adiciona os produtos informados na matriz de balanceamento na data informada.
	 */
	private void adicionarProdutoLancamentoNaMatriz(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
													List<ProdutoLancamentoDTO> produtosLancamento,
													Date dataLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoMatriz = 
			matrizLancamento.get(dataLancamento);
		
		if (produtosLancamentoMatriz == null) {
			
			produtosLancamentoMatriz = new ArrayList<ProdutoLancamentoDTO>();
		}
		
		produtosLancamentoMatriz.addAll(produtosLancamento);
		
		matrizLancamento.put(dataLancamento, produtosLancamentoMatriz);
	}
	
	/**
	 * Verifica se o produto é balanceável ou não.
	 */
	private boolean isProdutoBalanceavel(ProdutoLancamentoDTO produtoLancamento,
										 DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
		
		if (produtoLancamento.isPossuiRecebimentoFisico()
				&& produtoLancamento.getNumeroReprogramacoes() != null
				&& produtoLancamento.getNumeroReprogramacoes() >= NUMERO_REPROGRAMACOES_LIMITE) {
			
			return false;
		}
		
		if (StatusLancamento.CANCELADO_GD.equals(produtoLancamento.getStatusLancamento())) {
			
			return false;
		}
		
		if (StatusLancamento.BALANCEADO.equals(produtoLancamento.getStatusLancamento())
				&& !dadosLancamentoBalanceamento.isConfiguracaoInicial()) {
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Configura a matriz após a mesma estar balanceada.
	 */
	private void configurarMatriz(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entryMatrizLancamento 
				: matrizLancamento.entrySet()) {
			
			Date dataLancamento = entryMatrizLancamento.getKey();
			
			List<ProdutoLancamentoDTO> produtosLancamento = entryMatrizLancamento.getValue();
			
			for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
				
				produtoLancamento.setDataLancamentoDistribuidor(dataLancamento);
				produtoLancamento.setNovaDataLancamento(dataLancamento);
			}
		}
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private DadosBalanceamentoLancamentoDTO obterDadosLancamento(FiltroLancamentoDTO filtro,
			 									 				 boolean configuracaoInicial) {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(distribuidor, filtro.getData());
		
		TreeSet<Date> datasDistribuicaoFornecedor = 
			this.obterDatasDistribuicaoFornecedor(periodoDistribuicao, filtro.getIdsFornecedores());
			
		TreeSet<Date> datasDistribuicaoDistribuidor = 
			this.obterDatasDistribuicaoDistribuidor(distribuidor, periodoDistribuicao);
			
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			new DadosBalanceamentoLancamentoDTO();
		
		dadosBalanceamentoLancamento.setConfiguracaoInicial(configuracaoInicial);
		
		TreeSet<Date> datasDistribuicaoFornecedorDistribuidor =
			this.obterDatasDistribuicaoFornecedoresDistribuidor(datasDistribuicaoDistribuidor,
																datasDistribuicaoFornecedor);
		
		dadosBalanceamentoLancamento.setDatasDistribuicaoFornecedor(datasDistribuicaoFornecedor);
		dadosBalanceamentoLancamento.setDatasDistribuicaoDistribuidor(datasDistribuicaoDistribuidor);
		
		dadosBalanceamentoLancamento.setDatasDistribuicaoFornecedorDistribuidor(
			datasDistribuicaoFornecedorDistribuidor);
		
		dadosBalanceamentoLancamento.setCapacidadeDistribuicao(
			distribuidor.getCapacidadeDistribuicao());
		
		List<ProdutoLancamentoDTO> produtosLancamento = this.obterProdutosLancamentoMock();
		
		dadosBalanceamentoLancamento.setProdutosLancamento(produtosLancamento);
		
		dadosBalanceamentoLancamento.setMapaExpectativaReparteTotalDiario(
			this.obterMapaExpectativaReparteTotalDiarioMock(produtosLancamento));
		
		dadosBalanceamentoLancamento.setQtdDiasLimiteParaReprogLancamento(
			distribuidor.getQtdDiasLimiteParaReprogLancamento());
		
		return dadosBalanceamentoLancamento;
	}
	
	/**
	 * Monta o perídodo da semana de distribuição referente à data informada.
	 */
	private Intervalo<Date> getPeriodoDistribuicao(Distribuidor distribuidor, Date dataLancamento) {
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataInicialSemana =
			DateUtil.obterDataDaSemanaNoAno(numeroSemana,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFinalSemana =
			DateUtil.adicionarDias(dataInicialSemana, 6);
		
		Intervalo<Date> periodo = new Intervalo<Date>(dataInicialSemana, dataFinalSemana);
		
		return periodo;
	}
	
	/**
	 * Obtém as datas de distribuição dos fornecedores informados.
	 */
	private TreeSet<Date> obterDatasDistribuicaoFornecedor(Intervalo<Date> periodoDistribuicao,
														   List<Long> listaIdsFornecedores) {
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		
		if (listaDistribuicaoFornecedor == null || listaDistribuicaoFornecedor.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING ,
										 "Dias de distribuição para os fornecedores não encontrados!");
		}
		
		Set<Integer> codigosDiaSemanaFornecedor = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			
			codigosDiaSemanaFornecedor.add(distribuicaoFornecedor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasDistribuicaoFornecedor = 
			this.obterDatasDistribuicao(periodoDistribuicao, codigosDiaSemanaFornecedor);
		
		return datasDistribuicaoFornecedor;
	}
	
	/**
	 * Obtém as datas de distribuição do distribuidor.
	 */
	private TreeSet<Date> obterDatasDistribuicaoDistribuidor(Distribuidor distribuidor,
														     Intervalo<Date> periodoDistribuicao) {
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor = 
			this.distribuidorRepository.buscarDiasDistribuicaoDistribuidor(
				distribuidor.getId(), OperacaoDistribuidor.DISTRIBUICAO);
		
		Set<Integer> codigosDiaSemanaDistribuidor = new TreeSet<Integer>();
		
		for (DistribuicaoDistribuidor distribuicaoDistribuidor : listaDistribuicaoDistribuidor) {
			
			codigosDiaSemanaDistribuidor.add(distribuicaoDistribuidor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasDistribuicaoDistribuidor = 
			this.obterDatasDistribuicao(periodoDistribuicao, codigosDiaSemanaDistribuidor);
		
		return datasDistribuicaoDistribuidor;
	}
	
	/**
	 * Obtém as datas de distribuição que são comuns à data de distribuição do distribuidor.
	 */
	public TreeSet<Date> obterDatasDistribuicaoFornecedoresDistribuidor(
														TreeSet<Date> datasDistribuicaoDistribuidor,
														TreeSet<Date> datasDistribuicaoFornecedor) {

		TreeSet<Date> datasDistribuicaoFornecedorDistribuidor = 
			new TreeSet<Date>();
		
		for (Date dataDistribuicaoFornecedor : datasDistribuicaoFornecedor) {
		
			if (datasDistribuicaoDistribuidor.contains(dataDistribuicaoFornecedor)) {
				
				datasDistribuicaoFornecedorDistribuidor.add(dataDistribuicaoFornecedor);
			}
		}
		
		if (datasDistribuicaoFornecedorDistribuidor.isEmpty()) {
			
			throw new RuntimeException(
				"O distribuidor não distribui nas datas de distribuição dos fornecedores!");
		}
		
		return datasDistribuicaoFornecedorDistribuidor;
	}
	
	/**
	 * Obtém as datas para distribuição no período informado,
	 * de acordo com os códigos dos dias da semana informados.
	 */
	private TreeSet<Date> obterDatasDistribuicao(Intervalo<Date> periodoRecolhimento,
									 			 Set<Integer> codigosDiaSemana) {
		
		TreeSet<Date> datasDistribuicao =
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(periodoRecolhimento.getDe(),  
														 periodoRecolhimento.getAte(),
														 codigosDiaSemana);
		
		return datasDistribuicao;
	}
	
	/*
	 * Mock para obter os produtos de lançamento
	 */
	private List<ProdutoLancamentoDTO> obterProdutosLancamentoMock() {
		
		List<ProdutoLancamentoDTO> produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		
		Date data1 = DateUtil.removerTimestamp(new Date());
		Date data2 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data1, 1));
		Date data3 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data2, 1));
		Date data4 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data3, 1));
		Date data5 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data4, 1));
		
		Set<Date> datas = new TreeSet<Date>();
		
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		datas.add(data4);
		datas.add(data5);
		
		Date dataRecolhimentoPrevista =
			DateUtil.removerTimestamp(DateUtil.adicionarDias(new Date(), 10));
		
		long x = 0;
		
		for (Date data : datas) {
			
			for (int i = 0; i < 100; i++) {
			
				ProdutoLancamentoDTO produtoLancamento = new ProdutoLancamentoDTO();
				
				BigDecimal repartePrevisto = new BigDecimal("100.0");
				repartePrevisto = repartePrevisto.add(new BigDecimal(x));
				
				produtoLancamento.setIdLancamento(x);
				produtoLancamento.setDataLancamentoPrevista(data);
				produtoLancamento.setDataLancamentoDistribuidor(data);
				produtoLancamento.setRepartePrevisto(repartePrevisto);
				produtoLancamento.setDataRecolhimentoPrevista(dataRecolhimentoPrevista);
				produtoLancamento.setPeso(new BigDecimal(10));
				produtoLancamento.setValorTotal(new BigDecimal(2));
				produtoLancamento.setReparteFisico(new BigDecimal(5));
				produtoLancamento.setStatusLancamento(StatusLancamento.PLANEJADO.toString());
				
				if (x == 101) {
					produtoLancamento.setStatusLancamento(StatusLancamento.CANCELADO_GD.toString());
				}
				
				produtosLancamento.add(produtoLancamento);
				
				
				x++;
			}
		}
		
		return produtosLancamento;
	}
	
	/*
	 * Mock para obter o mapa de expectativa de reparte total diario
	 */
	private TreeMap<Date, BigDecimal> obterMapaExpectativaReparteTotalDiarioMock(List<ProdutoLancamentoDTO> produtosLancamento) {
		
		TreeMap<Date, BigDecimal> mapaExpectativaReparteTotalDiario =
			new TreeMap<Date, BigDecimal>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			BigDecimal expectativaReparte =
				mapaExpectativaReparteTotalDiario.get(produtoLancamento.getDataLancamentoDistribuidor());
			
			if (expectativaReparte != null) {
				
				expectativaReparte = expectativaReparte.add(produtoLancamento.getRepartePrevisto());
			
			} else {
				
				expectativaReparte = produtoLancamento.getRepartePrevisto();
			}
			
			mapaExpectativaReparteTotalDiario.put(produtoLancamento.getDataLancamentoDistribuidor(),
					  							  expectativaReparte);
		}
				
		return mapaExpectativaReparteTotalDiario;
	}
	
	
	
	
	@Override
	@Transactional
	public List<LancamentoDTO> buscarLancamentosBalanceamento(FiltroLancamentoDTO filtro) {
		
		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		List<LancamentoDTO> dtos = new ArrayList<LancamentoDTO>(
				lancamentos.size());
		for (Lancamento lancamento : lancamentos) {
			LancamentoDTO dto = montarDTO(filtro.getData(),lancamento);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data,
			List<Long> idsFornecedores) {
		return lancamentoRepository.sumarioBalanceamentoMatrizLancamentos(data,
				idsFornecedores);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodo(
			Date dataInicial, List<Long> fornecedores) {
		Date dataFinal = DateUtil.adicionarDias(dataInicial, 6);
		List<DistribuicaoFornecedor> distribuicoes = distribuidorRepository
				.buscarDiasDistribuicaoFornecedor(fornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		Set<DiaSemana> diasDistribuicao = EnumSet.noneOf(DiaSemana.class);
		for (DistribuicaoFornecedor distribuicao : distribuicoes) {
			diasDistribuicao.add(distribuicao.getDiaSemana());
		}

		List<Date> periodoDistribuicao = filtrarPeriodoDistribuicao(
				dataInicial, dataFinal, diasDistribuicao);
		List<ResumoPeriodoBalanceamentoDTO> resumos = lancamentoRepository
				.buscarResumosPeriodo(periodoDistribuicao, fornecedores,
						GrupoProduto.CROMO);
		
		return montarResumoPeriodo(periodoDistribuicao, resumos);
	}

	private List<ResumoPeriodoBalanceamentoDTO> montarResumoPeriodo(
			List<Date> periodoDistribuicao,
			List<ResumoPeriodoBalanceamentoDTO> resumos) {
		Map<Date, ResumoPeriodoBalanceamentoDTO> mapa = new HashMap<Date, ResumoPeriodoBalanceamentoDTO>();
		for (ResumoPeriodoBalanceamentoDTO resumo : resumos) {
			mapa.put(resumo.getData(), resumo);
		}
		List<ResumoPeriodoBalanceamentoDTO> retorno = new ArrayList<ResumoPeriodoBalanceamentoDTO>(
				periodoDistribuicao.size());
		for (Date data : periodoDistribuicao) {
			ResumoPeriodoBalanceamentoDTO resumo = mapa.get(data);
			if (resumo == null) {
				resumo = ResumoPeriodoBalanceamentoDTO.empty(data);
			}
			retorno.add(resumo);
		}
		return retorno;
	}

	private List<Date> filtrarPeriodoDistribuicao (Date dataInicial,
			Date dataFinal, Collection<DiaSemana> diasDistribuicao) {
		List<Date> datas = new ArrayList<Date>();
		while (dataInicial.before(dataFinal) || dataInicial.equals(dataFinal)) {
			DiaSemana ds = DiaSemana.getByDate(dataInicial);
			if (diasDistribuicao.contains(ds)) {
				datas.add(dataInicial);
			}
			dataInicial = DateUtil.adicionarDias(dataInicial, 1);
		}
		return datas;
	}

	private LancamentoDTO montarDTO(Date data, Lancamento lancamento) {
		ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
		Produto produto = produtoEdicao.getProduto();
		LancamentoDTO dto = new LancamentoDTO();
		dto.setCodigoProduto(produto.getCodigo());
		dto.setDataMatrizDistrib(DateUtil.formatarData(
				lancamento.getDataLancamentoDistribuidor(),
				FORMATO_DATA_LANCAMENTO));
		dto.setDataPrevisto(DateUtil.formatarData(
				lancamento.getDataLancamentoPrevista(),
				FORMATO_DATA_LANCAMENTO));
		dto.setDataRecolhimento(DateUtil.formatarData(
				lancamento.getDataRecolhimentoPrevista(),
				FORMATO_DATA_LANCAMENTO));
		dto.setId(lancamento.getId());
		dto.setIdFornecedor(1L);
		dto.setNomeFornecedor(produto.getFornecedor().getJuridica().getNomeFantasia());
		dto.setLancamento(lancamento.getTipoLancamento().getDescricao());
		dto.setNomeProduto(produto.getNome());
		dto.setNumEdicao(produtoEdicao.getNumeroEdicao());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		dto.setPreco(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
		dto.setReparte(lancamento.getReparte().toString());
		BigDecimal total = produtoEdicao.getPrecoVenda().multiply(lancamento.getReparte());
		dto.setTotal(CurrencyUtil.formatarValor(total));
		dto.setFisico(lancamento.getTotalRecebimentoFisico().toString());
		Estudo estudo = lancamento.getEstudo();
		if (estudo != null) {
			dto.setQtdeEstudo(estudo.getQtdeReparte().toString());
		} else {
			dto.setQtdeEstudo("0");
		}
		dto.setFuro(lancamento.isFuro());
		dto.setCancelamentoGD(lancamento.isCancelamentoGD());
		dto.setExpedido(lancamento.isExpedido());
		dto.setEstudoFechado(lancamento.isEstudoFechado());
		if (DateUtil.isHoje(data) && lancamento.isSemRecebimentoFisico()) {
			dto.setSemFisico(true);
		}
		return dto;
	}
	
	

}
