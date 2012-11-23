package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;

@Service
public class ResumoSuplementarFecharDiaServiceImp implements
		ResumoSuplementarFecharDiaService {
	
	@Autowired
	private ResumoSuplementarFecharDiaRepository resumoSuplementarFecharDiaRepository;

	@Override
	@Transactional
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorEstoqueLogico(dataOperacao);
	}

	@Override
	@Transactional
	public BigDecimal obterValorTransferencia(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorTransferencia(dataOperacao);
	}

	@Override
	@Transactional
	public BigDecimal obterValorVenda(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorVenda(dataOperacao);
	}

	@Override
	@Transactional
	public BigDecimal obterValorFisico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorFisico(dataOperacao);
	}

	@Override
	@Transactional
	public List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterVendasSuplementar(dataOperacao);
		
	}

	@Override
	@Transactional
	public ResumoSuplementarFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacional) {
		 
		ResumoSuplementarFecharDiaDTO dto = new ResumoSuplementarFecharDiaDTO();
		List<BigDecimal> listaDeSuplementares = new ArrayList<BigDecimal>();
		
		BigDecimal totalEstoqueLogico = this.obterValorEstoqueLogico(dataOperacional);
		dto.setTotalEstoqueLogico(totalEstoqueLogico);
		
		BigDecimal totalTransferencia = this.obterValorTransferencia(dataOperacional);
		dto.setTotalTransferencia(totalTransferencia);		
		
		BigDecimal totalVenda = this.obterValorVenda(dataOperacional);
		dto.setTotalVenda(totalVenda);
		listaDeSuplementares.add(totalVenda);
		
		BigDecimal totalFisico = this.obterValorFisico(dataOperacional);
		dto.setSaldo(totalEstoqueLogico.subtract(totalFisico));
		
		return dto;
	}

	@Override
	@Transactional
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar() {
		return this.resumoSuplementarFecharDiaRepository.obterDadosGridSuplementar();
	}

}
