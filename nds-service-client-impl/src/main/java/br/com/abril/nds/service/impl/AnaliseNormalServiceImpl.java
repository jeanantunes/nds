package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseNormalDTO;
import br.com.abril.nds.dto.filtro.AnaliseNormalQueryDTO;
import br.com.abril.nds.model.cadastro.AnaliseNormalProdutoEdicaoDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.repository.AnaliseNormalRepository;
import br.com.abril.nds.service.AnaliseNormalService;
import br.com.abril.nds.service.EstudoService;

@Service
public class AnaliseNormalServiceImpl implements AnaliseNormalService{
	
	@Autowired
	private AnaliseNormalRepository analiseNormalRepository;
	
	@Autowired
	private EstudoService estudoService;

	@Override
	@Transactional(readOnly = true)
	public EstudoGerado buscarPorId(Long id) {
		EstudoGerado estudo = analiseNormalRepository.buscarPorId(id);
		return estudo;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnaliseNormalDTO> buscaAnaliseNormalPorEstudo(AnaliseNormalQueryDTO queryDTO) {
		return analiseNormalRepository.buscaAnaliseNormalPorEstudo(queryDTO);
	}

	@Override
	@Transactional
	public void atualizaReparte(Long estudoId, Long numeroCota, Long reparte) {
		analiseNormalRepository.atualizaReparte(estudoId, numeroCota, reparte);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnaliseNormalProdutoEdicaoDTO> buscaProdutoParaGrid(Long id) {
		return analiseNormalRepository.buscaProdutoParaGrid(id);
	}
	
	@Override
	@Transactional
	public void liberar(Long id) {

		estudoService.liberar(id);
	}

}
