package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.repository.ProdutoBaseSugeridaRepository;
import br.com.abril.nds.service.ProdutoBaseSugeridaService;

@Service
@Transactional(readOnly=true)
public class ProdutoBaseSugerida implements ProdutoBaseSugeridaService {
	
	@Autowired
	private ProdutoBaseSugeridaRepository produtoBaseSugeridaRepo;

	@Override
	public List<ProdutoBaseSugeridaDTO> obterBaseSugerida(Long idEstudo) {
		return produtoBaseSugeridaRepo.obterBaseSugerida(idEstudo);
	}
	
}
