package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.DistribuidorService;
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
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	protected ChamadaEncalheRepository chamadaEncalheRepository;
		
	@Autowired
	protected ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
																 List<Long> listaIdsFornecedores,
																 TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento) {
		
		RecolhimentoDTO dadosRecolhimento =
			this.obterDadosRecolhimento(numeroSemana, listaIdsFornecedores, tipoBalanceamentoRecolhimento);
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(tipoBalanceamentoRecolhimento);
		
		return balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void salvarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		if (matrizRecolhimento == null
				|| matrizRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaRecolhimentos =
			new HashMap<Long, ProdutoRecolhimentoDTO>();
		
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
		
		atualizarLancamento(idsLancamento, mapaRecolhimentos);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void confirmarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												   Integer numeroSemana) {
		
		if (matrizRecolhimento == null
				|| matrizRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento =
			new HashMap<Long, ProdutoRecolhimentoDTO>();
		
		Map<Long, Date> mapaEdicaoDataRecolhimento =
				new HashMap<Long, Date>();
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		Set<Long> idsProdutoEdicao = new TreeSet<Long>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
			
			if (listaProdutoRecolhimentoDTO == null
					|| listaProdutoRecolhimentoDTO.isEmpty()) {
			
				continue;
			}
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutoRecolhimentoDTO) {
				
				mapaLancamentoRecolhimento.put(produtoRecolhimento.getIdLancamento(),
											   produtoRecolhimento);
				
				idsLancamento.add(produtoRecolhimento.getIdLancamento());
				
				mapaEdicaoDataRecolhimento.put(produtoRecolhimento.getIdProdutoEdicao(),
											   produtoRecolhimento.getNovaData());
				
				idsProdutoEdicao.add(produtoRecolhimento.getIdProdutoEdicao());
			}
		}
		
		atualizarLancamento(idsLancamento, mapaLancamentoRecolhimento);
		
		gerarChamadaEncalhe(idsProdutoEdicao, mapaEdicaoDataRecolhimento, numeroSemana);
		
		//TODO: chamar componente de cadastro de lançamentos parciais
	}
	
	private void atualizarLancamento(Set<Long> idsLancamento,
									 Map<Long, ProdutoRecolhimentoDTO> mapaRecolhimentos) {
		
		if (!idsLancamento.isEmpty()) {
		
			List<Lancamento> listaLancamentos = this.lancamentoRepository.obterLancamentosPorId(idsLancamento);
			
			if (listaLancamentos == null || listaLancamentos.isEmpty()) {
				
//				throw new ValidacaoException(TipoMensagem.WARNING,
//					"Lançamento não encontrado!");
			}
			
			if (idsLancamento.size() != listaLancamentos.size()) {
				
//				throw new ValidacaoException(TipoMensagem.WARNING,
//					"Lançamento não encontrado!");
			}
			
			ProdutoRecolhimentoDTO produtoRecolhimento = null;
			
			for (Lancamento lancamento : listaLancamentos) {
				
				produtoRecolhimento = mapaRecolhimentos.get(lancamento.getId());
				
				lancamento.setDataRecolhimentoDistribuidor(produtoRecolhimento.getNovaData());
				lancamento.setSequenciaMatriz(produtoRecolhimento.getSequencia().intValue());
				lancamento.setStatus(StatusLancamento.BALANCEADO_RECOLHIMENTO);
				lancamento.setDataStatus(new Date());
				
				this.lancamentoRepository.merge(lancamento);
			}
		}
	}
	
	private void gerarChamadaEncalhe(Set<Long> idsProdutoEdicao,
			 						 Map<Long, Date> mapaEdicaoDataRecolimento,
			 						 Integer numeroSemana) {
		
		if (!idsProdutoEdicao.isEmpty()) {
			
			Distribuidor distribuidor = this.distribuidorService.obter();
			
			PeriodoVO periodoRecolhimento = getPeriodoRecolhimento(distribuidor, numeroSemana);
			
			removerChamadasEncalhe(periodoRecolhimento.getDataInicial(),
								   periodoRecolhimento.getDataFinal());
			
			List<EstoqueProdutoCota> listaEstoqueProdutoCota =
				this.estoqueProdutoCotaRepository.buscarEstoquesProdutoCotaPorIdProdutEdicao(
					idsProdutoEdicao);
			
			if (listaEstoqueProdutoCota == null || listaEstoqueProdutoCota.isEmpty()) {
				
//				throw new ValidacaoException(TipoMensagem.WARNING,
//					"Estoque produto cota não encontrado!");
			}
			
			ProdutoEdicao produtoEdicao = null;
			Cota cota = null;
			Date dataRecolhimento = null;
			ChamadaEncalhe chamadaEncalhe = null;
			ChamadaEncalhe chamadaEncalheLista = null;
			ChamadaEncalheCota chamadaEncalheCota = null;			
			
			List<ChamadaEncalhe> listaChamadaEncalhe = new ArrayList<ChamadaEncalhe>();
			
			for (EstoqueProdutoCota estoqueProdutoCota : listaEstoqueProdutoCota) {
				
				produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
				
				cota = estoqueProdutoCota.getCota();
				
				dataRecolhimento = mapaEdicaoDataRecolimento.get(produtoEdicao.getId());
				
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
				chamadaEncalheCota.setConferido(false);
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
	
	private void removerChamadasEncalhe(Date dataInicialRecolhimento,
									    Date dataFinalRecolhimento) {
		
		List<ChamadaEncalhe> listaChamadaEncalhe =
			this.chamadaEncalheRepository.obterPorPeriodoTipoChamadaEncalhe(
				dataInicialRecolhimento, dataFinalRecolhimento, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		for (ChamadaEncalhe chamadaEncalhe : listaChamadaEncalhe) {
			
			this.chamadaEncalheRepository.remover(chamadaEncalhe);
		}
	}
		
	/*
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private RecolhimentoDTO obterDadosRecolhimento(Integer numeroSemana,
			 									   List<Long> listaIdsFornecedores,
			 									   TipoBalanceamentoRecolhimento tipoBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		PeriodoVO periodoRecolhimento = getPeriodoRecolhimento(distribuidor, numeroSemana);
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimentoFornecedor(periodoRecolhimento, listaIdsFornecedores);
		
		TreeSet<Date> datasRecolhimentoDistribuidor = 
			this.obterDatasRecolhimentoDistribuidor(distribuidor, periodoRecolhimento);
		
		dadosRecolhimento.setDatasRecolhimentoDistribuidor(datasRecolhimentoDistribuidor);
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);

		//TODO: popular de acordo com o tipo de balanceamento
		
//		dadosRecolhimento.setProdutosRecolhimento();
//		dadosRecolhimento.setMapaExpectativaEncalheTotalDiaria();
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(distribuidor.getCapacidadeRecolhimento());
		
		return dadosRecolhimento;
	}

	/*
	 * Monta o perídodo de recolhimento de acordo com a semana informada.
	 */
	private PeriodoVO getPeriodoRecolhimento(Distribuidor distribuidor, Integer numeroSemana) {
		
		Date dataInicioSemana = 
			DateUtil.obterDataDaSemanaNoAno(numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		PeriodoVO periodoRecolhimento = new PeriodoVO(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
	}
	
	/*
	 * Obtém as datas de recolhimento dos fornecedores informados.
	 */
	private TreeSet<Date> obterDatasRecolhimentoFornecedor(PeriodoVO periodoRecolhimento,
														   List<Long> listaIdsFornecedores) {
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.RECOLHIMENTO);
		
		Set<Integer> diasSemanaFornecedor = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			
			diasSemanaFornecedor.add(distribuicaoFornecedor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimento(periodoRecolhimento, diasSemanaFornecedor);
		
		return datasRecolhimentoFornecedor;
	}
	
	/*
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
	
	/*
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
