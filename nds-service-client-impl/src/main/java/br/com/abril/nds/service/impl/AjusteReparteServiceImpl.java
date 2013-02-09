package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.repository.AjusteReparteRepository;
import br.com.abril.nds.service.AjusteReparteService;

@Service
public class AjusteReparteServiceImpl implements AjusteReparteService  {
	
	@Autowired
	private AjusteReparteRepository ajusteRepository;

	@Override
	@Transactional
	public void salvarAjuste(AjusteReparte ajuste) {
		ajusteRepository.adicionar(ajuste);
	}

	@Override
	@Transactional
	public List<AjusteReparteDTO> buscarCotasEmAjuste(AjusteReparteDTO dto) {
		return ajusteRepository.buscarTodasCotas(dto);
	}
}
