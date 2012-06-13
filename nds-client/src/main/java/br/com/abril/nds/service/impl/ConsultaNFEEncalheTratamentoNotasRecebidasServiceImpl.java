package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaNFEEncalheTratamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.repository.ConsultaNFEEncalheTratamentoNotasRecebidasRepository;
import br.com.abril.nds.service.ConsultaNFEEncalheTratamentoNotasRecebidasService;

@Service
public class ConsultaNFEEncalheTratamentoNotasRecebidasServiceImpl implements
		ConsultaNFEEncalheTratamentoNotasRecebidasService {
	
	@Autowired
	private ConsultaNFEEncalheTratamentoNotasRecebidasRepository consultaNFEEncalheTratamentoNotasRecebidasRepository;

	@Override
	@Transactional
	public List<ConsultaNFEEncalheTratamentoDTO> buscarNFNotasRecebidas(
			FiltroConsultaNFEEncalheTratamento filtro, String limitar) {		 
		return this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarNFNotasRecebidas(filtro, limitar);
	}

	@Override
	@Transactional
	public Integer buscarTodasNFENotasRecebidas(
			FiltroConsultaNFEEncalheTratamento filtro) {
		return this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarTotalNotasRecebidas(filtro);
	}

	@Override
	@Transactional
	public List<ConsultaNFEEncalheTratamentoDTO> buscarNFNotasPendentes(
			FiltroConsultaNFEEncalheTratamento filtro, String limitar) {
		return this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarNFNotasPendentes(filtro, limitar);
	}

}
