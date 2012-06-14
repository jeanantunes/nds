package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PeriodoVO;

/**
 * Implementação de serviços referentes ao recolhimento.
 * 
 * @author Discover Technology
 *
 */
@Service
public class RecolhimentoServiceImpl implements RecolhimentoService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	protected ChamadaEncalheRepository chamadaEncalheRepository;
		
	@Autowired
	protected ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	protected ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParciaisService parciaisService;
	
	private static final Integer QTDE_PERIODOS_PARCIAIS = 1;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
																 List<Long> listaIdsFornecedores,
																 TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
																 boolean forcarBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento =
			this.obterDadosRecolhimento(numeroSemana, listaIdsFornecedores,
										tipoBalanceamentoRecolhimento, forcarBalanceamento);
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(tipoBalanceamentoRecolhimento);
		
		return balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void salvarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												Usuario usuario) {
		
		if (matrizRecolhimento == null
				|| matrizRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaRecolhimentos =
			new TreeMap<Long, ProdutoRecolhimentoDTO>();
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
			
			if (listaProdutoRecolhimentoDTO == null
					|| listaProdutoRecolhimentoDTO.isEmpty()) {
			
				continue;
			}
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutoRecolhimentoDTO) {
			
				mapaRecolhimentos.put(produtoRecolhimento.getIdLancamento(), produtoRecolhimento);
				
				idsLancamento.add(produtoRecolhimento.getIdLancamento());
			}
		}
		
		atualizarLancamentos(idsLancamento, usuario, mapaRecolhimentos);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void confirmarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												   Integer numeroSemana, Usuario usuario) {
		
		if (matrizRecolhimento == null
				|| matrizRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento =
			new TreeMap<Long, ProdutoRecolhimentoDTO>();
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		Set<Long> idsProdutoEdicaoParcial = new TreeSet<Long>();
		
		Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos = new TreeMap<Date, Set<Long>>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
			
			if (listaProdutoRecolhimentoDTO == null
					|| listaProdutoRecolhimentoDTO.isEmpty()) {
			
				continue;
			}
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutoRecolhimentoDTO) {
				
				Date novaDataRecolhimento = produtoRecolhimento.getNovaData();
				
				Long idLancamento = produtoRecolhimento.getIdLancamento();

				// Monta Map e Set para controlar a atualização dos lançamentos 
				
				mapaLancamentoRecolhimento.put(idLancamento, produtoRecolhimento);
				
				idsLancamento.add(idLancamento);
				
				// Monta Map para controlar a geração de chamada de encalhe
				
				Set<Long> idsLancamentoPorData = mapaDataRecolhimentoLancamentos.get(novaDataRecolhimento);
				
				if (idsLancamentoPorData == null) {
					
					idsLancamentoPorData = new TreeSet<Long>();
				}
				
				idsLancamentoPorData.add(idLancamento);
				
				mapaDataRecolhimentoLancamentos.put(novaDataRecolhimento, idsLancamentoPorData);
				
				// Monta Set para controlar a geração de períodos parciais
				
				if (produtoRecolhimento.getParcial() != null) {
					
					idsProdutoEdicaoParcial.add(produtoRecolhimento.getIdProdutoEdicao());
				}
			}
		}
		
		atualizarLancamentos(idsLancamento, usuario, mapaLancamentoRecolhimento);
		
		gerarChamadasEncalhe(mapaDataRecolhimentoLancamentos, numeroSemana);
		
		gerarPeriodosParciais(idsProdutoEdicaoParcial, usuario);
	}
	
	/**
	 * Método que atualiza as informações dos lançamentos.
	 * 
	 * @param idsLancamento - identificadores de lançamentos
	 * @param usuario - usuário
	 * @param mapaLancamentoRecolhimento - mapa de lancamentos e produtos de recolhimento
	 */
	private void atualizarLancamentos(Set<Long> idsLancamento, Usuario usuario,
									  Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento) {
		
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
			
			ProdutoRecolhimentoDTO produtoRecolhimento = null;
			
			boolean gerarHistoricoLancamento = false;
			
			HistoricoLancamento historicoLancamento = null;
			
			for (Lancamento lancamento : listaLancamentos) {
				
				gerarHistoricoLancamento =
					!(lancamento.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO));
				
				produtoRecolhimento = mapaLancamentoRecolhimento.get(lancamento.getId());
				
				lancamento.setDataRecolhimentoDistribuidor(produtoRecolhimento.getNovaData());
				lancamento.setSequenciaMatriz(produtoRecolhimento.getSequencia().intValue());
				lancamento.setStatus(StatusLancamento.BALANCEADO_RECOLHIMENTO);
				lancamento.setDataStatus(new Date());
				
				this.lancamentoRepository.merge(lancamento);
				
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
	}
	
	/**
	 * Gera as chamadas de encalhe para os produtos da matriz de balanceamento.
	 * 
	 * @param mapaDataRecolhimentoLancamentos - mapa de datas de recolhimento e identificadores de lancamentos.
	 * @param numeroSemana - número da semana
	 */
	private void gerarChamadasEncalhe(Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos,
			 						 Integer numeroSemana) {
		
		if (mapaDataRecolhimentoLancamentos != null && !mapaDataRecolhimentoLancamentos.isEmpty()) {
			
			Distribuidor distribuidor = this.distribuidorService.obter();
			
			PeriodoVO periodoRecolhimento = getPeriodoRecolhimento(distribuidor, numeroSemana);
			
			removerChamadasEncalhe(periodoRecolhimento.getDataInicial(),
								   periodoRecolhimento.getDataFinal());
			
			for (Map.Entry<Date, Set<Long>> entry : mapaDataRecolhimentoLancamentos.entrySet()) {
			
				Set<Long> idsLancamento = entry.getValue();
				
				Date dataRecolhimento = entry.getKey();
				
				if (idsLancamento == null || idsLancamento.isEmpty()) {
					
					continue;
				}
				
				List<EstoqueProdutoCota> listaEstoqueProdutoCota =
					this.estoqueProdutoCotaRepository.buscarListaEstoqueProdutoCota(idsLancamento);
				
				if (listaEstoqueProdutoCota == null || listaEstoqueProdutoCota.isEmpty()) {
					
					throw new ValidacaoException(TipoMensagem.WARNING,
						"Estoque produto cota não encontrado!");
				}
				
				ProdutoEdicao produtoEdicao = null;
				Cota cota = null;
				ChamadaEncalhe chamadaEncalhe = null;
				ChamadaEncalhe chamadaEncalheLista = null;
				ChamadaEncalheCota chamadaEncalheCota = null;			
				
				List<ChamadaEncalhe> listaChamadaEncalhe = new ArrayList<ChamadaEncalhe>();
				
				for (EstoqueProdutoCota estoqueProdutoCota : listaEstoqueProdutoCota) {
					
					produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
					
					cota = estoqueProdutoCota.getCota();
					
					chamadaEncalhe = new ChamadaEncalhe();
					
					chamadaEncalhe.setDataRecolhimento(dataRecolhimento);
					chamadaEncalhe.setProdutoEdicao(produtoEdicao);
					chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
					
					chamadaEncalheLista = obterChamadaEncalheLista(listaChamadaEncalhe,
																   chamadaEncalhe);
					
					if (chamadaEncalheLista == null) {
					
						chamadaEncalhe = this.chamadaEncalheRepository.merge(chamadaEncalhe);
					
						listaChamadaEncalhe.add(chamadaEncalhe);
						
					} else{
						
						chamadaEncalhe = chamadaEncalheLista;
					}
					
					chamadaEncalheCota = new ChamadaEncalheCota();
					
					chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
					chamadaEncalheCota.setFechado(false);
					chamadaEncalheCota.setCota(cota);
					
					BigDecimal qtdPrevista = BigDecimal.ZERO;
					
					if (estoqueProdutoCota != null) {
						
						qtdPrevista = estoqueProdutoCota.getQtdeRecebida().subtract(
							estoqueProdutoCota.getQtdeDevolvida());
					}
					
					chamadaEncalheCota.setQtdePrevista(qtdPrevista);
					
					this.chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
				}
			}
		}
	}
	
	/**
	 * Gera os períodos de parciais
	 * 
	 * @param idsProdutoEdicaoParcial - identificadores dos produtos de edição
	 * @param usuario - usuário
	 */
	private void gerarPeriodosParciais(Set<Long> idsProdutoEdicaoParcial, Usuario usuario) {
		
		List<ProdutoEdicao> listaProdutoEdicao =
			produtoEdicaoRepository.obterProdutosEdicaoPorId(idsProdutoEdicaoParcial);
		
		if (listaProdutoEdicao == null || listaProdutoEdicao.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Produto edição não encontrado!");
		}
		
		if (listaProdutoEdicao.size() != idsProdutoEdicaoParcial.size()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Produto edição não encontrado!");
		}
		
		Distribuidor distribuidor = this.distribuidorRepository.obter();
		
		for (ProdutoEdicao produtoEdicao : listaProdutoEdicao) {
		
			parciaisService.gerarPeriodosParcias(produtoEdicao, QTDE_PERIODOS_PARCIAIS,
												 usuario, null, distribuidor);
		}
	}
	
	/**
	 * Método que obtém uma chamada de encalhe da lista informada.
	 * É utilizado para informar se já existe uma chamada de encalhe adicionada anteriormente na lista.
	 * 
	 * @param listaChamadaEncalhe - lista de chamadas de encalhe
	 * @param chamadaEncalhe - chamada de encalhe
	 * 
	 * @return chamada de encalhe, caso já exista uma chamada na lista
	 */
	private ChamadaEncalhe obterChamadaEncalheLista(List<ChamadaEncalhe> listaChamadaEncalhe,
													ChamadaEncalhe chamadaEncalhe) {
		
		for (ChamadaEncalhe chamadaEncalheLista : listaChamadaEncalhe) {
			
			if (chamadaEncalhe.getDataRecolhimento().equals(chamadaEncalheLista.getDataRecolhimento())
					&& chamadaEncalhe.getProdutoEdicao().equals(chamadaEncalheLista.getProdutoEdicao())) {
				
				return chamadaEncalheLista;
			}
		}
		
		return null;
	}
	
	/**
	 * Remove as chamadas de encalhe do período informado.
	 * 
	 * @param dataInicialRecolhimento - data inicial de recolhimento
	 * @param dataFinalRecolhimento - data final de recolhimento
	 */
	private void removerChamadasEncalhe(Date dataInicialRecolhimento,
									    Date dataFinalRecolhimento) {
		
		List<ChamadaEncalhe> listaChamadaEncalhe =
			this.chamadaEncalheRepository.obterPorPeriodoTipoChamadaEncalhe(
				dataInicialRecolhimento, dataFinalRecolhimento, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		for (ChamadaEncalhe chamadaEncalhe : listaChamadaEncalhe) {
			
			this.chamadaEncalheRepository.remover(chamadaEncalhe);
		}
	}
		
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private RecolhimentoDTO obterDadosRecolhimento(Integer numeroSemana,
			 									   List<Long> listaIdsFornecedores,
			 									   TipoBalanceamentoRecolhimento tipoBalanceamento,
			 									   boolean forcarBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		PeriodoVO periodoRecolhimento = getPeriodoRecolhimento(distribuidor, numeroSemana);
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimentoFornecedor(periodoRecolhimento, listaIdsFornecedores);
		
		TreeSet<Date> datasRecolhimentoDistribuidor = 
			this.obterDatasRecolhimentoDistribuidor(distribuidor, periodoRecolhimento);
		
		dadosRecolhimento.setDatasRecolhimentoDistribuidor(datasRecolhimentoDistribuidor);
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);

		List<ProdutoRecolhimentoDTO> produtosRecolhimento = null;

		if (TipoBalanceamentoRecolhimento.EDITOR.equals(tipoBalanceamento)) {
			
			produtosRecolhimento =
				this.lancamentoRepository.obterBalanceamentoRecolhimentoPorEditorData(periodoRecolhimento, 
					 																  listaIdsFornecedores, 
																					  GrupoProduto.CROMO);
		
		} else if (TipoBalanceamentoRecolhimento.AUTOMATICO.equals(tipoBalanceamento)
						|| TipoBalanceamentoRecolhimento.VALOR.equals(tipoBalanceamento)) {
			
			produtosRecolhimento =
				this.lancamentoRepository.obterBalanceamentoRecolhimento(periodoRecolhimento, 
																		 listaIdsFornecedores, 
																		 GrupoProduto.CROMO);
		}

		TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria =
			this.lancamentoRepository.obterExpectativasEncalhePorData(periodoRecolhimento, 
																	  listaIdsFornecedores, 
																	  GrupoProduto.CROMO);

		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		dadosRecolhimento.setMapaExpectativaEncalheTotalDiaria(mapaExpectativaEncalheTotalDiaria);
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(distribuidor.getCapacidadeRecolhimento());
		
		boolean matrizFechada = isMatrizFechada(distribuidor.getDataOperacao(), periodoRecolhimento);
		
		dadosRecolhimento.setBalancearMatriz(
			balancearMatriz(produtosRecolhimento, distribuidor.getDataOperacao(),
							periodoRecolhimento, matrizFechada, forcarBalanceamento));
		
		dadosRecolhimento.setMatrizFechada(matrizFechada);
		
		return dadosRecolhimento;
	}
	
	/**
	 * Retorna uma flag que indica se a matriz deve ou não ser balanceada.
	 * 
	 * @param produtosRecolhimento - lista de produtos de recolhimento
	 * @param dataOperacao - data de operação
	 * @param periodoRecolhimento - período de recolhimento
	 * @param matrizFechada - flag que indica se a matriz está fechada
	 * @param forcarBalanceamento - flag que indica se deve ser forçado o balanceamento da matriz
	 * 
	 * @return boolean
	 */
	private boolean balancearMatriz(List<ProdutoRecolhimentoDTO> produtosRecolhimento,
									Date dataOperacao,
									PeriodoVO periodoRecolhimento,
									boolean matrizFechada,
									boolean forcarBalanceamento) {
		
		if (matrizFechada) {
			
			return false;
		}
		
		if (!isMatrizSalva(produtosRecolhimento)) {
			
			return true;
		}
		
		return forcarBalanceamento;
	}
	
	/**
	 * Retorna uma flag que indica se a matriz está salva.
	 * 
	 * @param produtosRecolhimento - lista de produtos de recolhimento
	 * 
	 * @return boolean
	 */
	private boolean isMatrizSalva(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		boolean matrizSalva = false;
		
		if (produtosRecolhimento != null && !produtosRecolhimento.isEmpty()) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = produtosRecolhimento.get(0);
			
			if (produtoRecolhimento != null) {
			
				matrizSalva =
					StatusLancamento.BALANCEADO_RECOLHIMENTO
						.equals(produtoRecolhimento.getStatusLancamento());
			}
		}
		
		return matrizSalva;
	}
	
	/**
	 * Retorna uma flag que indica se a matriz está fechada.
	 * 
	 * @param dataOperacao - data de operação
	 * @param periodoRecolhimento - período de recolhimento
	 * 
	 * @return boolean
	 */
	private boolean isMatrizFechada(Date dataOperacao, PeriodoVO periodoRecolhimento) {
		
		boolean matrizConfirmada = 
			this.lancamentoRepository
				.verificarExistenciaChamadaEncalheMatrizRecolhimento(periodoRecolhimento);
		
		boolean semanaRecolhimento = DateUtil.validarDataEntrePeriodo(dataOperacao,
																	  periodoRecolhimento.getDataInicial(),
																	  periodoRecolhimento.getDataFinal());
		
		return matrizConfirmada || semanaRecolhimento;
	}

	/**
	 * Monta o perídodo de recolhimento de acordo com a semana informada.
	 */
	private PeriodoVO getPeriodoRecolhimento(Distribuidor distribuidor, Integer numeroSemana) {
		
		Date dataInicioSemana = 
			DateUtil.obterDataDaSemanaNoAno(numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		PeriodoVO periodoRecolhimento = new PeriodoVO(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
	}
	
	/**
	 * Obtém as datas de recolhimento dos fornecedores informados.
	 */
	private TreeSet<Date> obterDatasRecolhimentoFornecedor(PeriodoVO periodoRecolhimento,
														   List<Long> listaIdsFornecedores) {
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.RECOLHIMENTO);
		
		if (listaDistribuicaoFornecedor == null || listaDistribuicaoFornecedor.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING ,
										 "Dias de recolhimento para os fornecedores não encontrados!");
		}
		
		Set<Integer> diasSemanaFornecedor = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			
			diasSemanaFornecedor.add(distribuicaoFornecedor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimento(periodoRecolhimento, diasSemanaFornecedor);
		
		return datasRecolhimentoFornecedor;
	}
	
	/**
	 * Obtém as datas de recolhimento do distribuídor.
	 */
	private TreeSet<Date> obterDatasRecolhimentoDistribuidor(Distribuidor distribuidor,
														 PeriodoVO periodoRecolhimento) {
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor = 
			this.distribuidorRepository.buscarDiasDistribuicaoDistribuidor(
				distribuidor.getId(), OperacaoDistribuidor.RECOLHIMENTO);
		
		Set<Integer> diasSemanaDistribuidor = new TreeSet<Integer>();
		
		for (DistribuicaoDistribuidor distribuicaoDistribuidor : listaDistribuicaoDistribuidor) {
			
			diasSemanaDistribuidor.add(distribuicaoDistribuidor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasRecolhimentoDistribuidor = 
			this.obterDatasRecolhimento(periodoRecolhimento, diasSemanaDistribuidor);
		
		return datasRecolhimentoDistribuidor;
	}
	
	/**
	 * Obtém as datas para recolhimento no período informado,
	 * de acordo com os dias da semana informados.
	 */
	private TreeSet<Date> obterDatasRecolhimento(PeriodoVO periodoRecolhimento,
											 	 Set<Integer> diasRecolhimentoSemana) {
		
		TreeSet<Date> datasRecolhimento =
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(
				periodoRecolhimento.getDataInicial(),  
					periodoRecolhimento.getDataFinal(), diasRecolhimentoSemana);
		
		return datasRecolhimento;
	}
	
}
