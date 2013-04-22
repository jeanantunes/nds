package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import br.com.abril.nds.service.AnaliseParcialService;

@Service
public class AnaliseParcialServiceImpl implements AnaliseParcialService {

	@Autowired
	private AnaliseParcialRepository analiseNormalRepository;

	@Override
	@Transactional
	public EstudoCota buscarPorId(Long id) {
		EstudoCota estudo = analiseNormalRepository.buscarPorId(id);
		return estudo;
	}

	@Override
	public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
		return analiseNormalRepository.buscaAnaliseParcialPorEstudo(queryDTO);
	}

	@Override
	public void atualizaReparte(Long estudoId, Long numeroCota, Double reparte) {
		analiseNormalRepository.atualizaReparte(estudoId, numeroCota, reparte);
		
	}

	@Override
	public void liberar(Long id) {
		analiseNormalRepository.liberar(id);
	}


	

}
