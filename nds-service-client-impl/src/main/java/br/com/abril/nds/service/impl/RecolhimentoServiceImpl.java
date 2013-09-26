package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.CotaOperacaoDiferenciadaDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoCota;
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
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

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
	
	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer anoNumeroSemana,
																 List<Long> listaIdsFornecedores,
																 TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
																 boolean forcarBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento =
			this.obterDadosRecolhimento(
				anoNumeroSemana, listaIdsFornecedores, tipoBalanceamentoRecolhimento,
				forcarBalanceamento);
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(tipoBalanceamentoRecolhimento);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoDTO =
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		return balanceamentoRecolhimentoDTO;
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
			
				if (!produtoRecolhimento.isBalanceamentoConfirmado()) {
				
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
			
			this.ordenarProdutosRecolhimentoPorNome(produtosRecolhimento);
			
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
					
					idsLancamentoPorData = new LinkedHashSet<Long>();
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
		
		return matrizConfirmada;
	}

	@SuppressWarnings("unchecked")
	private void ordenarProdutosRecolhimentoPorNome(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("nomeProduto"));
		
		Collections.sort(produtosRecolhimento, comparatorChain);
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
				lancamento.setStatus(statusLancamento);
				lancamento.setDataStatus(new Date());
				lancamento.setUsuario(usuario);
				
				this.lancamentoRepository.merge(lancamento);
				
				if(TipoLancamento.PARCIAL.equals(lancamento.getTipoLancamento())){
					parciaisService.atualizarReparteDoProximoLancamentoParcial(lancamento, usuario);
				}
				
				this.montarMatrizRecolhimentosConfirmados(matrizConfirmada, produtoRecolhimento,
												   		lancamento, novaData);
				
				if (gerarHistoricoLancamento) {
				
					historicoLancamento = new HistoricoLancamento();
					
					historicoLancamento.setLancamento(lancamento);
					historicoLancamento.setTipoEdicao(TipoEdicao.ALTERACAO);
					historicoLancamento.setStatusNovo(lancamento.getStatus());
					historicoLancamento.setDataEdicao(new Date());
					historicoLancamento.setResponsavel(usuario);
					
					//TODO: geração de historico desativada devido a criação de trigger para realizar essa geração.
					//this.historicoLancamentoRepository.merge(historicoLancamento);
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
			
			Integer sequencia = this.chamadaEncalheRepository.obterMaiorSequenciaPorDia(dataRecolhimento);
			
			for (Long idLancamento : idsLancamento) {

				Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);

				List<EstoqueProdutoCota> listaEstoqueProdutoCota =
					this.estoqueProdutoCotaRepository.buscarListaEstoqueProdutoCota(idLancamento);

				if (listaEstoqueProdutoCota == null	|| listaEstoqueProdutoCota.isEmpty()) {

					throw new ValidacaoException(TipoMensagem.WARNING,
							"Estoque produto cota não encontrado!");
				}

				List<ChamadaEncalhe> listaChamadaEncalhe = new ArrayList<ChamadaEncalhe>();

				for (EstoqueProdutoCota estoqueProdutoCota : listaEstoqueProdutoCota) {

					boolean indNovaChamadaEncalhe = false;
					
					ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

					Cota cota = estoqueProdutoCota.getCota();

					Boolean existeChamadaEncalheCota = 
						this.chamadaEncalheCotaRepository.existeChamadaEncalheCota(
							cota.getId(), produtoEdicao.getId(), null, null);
					
					if (existeChamadaEncalheCota) {
						
						continue;
					}
					
					ChamadaEncalhe chamadaEncalhe =
						this.obterChamadaEncalheLista(
							listaChamadaEncalhe, dataRecolhimento, produtoEdicao.getId());

					indNovaChamadaEncalhe = (chamadaEncalhe == null);
					
					if (indNovaChamadaEncalhe) {
						chamadaEncalhe =
							this.criarChamadaEncalhe(dataRecolhimento, produtoEdicao, ++sequencia);
					}
					
					Set<Lancamento> lancamentos = chamadaEncalhe.getLancamentos();
					
					if(lancamentos == null || lancamentos.isEmpty()) {
						lancamentos = new HashSet<Lancamento>();
					}
					
					lancamentos.add(lancamento);
					
					chamadaEncalhe.setLancamentos(lancamentos);
					
					chamadaEncalhe = this.chamadaEncalheRepository.merge(chamadaEncalhe);
					
					if (indNovaChamadaEncalhe) {
						listaChamadaEncalhe.add(chamadaEncalhe);
					}
					
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
	 * @param sequencia
	 * 
	 * @return chamada de encalhe
	 */
	private ChamadaEncalhe criarChamadaEncalhe(Date dataRecolhimento, ProdutoEdicao produtoEdicao, Integer sequencia) {
		
		ChamadaEncalhe chamadaEncalhe = new ChamadaEncalhe();
		
		chamadaEncalhe.setDataRecolhimento(dataRecolhimento);
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		chamadaEncalhe.setSequencia(sequencia);
		
		return chamadaEncalhe;
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
	private RecolhimentoDTO obterDadosRecolhimento(Integer anoNumeroSemana,
			 									   List<Long> listaIdsFornecedores,
			 									   TipoBalanceamentoRecolhimento tipoBalanceamento,
			 									   boolean forcarBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		Intervalo<Date> periodoRecolhimento =
			getPeriodoRecolhimento(anoNumeroSemana);
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimentoFornecedor(periodoRecolhimento, listaIdsFornecedores);
		
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
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(
				this.distribuidorRepository.capacidadeRecolhimento());
		
		dadosRecolhimento.setForcarBalanceamento(forcarBalanceamento);
		
		if (!produtosRecolhimento.isEmpty()) {
		
			this.obterCotasOperacaoDiferenciada(
				periodoRecolhimento, produtosRecolhimento, dadosRecolhimento);
		}
		
		this.atualizarEncalheSedeAtendidaDosProdutos(
			produtosRecolhimento, dadosRecolhimento.getCotasOperacaoDiferenciada());
		
		return dadosRecolhimento;
	}

	/**
	 * Monta o perídodo de recolhimento de acordo com a semana informada.
	 */
	@Override
	public Intervalo<Date> getPeriodoRecolhimento(Integer anoNumeroSemana) {
		
		int codigoInicioSemana = 
				this.distribuidorRepository.buscarInicioSemana().getCodigoDiaSemana();
		
		Integer anoBase = SemanaUtil.getAno(anoNumeroSemana);
		
		Integer numeroSemana = SemanaUtil.getSemana(anoNumeroSemana);
		
		Date dataInicioSemana = 
			SemanaUtil.obterDataDaSemanaNoAno(
				numeroSemana,
				codigoInicioSemana, 
				anoBase);
		
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
	 * Obtém as datas para recolhimento no período informado,
	 * de acordo com os dias da semana informados.
	 */
	private TreeSet<Date> obterDatasRecolhimento(Intervalo<Date> periodoRecolhimento,
											 	 Set<Integer> diasRecolhimentoSemana) {
		
		TreeSet<Date> datasRecolhimento =
			SemanaUtil.obterPeriodoDeAcordoComDiasDaSemana(
				periodoRecolhimento.getDe(), periodoRecolhimento.getAte(), diasRecolhimentoSemana);
		
		TreeSet<Date> datasRecolhimentoComOperacao = new TreeSet<>();
		
		for (Date data : datasRecolhimento) {
			
			try {
				
				this.verificaDataOperacao(data);
				
				datasRecolhimentoComOperacao.add(data);
				
			} catch (ValidacaoException e) {
				
				continue;
			}
		}
		
		return datasRecolhimentoComOperacao;
	}

	@Override
	@Transactional
	public void voltarConfiguracaoOriginal(Integer numeroSemana, List<Long> fornecedores, Usuario usuario) {
		
		Intervalo<Date> periodoRecolhimento =
			getPeriodoRecolhimento(numeroSemana);
		
		List<Lancamento> lancamentos =  lancamentoRepository.obterLancamentosARecolherNaSemana(periodoRecolhimento, fornecedores);
			
		for(Lancamento lancamento: lancamentos) {
			
			lancamento.setStatus(StatusLancamento.EXPEDIDO);
			lancamento.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoPrevista());
			
			lancamento.setUsuario(usuario);
			
			lancamentoRepository.alterar(lancamento);
		}
	}
	
	public void verificaDataOperacao(Date data) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
	
		if (this.calendarioService.isFeriadoSemOperacao(data)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de recolhimento deve ser uma data em que o distribuidor realiza operação!");
		}
		
		if (this.calendarioService.isFeriadoMunicipalSemOperacao(data)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de recolhimento deve ser uma data em que o distribuidor realiza operação!");
		}
	}
	
	@Transactional
	@Override
	public void processarProdutosProximaSemanaRecolhimento(List<ProdutoRecolhimentoDTO> produtos, Integer numeroSemana){
		
		if(produtos == null || produtos.isEmpty()){
			return;
		}
		
		for(ProdutoRecolhimentoDTO item : produtos){
			
			Date dataRecolhimento = this.obterDataValidaRecolhimento(numeroSemana, item.getIdFornecedor());
			
			lancamentoRepository.atualizarDataRecolhimentoDistribuidor(dataRecolhimento, item.getIdLancamento());
		}		
	}
	
	private Date obterDataValidaRecolhimento(Integer numeroSemana, Long idFornecedor){
		
		Intervalo<Date> periodoRecolhimento = getPeriodoRecolhimento(++numeroSemana);
		
		Date dataRecolhimento = periodoRecolhimento.getDe();
		
		Date dataValida = null;
		
		while(dataRecolhimento.compareTo(periodoRecolhimento.getAte())<=0){
			
			if(!lancamentoRepository.existeRecolhimentoNaoBalanceado(dataRecolhimento)
					&& this.validarDiaRecolhimentoFornecedor(idFornecedor, dataRecolhimento)){
				dataValida = dataRecolhimento;
				break;
			}
			
			dataRecolhimento = DateUtil.adicionarDias(dataRecolhimento, 1);
		}		
				
		if(dataValida == null){
			dataValida = obterDataValidaRecolhimento(numeroSemana, idFornecedor);
		}
		
		return dataValida;
	}

	private boolean validarDiaRecolhimentoFornecedor(Long idFornecedor, Date dataRecolhimento) {
		
		Fornecedor fornecedor = this.fornecedorService.obterPorId(idFornecedor);
		
		List<Integer> diasRecolhimentoFornecedor = 
				this.distribuicaoFornecedorService.obterCodigosDiaDistribuicaoFornecedor(
						fornecedor.getId(), OperacaoDistribuidor.RECOLHIMENTO);
		
		int codigoDiaCorrente = SemanaUtil.obterDiaDaSemana(dataRecolhimento);
		
		return diasRecolhimentoFornecedor.contains(codigoDiaCorrente);
	}
	
	private void obterCotasOperacaoDiferenciada(Intervalo<Date> periodoRecolhimento,
											    List<ProdutoRecolhimentoDTO> produtosRecolhimento,
											    RecolhimentoDTO dadosRecolhimento) {

		Map<Long, List<Date>> mapCotasDiasRecolhimento =
			this.obterCotasOperacaoDiferenciada(periodoRecolhimento);

		if (mapCotasDiasRecolhimento.isEmpty()) {
			
			return;
		}
		
		Set<Long> idsLancamento = this.obterIdsLancamento(produtosRecolhimento);

		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada =
			this.lancamentoRepository.obterLancamentosEncalhesPorCota(
				mapCotasDiasRecolhimento.keySet(), idsLancamento);

		this.setDatasRecolhimentoCotasOperaDif(mapCotasDiasRecolhimento, cotasOperacaoDiferenciada);

		dadosRecolhimento.setCotasOperacaoDiferenciada(cotasOperacaoDiferenciada);
	}
	
	private Map<Long, List<Date>> obterCotasOperacaoDiferenciada(Intervalo<Date> periodoRecolhimento) {
		
		Map<Long, List<Date>> mapCotasDiasRecolhimento = new HashMap<>();
		
		List<GrupoCota> gruposCota = this.grupoRepository.obterTodosGrupos();
		
		for (GrupoCota grupoCota : gruposCota) {
			
			Set<Cota> cotas = grupoCota.getCotas();
			Set<String> municipios = grupoCota.getMunicipios();
			Set<DiaSemana> diasRecolhimento = grupoCota.getDiasRecolhimento();
			
			if (cotas != null && !cotas.isEmpty()) {
			
				this.montarMapOperacaoDiferenciadaCotas(
					cotas, diasRecolhimento, periodoRecolhimento, mapCotasDiasRecolhimento);
				
			} else if (municipios != null && !municipios.isEmpty()) {
				
				this.montarMapOperacaoDiferenciadaMunicipios(
					municipios, diasRecolhimento, periodoRecolhimento, mapCotasDiasRecolhimento);
			}
		}
		
		return mapCotasDiasRecolhimento;
	}

	private void montarMapOperacaoDiferenciadaCotas(Set<Cota> cotas,
													Set<DiaSemana> diasRecolhimento,
													Intervalo<Date> periodoRecolhimento,
													Map<Long, List<Date>> mapCotasDiasRecolhimento) {
		
		List<Date> datasRecolhimento = 
			this.obterDatasRecolhimentoGrupo(diasRecolhimento, periodoRecolhimento);
		
		for (Cota cota : cotas) {
			
			mapCotasDiasRecolhimento.put(cota.getId(), datasRecolhimento);
		}
	}
	
	private void montarMapOperacaoDiferenciadaMunicipios(Set<String> municipios,
														 Set<DiaSemana> diasRecolhimento,
														 Intervalo<Date> periodoRecolhimento,
														 Map<Long, List<Date>> mapCotasDiasRecolhimento) {
		
		List<Date> datasRecolhimento = 
			this.obterDatasRecolhimentoGrupo(diasRecolhimento, periodoRecolhimento);
		
		for (String municipio : municipios) {
			
			List<Long> idsCotas = this.cotaRepository.obterIdsCotasPorMunicipio(municipio);
			
			for (Long idCota : idsCotas) {
				
				mapCotasDiasRecolhimento.put(idCota, datasRecolhimento);
			}
		}
	}

	private List<Date> obterDatasRecolhimentoGrupo(Set<DiaSemana> diasRecolhimento,
											   	   Intervalo<Date> periodoRecolhimento) {
		
		List<Date> datasRecolhimentoGrupo = new ArrayList<>();
		
		List<Calendar> datasDaSemana =
			this.getDatasDaSemana(periodoRecolhimento);
		
		Set<Integer> diasRecolhimentoNaSemana = this.getDiasRecolhimentoNaSemana(diasRecolhimento);
		
		for (Calendar dataRecolhimento : datasDaSemana) {
			
			if (diasRecolhimentoNaSemana.contains(SemanaUtil.obterDiaDaSemana(dataRecolhimento))) {
				
				datasRecolhimentoGrupo.add(dataRecolhimento.getTime());
			}
		}
		
		return datasRecolhimentoGrupo;
	}

	private Set<Integer> getDiasRecolhimentoNaSemana(Set<DiaSemana> diasRecolhimento) {
		
		Set<Integer> diasRecolhimentoNaSemana = new HashSet<>();
		
		for (DiaSemana diaSemana : diasRecolhimento) {
			
			diasRecolhimentoNaSemana.add(diaSemana.getCodigoDiaSemana());
		}
		
		return diasRecolhimentoNaSemana;
	}

	private List<Calendar> getDatasDaSemana(Intervalo<Date> periodoRecolhimento) {
		
		List<Calendar> datasDaSemana = new ArrayList<>();
		
		Calendar dataInicio = DateUtil.toCalendar(periodoRecolhimento.getDe());
		Calendar dataFim = DateUtil.toCalendar(periodoRecolhimento.getAte());
		
		Calendar dataPeriodoRecolhimento = dataInicio;
		
		while(dataPeriodoRecolhimento.compareTo(dataInicio) >= 0
				&& dataPeriodoRecolhimento.compareTo(dataFim) <= 0) {
			
			datasDaSemana.add(dataPeriodoRecolhimento);
			
			dataPeriodoRecolhimento = DateUtil.adicionarDias(dataPeriodoRecolhimento, 1);
		}
		
		return datasDaSemana;
	}
	
	private Set<Long> obterIdsLancamento(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		Set<Long> idsLancamento = new HashSet<>();
		
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : produtosRecolhimento) {
			
			idsLancamento.add(produtoRecolhimentoDTO.getIdLancamento());
		}
		
		return idsLancamento;
	}
	
	private void setDatasRecolhimentoCotasOperaDif(Map<Long, List<Date>> mapCotasDiasRecolhimento,
												   List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {

		List<Date> datasRecolhimento = null;
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciadaDTO : cotasOperacaoDiferenciada) {

			datasRecolhimento = mapCotasDiasRecolhimento.get(cotaOperacaoDiferenciadaDTO.getIdCota());

			cotaOperacaoDiferenciadaDTO.setDatasRecolhimento(datasRecolhimento);
		}
	}
	
	public void montarMapasOperacaoDiferenciada(Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifAdicionar,
												Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifRemover,
												TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		
		if (cotasOperacaoDiferenciada == null || cotasOperacaoDiferenciada.isEmpty()) {
			
			return;
		}

		if (matrizRecolhimento == null) {
			
			return;
		}

		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = entry.getValue();
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento =
					this.obterCotasOperacaoDiferenciadaPorLancamento(
						cotasOperacaoDiferenciada, produtoRecolhimento.getIdLancamento());
				
				if (!cotasOperacaoDiferenciadaDoLancamento.isEmpty()) {
					
					this.montarMapsOperacaoDiferenciada(
						cotasOperacaoDiferenciadaDoLancamento, mapOperacaoDifAdicionar,
						mapOperacaoDifRemover, dataRecolhimento);
				}
			}
		}
	}
	
	private void atualizarEncalheSedeAtendidaDosProdutos(
								List<ProdutoRecolhimentoDTO> produtosRecolhimento,
								List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		
		if (cotasOperacaoDiferenciada == null || cotasOperacaoDiferenciada.isEmpty()) {
			
			return;
		}

		if (produtosRecolhimento == null || produtosRecolhimento.isEmpty()) {
			
			return;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento =
				this.obterCotasOperacaoDiferenciadaPorLancamento(
					cotasOperacaoDiferenciada, produtoRecolhimento.getIdLancamento());
			
			this.atualizarEncalheSedeAtendida(
				produtoRecolhimento, cotasOperacaoDiferenciadaDoLancamento);
		}
	}
	
	private void atualizarEncalheSedeAtendida(
						   ProdutoRecolhimentoDTO produtoRecolhimento,
						   List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento) {
		
		BigDecimal expectativaEncalheAtendida = BigDecimal.ZERO;
		BigDecimal expectativaEncalheSede = produtoRecolhimento.getExpectativaEncalhe();
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciadaDoLancamento) {
			
			expectativaEncalheAtendida = 
				expectativaEncalheAtendida.add(cotaOperacaoDiferenciada.getExpectativaEncalhe());
			
			expectativaEncalheSede =
				expectativaEncalheSede.subtract(cotaOperacaoDiferenciada.getExpectativaEncalhe());
		}
		
		produtoRecolhimento.setExpectativaEncalheAtendida(expectativaEncalheAtendida);
		produtoRecolhimento.setExpectativaEncalheSede(expectativaEncalheSede);
	}
	
	private List<CotaOperacaoDiferenciadaDTO> obterCotasOperacaoDiferenciadaPorLancamento(
										List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada,
										Long idLancamento) {
		
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento = new ArrayList<>();
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciada) {
			
			if (cotaOperacaoDiferenciada.getIdLancamento().equals(idLancamento)) {
				
				cotasOperacaoDiferenciadaDoLancamento.add(cotaOperacaoDiferenciada);
			}
		}
		
		return cotasOperacaoDiferenciadaDoLancamento;
	}
	
	private void montarMapsOperacaoDiferenciada(List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento,
												Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifAdicionar,
												Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifRemover,
												Date dataRecolhimento) {
		
		List<Date> datasRecolhimento = null;
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciadaDoLancamento) {
			
			datasRecolhimento = cotaOperacaoDiferenciada.getDatasRecolhimento();
			
			if (!datasRecolhimento.contains(dataRecolhimento)) {
				
				Date dataRecolhimentoEscolhida =
					this.obterDataRecolhimentoOperacaoDiferenciada(datasRecolhimento, dataRecolhimento);
				
				this.addMap(mapOperacaoDifAdicionar, dataRecolhimentoEscolhida, cotaOperacaoDiferenciada);
				
				this.addMap(mapOperacaoDifRemover, dataRecolhimento, cotaOperacaoDiferenciada);
			}
		}
	}

	private void addMap(Map<Date, List<CotaOperacaoDiferenciadaDTO>> map,
						Date dataRecolhimento,
						CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada) {
		
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada = map.get(dataRecolhimento);
		
		if (cotasOperacaoDiferenciada == null) {
			
			cotasOperacaoDiferenciada = new ArrayList<>();
		}
		
		cotasOperacaoDiferenciada.add(cotaOperacaoDiferenciada);
		
		map.put(dataRecolhimento, cotasOperacaoDiferenciada);
	}
	
	/*
	 * Obtém uma data de recolhimento de acordo com a datas de recolhimento da cota.
	 * Primeiro tenta obter uma data maior que a data de recolhimento do lançamento.
	 * Se não houver nenhuma pega a primeira data da cota, mesmo não sendo mais que a data do lançamento.
	 */
	private Date obterDataRecolhimentoOperacaoDiferenciada(List<Date> datasRecolhimento,
														   Date dataRecolhimento) {
		
		for (Date dataRecolhimentoOperacaoDiferenciada : datasRecolhimento) {
			
			if (dataRecolhimentoOperacaoDiferenciada.after(dataRecolhimento)) {
				
				return dataRecolhimentoOperacaoDiferenciada;
			}
		}
		
		return datasRecolhimento.get(0);
	}
	
}
