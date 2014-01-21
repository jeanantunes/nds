package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;
import br.com.abril.nds.repository.AnaliseEstudoRepository;
import br.com.abril.nds.service.AnaliseEstudoService;

@Service
public class AnaliseEstudoServiceImpl implements AnaliseEstudoService  {
	
	@Autowired
	private AnaliseEstudoRepository analiseEstudoRepository;

	@Override
	@Transactional
	public List<AnaliseEstudoDTO> buscarTodosEstudos(FiltroAnaliseEstudoDTO filtro) {
		return analiseEstudoRepository.buscarEstudos(filtro);
	}

}
