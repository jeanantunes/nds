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
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
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
	@Transactional(readOnly = true)
	public List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodoBalanceamento(Date dataInicial, 
																			   List<Long> listaIdsFornecedores) {

		Date dataFinal = DateUtil.adicionarDias(dataInicial, 6);
		
		List<DistribuicaoFornecedor> recolhimentos = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.RECOLHIMENTO);
		
		Set<Integer> listaCodigosDiasSemana = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor recolhimento : recolhimentos) {
			
			listaCodigosDiasSemana.add(recolhimento.getDiaSemana().getCodigoDiaSemana());
		}

		List<Date> periodoRecolhimento = 
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(dataInicial, dataFinal, listaCodigosDiasSemana);
		
		return this.lancamentoRepository.buscarResumosPeriodoRecolhimento(
				periodoRecolhimento, listaIdsFornecedores, GrupoProduto.CROMO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<RecolhimentoDTO> obterDadosBalanceamentoRecolhimento(Date dataInicial) {

		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
																 List<Long> listaIdsFornecedores,
																 TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento) {
		
		RecolhimentoDTO dadosRecolhimento =
			obterDadosRecolhimento(numeroSemana, listaIdsFornecedores, tipoBalanceamentoRecolhimento);
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(tipoBalanceamentoRecolhimento);
		
		return balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 * 
	 * @param numeroSemana - número da semana 
	 * @param listaIdsFornecedores - lista de identificadores dos fornecedores
	 * @param tipoBalanceamento - tipo de balanceamento
	 * 
	 * @return {@link RecolhimentoDTO}
	 */
	private RecolhimentoDTO obterDadosRecolhimento(Integer numeroSemana,
			 									   List<Long> listaIdsFornecedores,
			 									   TipoBalanceamentoRecolhimento tipoBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		PeriodoVO periodoRecolhimento = getPeriodoRecolhimento(distribuidor, numeroSemana);
		
		List<Date> datasRecolhimentoFornecedor = 
			obterDatasRecolhimentoFornecedor(periodoRecolhimento, listaIdsFornecedores);
		
		List<Date> datasRecolhimentoDistribuidor = 
			obterDatasRecolhimentoDistribuidor(distribuidor, periodoRecolhimento);
		
		dadosRecolhimento.setListaDiasRecolhimentoDistribuidor(datasRecolhimentoDistribuidor);
		dadosRecolhimento.setListaDiasRecolhimentoFornecedor(datasRecolhimentoFornecedor);
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(
			distribuidor.getCapacidadeRecolhimento());
		
		return dadosRecolhimento;
	}

	/**
	 * Monta o perídodo de recolhimento de acordo com a semana informada.
	 * 
	 * @param distribuidor - distribuídor
	 * @param numeroSemana - número da semana
	 * 
	 * @return período de recolhimento
	 */
	private PeriodoVO getPeriodoRecolhimento(Distribuidor distribuidor, Integer numeroSemana) {
		
		Date dataInicioSemana = 
			DateUtil.obterDataDaSemanaNoAno(numeroSemana,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		PeriodoVO periodoRecolhimento = new PeriodoVO(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
	}
	
	/**
	 * Obtém as datas de recolhimento dos fornecedores informados.
	 * 
	 * @param periodoRecolhimento - perídos de recolhimento
	 * @param listaIdsFornecedores - lista com os identificadores dos fornecedores
	 * 
	 * @return lista de datas para recolhimento
	 */
	private List<Date> obterDatasRecolhimentoFornecedor(PeriodoVO periodoRecolhimento,
														List<Long> listaIdsFornecedores) {
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.RECOLHIMENTO);
		
		Set<Integer> diasSemanaFornecedor = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			
			diasSemanaFornecedor.add(distribuicaoFornecedor.getDiaSemana().getCodigoDiaSemana());
		}
		
		List<Date> datasRecolhimentoFornecedor = 
			obterDatasRecolhimento(periodoRecolhimento, diasSemanaFornecedor);
		
		return datasRecolhimentoFornecedor;
	}
	
	/**
	 * Obtém as datas de recolhimento do distribuídor.
	 * 
	 * @param distribuidor - distribuídor
	 * @param periodoRecolhimento - período de recolhimento
	 * 
	 * @return lista de datas para recolhimento
	 */
	private List<Date> obterDatasRecolhimentoDistribuidor(Distribuidor distribuidor,
														  PeriodoVO periodoRecolhimento) {
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor = 
			this.distribuidorRepository.buscarDiasDistribuicaoDistribuidor(
				distribuidor.getId(), OperacaoDistribuidor.RECOLHIMENTO);
		
		Set<Integer> diasSemanaDistribuidor = new TreeSet<Integer>();
		
		for (DistribuicaoDistribuidor distribuicaoDistribuidor : listaDistribuicaoDistribuidor) {
			
			diasSemanaDistribuidor.add(distribuicaoDistribuidor.getDiaSemana().getCodigoDiaSemana());
		}
		
		List<Date> datasRecolhimentoDistribuidor = 
			obterDatasRecolhimento(periodoRecolhimento, diasSemanaDistribuidor);
		
		return datasRecolhimentoDistribuidor;
	}
	
	/**
	 * Obtém as datas para recolhimento no período informado,
	 * de acordo com os dias da semana informados.
	 * 
	 * @param periodoRecolhimento - período de recolhimento
	 * @param diasRecolhimentoSemana - códigos dos dias de recolhimento da semana
	 * 
	 * @return lista de datas para recolhimento
	 */
	private List<Date> obterDatasRecolhimento(PeriodoVO periodoRecolhimento,
											  Set<Integer> diasRecolhimentoSemana) {
		
		List<Date> datasRecolhimento = 
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(periodoRecolhimento.getDataInicial(),
														 periodoRecolhimento.getDataFinal(),
														 diasRecolhimentoSemana);
		
		return datasRecolhimento;
	}
	
}
