package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.repository.ChamadaoRepository;
import br.com.abril.nds.service.ChamadaoService;

/**
 * Classe de implementação de serviços referentes
 * ao chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Service
public class ChamadaoServiceImpl implements ChamadaoService {

	@Autowired
	protected ChamadaoRepository chamadaoRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ConsignadoCotaChamadaoDTO> obterConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterTotalConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterTotalConsignadosParaChamadao(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResumoConsignadoCotaChamadaoDTO obterResumoConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterResumoConsignadosParaChamadao(filtro);
	}
	
}
