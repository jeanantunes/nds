package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DiferencaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class DiferencaEstoqueServiceImpl implements DiferencaEstoqueService {

	@Autowired
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Transactional(readOnly = true)
	public List<DiferencaDTO> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
	}

	@Transactional(readOnly = true)
	public List<DiferencaDTO> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterDiferencas(filtro);
	}

}
