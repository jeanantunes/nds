package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.service.ConsultaConsignadoCota;

@Service
public class ConsultaConsignadoCotaImpl implements ConsultaConsignadoCota {
	
	@Autowired
	private ConsultaConsignadoCotaRepository consignadoCotaRepository;

	@Override
	@Transactional
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro, String limitar) {		 
		return this.consignadoCotaRepository.buscarConsignadoCota(filtro, limitar);
	}

}
