package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.DateUtil;

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
		
		List<Integer> listaCodigosDiasSemana = new ArrayList<Integer>();
		
		for (DistribuicaoFornecedor recolhimento : recolhimentos) {
			
			listaCodigosDiasSemana.add(recolhimento.getDiaSemana().getCodigoDiaSemana());
		}

		List<Date> periodoRecolhimento = 
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(dataInicial, dataFinal, listaCodigosDiasSemana);
		
		return this.lancamentoRepository.buscarResumosPeriodoRecolhimento(
				periodoRecolhimento, listaIdsFornecedores, GrupoProduto.CROMO);
	}

}
