package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.DateUtil;
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
			this.distribuidorRepository.buscarDiasDistribuicao(
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
	public Map<Date, List<RecolhimentoDTO>> obterMatrizBalanceamento(Integer numeroSemana,
																	 List<Long> listaIdsFornecedores) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		Date dataInicioSemana = 
			DateUtil.obterDataDaSemanaNoAno(numeroSemana,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		List<DistribuicaoFornecedor> recolhimentos = 
			this.distribuidorRepository.buscarDiasDistribuicao(
				listaIdsFornecedores, OperacaoDistribuidor.RECOLHIMENTO);
		
		Set<Integer> listaCodigosDiasSemana = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor recolhimento : recolhimentos) {
			
			listaCodigosDiasSemana.add(recolhimento.getDiaSemana().getCodigoDiaSemana());
		}
		
		List<Date> datasRecolhimento = 
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(dataInicioSemana, dataFimSemana,
														 listaCodigosDiasSemana);
		
		PeriodoVO periodoRecolhimento = new PeriodoVO(dataInicioSemana, dataFimSemana);
		
		return null;
	}
	
}
