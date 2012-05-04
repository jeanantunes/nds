package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;
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
