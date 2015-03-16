package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.ConsultaConsignadoCotaService;

@Service
public class ConsultaConsignadoCotaServiceImpl implements ConsultaConsignadoCotaService {
	
	@Autowired
	private ConsultaConsignadoCotaRepository consignadoCotaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		return this.consignadoCotaRepository.buscarConsignadoCota(filtro, limitar);
	}

	@Override
	@Transactional
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {		
		
		filtro.setDataOperacao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
		return this.consignadoCotaRepository.buscarMovimentosCotaPeloFornecedor(filtro, limitar);
	}

	@Override
	@Transactional
	public BigDecimal buscarTotalGeralDaCota(FiltroConsultaConsignadoCotaDTO filtro) {	
		
		return this.consignadoCotaRepository.buscarTotalGeralDaCota(filtro);
	}

	@Override
	@Transactional
	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro) {
		
		if(filtro.getIdCota() != null) {
			
			return this.consignadoCotaRepository.buscarTotalDetalhadoPorCota(filtro);
		}
		
		return this.consignadoCotaRepository.buscarTotalDetalhado(filtro);
	}

}
