package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseNormalDTO;
import br.com.abril.nds.dto.filtro.AnaliseNormalQueryDTO;
import br.com.abril.nds.model.cadastro.AnaliseNormalProdutoEdicaoDTO;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.AnaliseNormalRepository;
import br.com.abril.nds.service.AnaliseNormalService;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class AnaliseNormalServiceImpl implements AnaliseNormalService{
	
	@Autowired
	private AnaliseNormalRepository analiseNormalRepository;

	@Override
	@Transactional
	public Estudo buscarPorId(Long id) {
		Estudo estudo = analiseNormalRepository.buscarPorId(id);
		return estudo;
	}

	@Override
	public List<AnaliseNormalDTO> buscaAnaliseNormalPorEstudo(AnaliseNormalQueryDTO queryDTO) {
		return analiseNormalRepository.buscaAnaliseNormalPorEstudo(queryDTO);
	}

	@Override
	public void atualizaReparte(Long estudoId, Long numeroCota, Long reparte) {
		analiseNormalRepository.atualizaReparte(estudoId, numeroCota, reparte);
	}

	@Override
	public List<AnaliseNormalProdutoEdicaoDTO> buscaProdutoParaGrid(Long id) {
		return analiseNormalRepository.buscaProdutoParaGrid(id);
	}
	@Override
	public void liberar(Long id) {
		analiseNormalRepository.liberarEstudo(id);
	}

}
