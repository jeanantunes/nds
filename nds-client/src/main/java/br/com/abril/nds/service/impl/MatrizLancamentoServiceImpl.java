package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
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
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	@Autowired
	protected LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	

	@Override
	@Transactional(readOnly = true)
	public BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro, boolean configuracaoInicial) {
	
		this.validarFiltro(filtro);
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			this.obterDadosLancamento(filtro, configuracaoInicial);
		
		return this.balancear(dadosBalanceamentoLancamento);
	}
		
	@Override
	@Transactional
	public List<ProdutoLancamentoDTO> confirmarMatrizLancamento(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento, 
										  						List<Date> datasConfirmadas, Usuario usuario) {

		if (matrizLancamento == null || matrizLancamento.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de lançamento não informada!");
		}

		Map<Long, ProdutoLancamentoDTO> mapaLancamento =
				new TreeMap<Long, ProdutoLancamentoDTO>();

		Set<Long> idsLancamento = new TreeSet<Long>();

		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = entry.getValue();
			
			if (listaProdutoLancamentoDTO == null || listaProdutoLancamentoDTO.isEmpty()) {

				continue;
			}

			for (ProdutoLancamentoDTO produtoRecolhimento : listaProdutoLancamentoDTO) {
				
				Date novaDataLancamento = produtoRecolhimento.getNovaDataLancamento();
				
				Long idLancamento = produtoRecolhimento.getIdLancamento();

				// Monta Map e Set para controlar a atualização dos lançamentos 
				
				if (datasConfirmadas.contains(novaDataLancamento)) {

					idsLancamento.add(idLancamento);
					
					mapaLancamento.put(idLancamento, produtoRecolhimento);
				}
			}
		}
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			this.atualizarLancamentos(idsLancamento, usuario, mapaLancamento);
		
		return produtosLancamento;
	}
	
	/**
	 * Método que atualiza as informações dos lançamentos.
	 * 
	 * @param idsLancamento - identificadores de lançamentos
	 * @param usuario - usuário
	 * @param mapaLancamento - mapa de lancamentos e produtos de recolhimento
	 * 
	 * @return {@link List<ProdutoLancamentoDTO>}
	 */
	private List<ProdutoLancamentoDTO> atualizarLancamentos(Set<Long> idsLancamento, Usuario usuario,
									  						Map<Long, ProdutoLancamentoDTO> mapaLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		
		if (!idsLancamento.isEmpty()) {
		
			List<Lancamento> listaLancamentos = this.lancamentoRepository.obterLancamentosPorId(idsLancamento);
			
			if (listaLancamentos == null || listaLancamentos.isEmpty()) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"Lançamento não encontrado!");
			}
			
			if (idsLancamento.size() != listaLancamentos.size()) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"Lançamento não encontrado!");
			}
			
			ProdutoLancamentoDTO produtoLancamento = null;
			
			boolean gerarHistoricoLancamento = false;
			
			HistoricoLancamento historicoLancamento = null;
			
			for (Lancamento lancamento : listaLancamentos) {
				
				gerarHistoricoLancamento =
					!(lancamento.getStatus().equals(StatusLancamento.BALANCEADO));
				
				produtoLancamento = mapaLancamento.get(lancamento.getId());
				
				Date novaData = produtoLancamento.getNovaDataLancamento();
				
				if (produtoLancamento.getDataLancamentoDistribuidor().compareTo(novaData) != 0) {
					
					lancamento.setNumeroReprogramacoes(atualizarNumeroReprogramacoes(lancamento));
				}
				
				lancamento.setDataLancamentoDistribuidor(novaData);
				lancamento.setStatus(StatusLancamento.BALANCEADO);
				lancamento.setDataStatus(new Date());
				
				this.lancamentoRepository.merge(lancamento);
				
				this.atualizarProdutosLancamentosConfirmados(produtosLancamento, produtoLancamento,
															 lancamento, novaData);
				
				if (gerarHistoricoLancamento) {
				
					historicoLancamento = new HistoricoLancamento();
					
					historicoLancamento.setLancamento(lancamento);
					historicoLancamento.setTipoEdicao(TipoEdicao.ALTERACAO);
					historicoLancamento.setStatus(lancamento.getStatus());
					historicoLancamento.setDataEdicao(new Date());
					historicoLancamento.setResponsavel(usuario);
					
					this.historicoLancamentoRepository.merge(historicoLancamento);
				}
			}
		}
		
		return produtosLancamento;
	}
	
	private void atualizarProdutosLancamentosConfirmados(List<ProdutoLancamentoDTO> produtosLancamento,
														 ProdutoLancamentoDTO produtoLancamento,
														 Lancamento lancamento,
														 Date novaData) {
		
		produtoLancamento.setDataLancamentoDistribuidor(novaData);
		produtoLancamento.setStatusLancamento(StatusLancamento.BALANCEADO.toString());
		produtoLancamento.setNumeroReprogramacoes(lancamento.getNumeroReprogramacoes());
		
		produtosLancamento.add(produtoLancamento);
	}
	
	private Integer atualizarNumeroReprogramacoes(Lancamento lancamento) {
		
		Integer numeroReprogramacoes = lancamento.getNumeroReprogramacoes();
		
		if (numeroReprogramacoes == null) {
			
			numeroReprogramacoes = 0;
		}
		
		numeroReprogramacoes++;
		
		return numeroReprogramacoes;
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
		
		balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
		
		balanceamentoLancamento.setCapacidadeDistribuicao(
			dadosBalanceamentoLancamento.getCapacidadeDistribuicao());
		
		balanceamentoLancamento.setNumeroSemana(dadosBalanceamentoLancamento.getNumeroSemana());
		
		return balanceamentoLancamento;
	}
	
	/**
	 * Valida os dados de entrada para realização do balanceamento.
	 */
	private void validarDadosEntradaBalanceamento(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		if (dadosBalanceamentoLancamento == null
				|| dadosBalanceamentoLancamento.getCapacidadeDistribuicao() == null
				|| dadosBalanceamentoLancamento.getDatasDistribuicaoFornecedorDistribuidor() == null
				|| dadosBalanceamentoLancamento.getDatasExpectativaReparte() == null
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
		
		Set<Date> datasExpectativaReparte =
			dadosBalanceamentoLancamento.getDatasExpectativaReparte();
		
		Set<Date> datasExpectativaReparteOrdenado =
			ordenarMapaExpectativaRepartePorDatasDistribuicao(datasExpectativaReparte,
															  datasDistribuicao);
		
		this.processarProdutosLancamentoNaoBalanceaveis(matrizLancamento,
														dadosBalanceamentoLancamento);
		
		for (Date dataLancamentoPrevista : datasExpectativaReparteOrdenado) {
			
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
	 * Efetua a ordenação do set de expectativa de reparte de acordo com as datas
	 * de distribuição passadas como parâmetro.
	 */
	private Set<Date> ordenarMapaExpectativaRepartePorDatasDistribuicao(
													Set<Date> datasExpectativaReparte, 
													TreeSet<Date> datasDistribuicao) {
		
		Set<Date> datasExpectativaReparteOrdenado =
			new LinkedHashSet<Date>();
		
		for (Date dataDistribuicao : datasDistribuicao) {

			if (datasExpectativaReparte.contains(dataDistribuicao)) {
				
				datasExpectativaReparteOrdenado.add(
					dataDistribuicao);
				
				datasExpectativaReparte.remove(dataDistribuicao);
			}
		}
		
		datasExpectativaReparteOrdenado.addAll(datasExpectativaReparte);
		
		return datasExpectativaReparteOrdenado;
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
		
		this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoFiltrados);
		
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
		
		this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoNaoBalanceados);
		
		produtosLancamentoNaoBalanceados =
			alocarSobrasMatrizLancamento(matrizLancamento,
										 produtosLancamentoNaoBalanceados,
										 mapaExpectativaReparteTotalDiariaAtual,
										 dadosBalanceamentoLancamento,
										 false);
		
		mapaExpectativaReparteTotalDiariaAtual = 
			this.gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(matrizLancamento,
																		datasDistribuicao);
		
		this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoNaoBalanceados);
		
		alocarSobrasMatrizLancamento(matrizLancamento,
									 produtosLancamentoNaoBalanceados,
									 mapaExpectativaReparteTotalDiariaAtual,
									 dadosBalanceamentoLancamento,
									 true);
	}
	
	/**
	 * Ordena os produtos informados por periodicidade do produto
	 * e pela expectativa de reparte.
	 */
	@SuppressWarnings("unchecked")
	private void ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(
														List<ProdutoLancamentoDTO> produtosLancamento) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("ordemPeriodicidadeProduto"));
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
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			produtoLancamento.setNovaDataLancamento(dataLancamento);
		}
		
		produtosLancamentoMatriz.addAll(produtosLancamento);
		
		matrizLancamento.put(dataLancamento, produtosLancamentoMatriz);
	}
	
	/**
	 * Verifica se o produto é balanceável ou não.
	 */
	private boolean isProdutoBalanceavel(ProdutoLancamentoDTO produtoLancamento,
										 DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
		
		if (!produtoLancamento.permiteReprogramacao()) {
			
			return false;
		}
		
		if (StatusLancamento.BALANCEADO.equals(produtoLancamento.getStatusLancamento())
				&& !dadosLancamentoBalanceamento.isConfiguracaoInicial()) {
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private DadosBalanceamentoLancamentoDTO obterDadosLancamento(FiltroLancamentoDTO filtro,
			 									 				 boolean configuracaoInicial) {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Date dataLancamento = filtro.getData();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(distribuidor, dataLancamento, numeroSemana);
		
		TreeSet<Date> datasDistribuicaoFornecedor = 
			this.obterDatasDistribuicaoFornecedor(periodoDistribuicao, filtro.getIdsFornecedores());
			
		TreeSet<Date> datasDistribuicaoDistribuidor = 
			this.obterDatasDistribuicaoDistribuidor(distribuidor, periodoDistribuicao);
			
		TreeSet<Date> datasDistribuicaoFornecedorDistribuidor =
			this.obterDatasDistribuicaoFornecedoresDistribuidor(datasDistribuicaoDistribuidor,
																datasDistribuicaoFornecedor);
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			new DadosBalanceamentoLancamentoDTO();
		
		dadosBalanceamentoLancamento.setNumeroSemana(numeroSemana);
		
		dadosBalanceamentoLancamento.setDatasDistribuicaoFornecedorDistribuidor(
			datasDistribuicaoFornecedorDistribuidor);
		
		dadosBalanceamentoLancamento.setConfiguracaoInicial(configuracaoInicial);
		
		dadosBalanceamentoLancamento.setCapacidadeDistribuicao(
			distribuidor.getCapacidadeDistribuicao());
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			this.lancamentoRepository.obterBalanceamentoLancamento(periodoDistribuicao,
																   filtro.getIdsFornecedores());
		
		dadosBalanceamentoLancamento.setProdutosLancamento(produtosLancamento);

		Set<Date> datasExpectativaReparte = new LinkedHashSet<Date>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			datasExpectativaReparte.add(produtoLancamento.getDataLancamentoDistribuidor());
		}
		
		dadosBalanceamentoLancamento.setDatasExpectativaReparte(datasExpectativaReparte);
		
		// TODO: a regra quanto à utilização desse parâmetro será tratada em um próximo ajuste
		dadosBalanceamentoLancamento.setQtdDiasLimiteParaReprogLancamento(
			distribuidor.getQtdDiasLimiteParaReprogLancamento());
		
		return dadosBalanceamentoLancamento;
	}
	
	/**
	 * Monta o perídodo da semana de distribuição referente à data informada.
	 */
	private Intervalo<Date> getPeriodoDistribuicao(Distribuidor distribuidor,
												   Date dataLancamento,
												   int numeroSemana) {
		
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

				if (calendarioService.isDiaUtil(dataDistribuicaoFornecedor)) {
				
					datasDistribuicaoFornecedorDistribuidor.add(dataDistribuicaoFornecedor);
				}
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

}
