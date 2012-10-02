package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import br.com.abril.nds.integracao.service.DistribuidorService;
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
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;
import br.com.abril.nds.util.TipoMensagem;

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
																 boolean forcarBalanceamento,
																 Date dataBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento =
			this.obterDadosRecolhimento(
				numeroSemana, listaIdsFornecedores, tipoBalanceamentoRecolhimento,
				forcarBalanceamento, dataBalanceamento);
		
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
			
				if (!produtoRecolhimento.isBalanceamentoConfirmado()
						&& !produtoRecolhimento.isPossuiChamada()) {
				
					mapaRecolhimentos.put(produtoRecolhimento.getIdLancamento(), produtoRecolhimento);
					
					idsLancamento.add(produtoRecolhimento.getIdLancamento());
				}
			}
		}
		
		this.atualizarLancamentos(
			idsLancamento, usuario, mapaRecolhimentos,
			StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TreeMap<Date, List<ProdutoRecolhimentoDTO>> confirmarBalanceamentoRecolhimento(
											Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
											Integer numeroSemana,
											List<Date> datasConfirmadas,
											Usuario usuario) {
		
		if (matrizRecolhimento == null
				|| matrizRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento =
			new TreeMap<Long, ProdutoRecolhimentoDTO>();
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		Set<Long> idsProdutoEdicaoParcial = new TreeSet<Long>();
		
		Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos = new TreeMap<Date, Set<Long>>();
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		for (Date dataConfirmada : datasConfirmadas) {
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = matrizRecolhimento.get(dataConfirmada);
			
			if (produtosRecolhimento == null || produtosRecolhimento.isEmpty()) {
			
				continue;
			}
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				Date novaDataRecolhimento = produtoRecolhimento.getNovaData();
				
				if (produtoRecolhimento.isBalanceamentoConfirmado()) {
					
					this.montarMatrizRecolhimentosConfirmados(matrizConfirmada, produtoRecolhimento,
															null, novaDataRecolhimento);
					
					continue;
				}
				
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
		
		this.atualizarLancamentos(
			idsLancamento, usuario, mapaLancamentoRecolhimento,
			StatusLancamento.BALANCEADO_RECOLHIMENTO, matrizConfirmada);
		
		this.gerarChamadasEncalhe(mapaDataRecolhimentoLancamentos, numeroSemana);
		
		this.gerarPeriodosParciais(idsProdutoEdicaoParcial, usuario);
		
		return matrizConfirmada;
	}

	@Override
	@Transactional
	public void excluiBalanceamento(Long idLancamento) {
		
		Lancamento lancamento =  lancamentoRepository.buscarPorId(idLancamento);
		
		if(lancamento == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Lançamento não encontrado!");
		}
		
		lancamento.setStatus(StatusLancamento.EXCLUIDO_RECOLHIMENTO);
	}
	
	/**
	 * Método que atualiza as informações dos lançamentos.
	 * 
	 * @param idsLancamento - identificadores de lançamentos
	 * @param usuario - usuário
	 * @param mapaLancamentoRecolhimento - mapa de lancamentos e produtos de recolhimento
	 * @param statusLancamento - status do lançamento
	 */
	private void atualizarLancamentos(Set<Long> idsLancamento, Usuario usuario,
									  Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento,
									  StatusLancamento statusLancamento,
									  TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada) {
		
		if (!idsLancamento.isEmpty()) {
		
			List<Lancamento> listaLancamentos = this.lancamentoRepository.obterLancamentosPorIdOrdenados(idsLancamento);
			
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
					!(lancamento.getStatus().equals(statusLancamento));
				
				produtoRecolhimento = mapaLancamentoRecolhimento.get(lancamento.getId());
				
				Date novaData = produtoRecolhimento.getNovaData();
				
				lancamento.setDataRecolhimentoDistribuidor(novaData);
				lancamento.setSequenciaMatriz(produtoRecolhimento.getSequencia().intValue());
				lancamento.setStatus(statusLancamento);
				lancamento.setDataStatus(new Date());
				
				this.lancamentoRepository.merge(lancamento);
				
				this.montarMatrizRecolhimentosConfirmados(matrizConfirmada, produtoRecolhimento,
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
	}
	
	/**
	 * Monta a matriz de recolhimento com os recolhimentos confirmados.
	 * 
	 * @param matrizConfirmada - matriz de recolhimento confirmada
	 * @param produtoRecolhimento - produto de recolhimento
	 * @param lancamento - lançamento
	 * @param novaData - nova data de recolhimento
	 */
	private void montarMatrizRecolhimentosConfirmados(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada,
													  ProdutoRecolhimentoDTO produtoRecolhimento,
													  Lancamento lancamento,
													  Date novaData) {

		if (matrizConfirmada == null) {
			
			return;
		}
		
		if (lancamento != null) {
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoDistribuidor());
			produtoRecolhimento.setStatusLancamento(lancamento.getStatus().toString());
		}
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = matrizConfirmada.get(novaData);
		
		if (produtosRecolhimento == null) {
		
			produtosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
		}
		
		produtosRecolhimento.add(produtoRecolhimento);
		
		matrizConfirmada.put(novaData, produtosRecolhimento);
	}
	
	/**
	 * Gera as chamadas de encalhe para os produtos da matriz de balanceamento.
	 * 
	 * @param mapaDataRecolhimentoLancamentos - mapa de datas de recolhimento e identificadores de lancamentos.
	 * @param numeroSemana - número da semana
	 */
	private void gerarChamadasEncalhe(Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos,
			 						  Integer numeroSemana) {
		
		if (mapaDataRecolhimentoLancamentos == null || mapaDataRecolhimentoLancamentos.isEmpty()) {
		
			return;
		}
		
		for (Map.Entry<Date, Set<Long>> entry : mapaDataRecolhimentoLancamentos.entrySet()) {
		
			Set<Long> idsLancamento = entry.getValue();
			
			Date dataRecolhimento = entry.getKey();
			
			if (idsLancamento == null || idsLancamento.isEmpty()) {
				
				continue;
			}
			
			for (Long idLancamento : idsLancamento) {

				Lancamento lancamento = this.lancamentoRepository
						.buscarPorId(idLancamento);

				List<EstoqueProdutoCota> listaEstoqueProdutoCota = this.estoqueProdutoCotaRepository
						.buscarListaEstoqueProdutoCota(idLancamento);

				if (listaEstoqueProdutoCota == null	|| listaEstoqueProdutoCota.isEmpty()) {

					throw new ValidacaoException(TipoMensagem.WARNING,
							"Estoque produto cota não encontrado!");
				}

				List<ChamadaEncalhe> listaChamadaEncalhe = new ArrayList<ChamadaEncalhe>();

				for (EstoqueProdutoCota estoqueProdutoCota : listaEstoqueProdutoCota) {

					ProdutoEdicao produtoEdicao = estoqueProdutoCota
							.getProdutoEdicao();

					Cota cota = estoqueProdutoCota.getCota();

					ChamadaEncalhe chamadaEncalhe = this.obterChamadaEncalheLista(listaChamadaEncalhe,
									dataRecolhimento, produtoEdicao.getId());

					if (chamadaEncalhe == null) {

						chamadaEncalhe = this.criarChamadaEncalhe(dataRecolhimento, produtoEdicao);

						listaChamadaEncalhe.add(chamadaEncalhe);
					}
					
					Set<Lancamento> lancamentos = chamadaEncalhe.getLancamentos();
					
					if(lancamentos == null || lancamentos.isEmpty()) {
						lancamentos = new HashSet<Lancamento>();
					}
					
					lancamentos.add(lancamento);
					
					chamadaEncalhe.setLancamentos(lancamentos);
					
					chamadaEncalhe = this.chamadaEncalheRepository.merge(chamadaEncalhe);
					
					this.criarChamadaEncalheCota(estoqueProdutoCota, cota,
							chamadaEncalhe);
				}
			}
		}
	}

	/**
	 * Método que cria uma chamada de encalhe para a cota.
	 * 
	 * @param estoqueProdutoCota - estoque do produto da cota
	 * @param cota - cota
	 * @param chamadaEncalhe chamada de encalhe
	 */
	private void criarChamadaEncalheCota(EstoqueProdutoCota estoqueProdutoCota,
										 Cota cota, ChamadaEncalhe chamadaEncalhe) {
		
		ChamadaEncalheCota chamadaEncalheCota = new ChamadaEncalheCota();
		
		chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
		chamadaEncalheCota.setFechado(false);
		chamadaEncalheCota.setCota(cota);
		
		BigInteger qtdPrevista = BigInteger.ZERO;
		
		if (estoqueProdutoCota != null) {
			
			qtdPrevista = estoqueProdutoCota.getQtdeRecebida().subtract(
				estoqueProdutoCota.getQtdeDevolvida());
		}
		
		chamadaEncalheCota.setQtdePrevista(qtdPrevista);
		
		this.chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
	}

	/**
	 * Método que cria uma chamada de encalhe.
	 * 
	 * @param dataRecolhimento - data de recolhimento
	 * @param produtoEdicao - produto edição
	 * 
	 * @return chamada de encalhe
	 */
	private ChamadaEncalhe criarChamadaEncalhe(Date dataRecolhimento, ProdutoEdicao produtoEdicao) {
		
		ChamadaEncalhe chamadaEncalhe = new ChamadaEncalhe();
		
		chamadaEncalhe.setDataRecolhimento(dataRecolhimento);
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		return chamadaEncalhe;
	}
	
	/**
	 * Gera os períodos de parciais
	 * 
	 * @param idsProdutoEdicaoParcial - identificadores dos produtos de edição
	 * @param usuario - usuário
	 */
	private void gerarPeriodosParciais(Set<Long> idsProdutoEdicaoParcial, Usuario usuario) {
		
		if (idsProdutoEdicaoParcial == null || idsProdutoEdicaoParcial.isEmpty()) {
			
			return;
		}
		
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
	 * @param dataRecolhimento - 
	 * @param idProdutoEdicao - 
	 * 
	 * @return chamada de encalhe, caso já exista uma chamada na lista
	 */
	private ChamadaEncalhe obterChamadaEncalheLista(List<ChamadaEncalhe> listaChamadaEncalhe,
													Date dataRecolhimento,
													Long idProdutoEdicao) {
		
		for (ChamadaEncalhe chamadaEncalheLista : listaChamadaEncalhe) {
			
			if (dataRecolhimento.equals(chamadaEncalheLista.getDataRecolhimento())
					&& idProdutoEdicao.equals(chamadaEncalheLista.getProdutoEdicao().getId())) {
				
				return chamadaEncalheLista;
			}
		}
		
		return null;
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private RecolhimentoDTO obterDadosRecolhimento(Integer numeroSemana,
			 									   List<Long> listaIdsFornecedores,
			 									   TipoBalanceamentoRecolhimento tipoBalanceamento,
			 									   boolean forcarBalanceamento,
			 									   Date dataBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		Intervalo<Date> periodoRecolhimento =
			getPeriodoRecolhimento(distribuidor, numeroSemana, dataBalanceamento);
		
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
		
		boolean semanaRecolhimento = 
			this.isSemanaRecolhimento(distribuidor.getDataOperacao(), periodoRecolhimento);
		
		dadosRecolhimento.setSemanaRecolhimento(semanaRecolhimento);
		
		dadosRecolhimento.setConfiguracaoInicial(forcarBalanceamento);
		
		return dadosRecolhimento;
	}
	
	/**
	 * Retorna uma flag que indica se a semana atual é a semana de recolhimento.
	 * 
	 * @param dataOperacao - data de operação
	 * @param periodoRecolhimento - período de recolhimento
	 * 
	 * @return boolean
	 */
	private boolean isSemanaRecolhimento(Date dataOperacao, Intervalo<Date> periodoRecolhimento) {
		
		boolean isSemanaRecolhimento =
			DateUtil.validarDataEntrePeriodo(dataOperacao,
											 periodoRecolhimento.getDe(),
											 periodoRecolhimento.getAte());
		
		return isSemanaRecolhimento;
	}

	/**
	 * Monta o perídodo de recolhimento de acordo com a semana informada.
	 */
	private Intervalo<Date> getPeriodoRecolhimento(Distribuidor distribuidor,
												   Integer numeroSemana,
												   Date dataBalanceamento) {
		
		Date dataInicioSemana = 
			DateUtil.obterDataDaSemanaNoAno(
				numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana(), dataBalanceamento);
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
	}
	
	/**
	 * Obtém as datas de recolhimento dos fornecedores informados.
	 */
	private TreeSet<Date> obterDatasRecolhimentoFornecedor(Intervalo<Date> periodoRecolhimento,
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
															 Intervalo<Date> periodoRecolhimento) {
		
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
	private TreeSet<Date> obterDatasRecolhimento(Intervalo<Date> periodoRecolhimento,
											 	 Set<Integer> diasRecolhimentoSemana) {
		
		TreeSet<Date> datasRecolhimento =
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(
				periodoRecolhimento.getDe(), periodoRecolhimento.getAte(), diasRecolhimentoSemana);
		
		return datasRecolhimento;
	}
	
}
