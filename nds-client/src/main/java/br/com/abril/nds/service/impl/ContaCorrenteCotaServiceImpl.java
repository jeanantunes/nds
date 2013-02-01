package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.ViewContaCorrenteCotaRepository;
import br.com.abril.nds.service.ContaCorrenteCotaService;

@Service
public class ContaCorrenteCotaServiceImpl implements ContaCorrenteCotaService {

	@Autowired
	private ViewContaCorrenteCotaRepository viewContaCorrenteCotaRepository;	
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

	@Transactional(readOnly=true)
	public List<ViewContaCorrenteCota> obterListaConsolidadoPorCota(FiltroViewContaCorrenteCotaDTO filtro) {
		return viewContaCorrenteCotaRepository.getListaViewContaCorrenteCota(filtro);
		
	}


	/**
	 * @param filtro
	 * @return
	 * @see br.com.abril.nds.repository.ViewContaCorrenteCotaRepository#getQuantidadeViewContaCorrenteCota(br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO)
	 */
	@Override
	@Transactional(readOnly=true)
	public Long getQuantidadeViewContaCorrenteCota(
			FiltroViewContaCorrenteCotaDTO filtro) {
		return viewContaCorrenteCotaRepository
				.getQuantidadeViewContaCorrenteCota(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DebitoCreditoCotaDTO> consultarDebitoCreditoCota(Long idConsolidado, Date data,
			String sortorder, String sortname){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<TipoMovimentoFinanceiro> tiposDebitoCredito = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.CREDITO),
						
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.DEBITO)
		);
		
		return this.movimentoFinanceiroCotaRepository.obterCreditoDebitoCota(
				idConsolidado, data, tiposDebitoCredito, sortorder, sortname);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal consultarJurosCota(Long idConsolidado, Date data){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<TipoMovimentoFinanceiro> tiposMovimento = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.JUROS)
		);
		
		return this.movimentoFinanceiroCotaRepository.
				obterSomatorioTipoMovimentoPorConsolidado(
						idConsolidado, data, tiposMovimento);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal consultarMultaCota(Long idConsolidado, Date data){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<TipoMovimentoFinanceiro> tiposMovimento = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.MULTA)
		);
		
		return this.movimentoFinanceiroCotaRepository.
				obterSomatorioTipoMovimentoPorConsolidado(
						idConsolidado, data, tiposMovimento);
	}
}