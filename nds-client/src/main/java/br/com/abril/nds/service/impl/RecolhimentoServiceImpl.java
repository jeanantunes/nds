package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
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
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
			
			if (listaProdutoRecolhimentoDTO == null
					|| listaProdutoRecolhimentoDTO.isEmpty()) {
			
				continue;
			}
				
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutoRecolhimentoDTO) {
			
				atualizarLancamento(produtoRecolhimento);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void confirmarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		if (matrizRecolhimento == null
				|| matrizRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
			
			if (listaProdutoRecolhimentoDTO == null
					|| listaProdutoRecolhimentoDTO.isEmpty()) {
			
				continue;
			}
				
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutoRecolhimentoDTO) {
			
				atualizarLancamento(produtoRecolhimento);
				
				List<EstoqueProdutoCota> listaEstoqueProdutoCota =
					estoqueProdutoCotaRepository.buscarEstoqueProdutoCotaPorIdProdutEdicao(
						produtoRecolhimento.getIdProdutoEdicao());
				
				if (listaEstoqueProdutoCota == null
						|| listaEstoqueProdutoCota.isEmpty()) {
					
					continue;
				}
				
				for (EstoqueProdutoCota estoqueProdutoCota : listaEstoqueProdutoCota) {
					
					gerarChamadaEncalhe(estoqueProdutoCota, produtoRecolhimento.getNovaData());
				}
			}
		}
	}
	
	private void atualizarLancamento(ProdutoRecolhimentoDTO produtoRecolhimento) {
		
		Lancamento lancamento =
			lancamentoRepository.buscarPorId(produtoRecolhimento.getIdLancamento());
		
		if (lancamento != null) {
			
			lancamento.setDataRecolhimentoDistribuidor(produtoRecolhimento.getNovaData());
			lancamento.setSequenciaMatriz(produtoRecolhimento.getSequencia().intValue());
			lancamento.setStatus(StatusLancamento.BALANCEADO_RECOLHIMENTO);
			lancamento.setDataStatus(new Date());
			
			lancamentoRepository.merge(lancamento);
		}
	}
	
	private void gerarChamadaEncalhe(EstoqueProdutoCota estoqueProdutoCota, Date dataRecolhimento) {
		
		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		
		Cota cota = estoqueProdutoCota.getCota();
		
		ChamadaEncalhe chamadaEncalhe =
			chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
				produtoEdicao, dataRecolhimento, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		if (chamadaEncalhe == null) {
			
			chamadaEncalhe = new ChamadaEncalhe();
			
			chamadaEncalhe.setDataRecolhimento(dataRecolhimento);
			chamadaEncalhe.setProdutoEdicao(produtoEdicao);
			chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
			
			chamadaEncalhe = chamadaEncalheRepository.merge(chamadaEncalhe);
		}
		
		ChamadaEncalheCota chamadaEncalheCota = new ChamadaEncalheCota();
		
		chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
		chamadaEncalheCota.setConferido(Boolean.FALSE);
		chamadaEncalheCota.setCota(cota);
		
		BigDecimal qtdPrevista = BigDecimal.ZERO;
		
		if (estoqueProdutoCota != null) {
			
			qtdPrevista = estoqueProdutoCota.getQtdeRecebida().subtract(
				estoqueProdutoCota.getQtdeDevolvida());
		}
		
		chamadaEncalheCota.setQtdePrevista(qtdPrevista);
		
		chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
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
