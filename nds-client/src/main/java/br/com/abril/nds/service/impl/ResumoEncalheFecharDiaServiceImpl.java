package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;


@Service
public class ResumoEncalheFecharDiaServiceImpl implements ResumoEncalheFecharDiaService {
	
	@Autowired
	private ResumoEncalheFecharDiaRepository resumoEncalheFecharDiaRepository;
	
	@Autowired
	private ResumoReparteFecharDiaService resumoFecharDiaService;

	@Override
	@Transactional
	public BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada) {		 
		return this.resumoEncalheFecharDiaRepository.obterValorEncalheFisico(dataOperacao,juramentada);
	}

	@Override
	@Transactional
	public BigDecimal obterValorEncalheLogico(Date dataOperacao) {		 
		return this.resumoEncalheFecharDiaRepository.obterValorEncalheLogico(dataOperacao);
	}

	@Override
	@Transactional
	public ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao) {

		ResumoEncalheFecharDiaDTO dto = new ResumoEncalheFecharDiaDTO();
		
		BigDecimal totalLogico = this.obterValorEncalheLogico(dataOperacao);
		dto.setTotalLogico(totalLogico);
		
		BigDecimal totalFisico = this.obterValorEncalheFisico(dataOperacao, false);
		dto.setTotalFisico(totalFisico);		
		
		BigDecimal totalJuramentado = this.obterValorEncalheFisico(dataOperacao, true);
		dto.setTotalJuramentado(totalJuramentado);
		
		List<ReparteFecharDiaDTO> lista = this.resumoFecharDiaService.obterValorReparte(dataOperacao, true);
		BigDecimal venda = lista.get(0).getValorTotalReparte().subtract(totalFisico);
		dto.setVenda(venda);
		
		BigDecimal totalFaltas = this.obterValorFaltas(dataOperacao);
		dto.setTotalFaltas(totalFaltas);
		
		BigDecimal totalSobras = this.obterValorSobras(dataOperacao);
		dto.setTotalSobras(totalSobras);
		
		BigDecimal saldo = totalLogico.subtract(totalFisico).subtract(totalJuramentado).subtract(venda).add(totalSobras).subtract(totalFaltas);
		dto.setSaldo(saldo);
		 
		return dto;
	}

	@Override
	@Transactional
	public BigDecimal obterValorFaltas(Date dataOperacao) {		 
		return this.resumoEncalheFecharDiaRepository.obterValorFaltasOuSobras(dataOperacao, StatusAprovacao.PERDA);
	}
	
	@Override
	@Transactional
	public BigDecimal obterValorSobras(Date dataOperacao) {		 
		return this.resumoEncalheFecharDiaRepository.obterValorFaltasOuSobras(dataOperacao, StatusAprovacao.GANHO);
	}

	@Override
	@Transactional
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacao) {
		return this.resumoEncalheFecharDiaRepository.obterDadosGridEncalhe(dataOperacao);
	}

}
