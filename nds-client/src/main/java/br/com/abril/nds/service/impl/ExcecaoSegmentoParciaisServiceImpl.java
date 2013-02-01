package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;

@Service
public class ExcecaoSegmentoParciaisServiceImpl implements
		ExcecaoSegmentoParciaisService {

	@Autowired
	private ExcecaoSegmentoParciaisRepository excecaoSegmentoParciaisRepository; 
	
	@Transactional(readOnly = true)
	@Override
	public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
		return excecaoSegmentoParciaisRepository.obterProdutosNaoRecebidosPelaCotaPorSegmento(filtro);
	}

}
