package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.service.BoxService;
/**
 * Classe de implementação referente ao serviço da entidade 
 * {@link br.com.abril.nds.model.cadastro.Box}
 * 
 * @author Discover Technology
 *
 */
@Service
public class BoxServiceImpl implements BoxService{

	@Autowired
	private BoxRepository boxRepository;
	
	@Transactional(readOnly=true)
	@Override
	public List<Box> obterBoxPorProduto(String codigoProduto) {
		
		return boxRepository.obterBoxPorProduto(codigoProduto);
	}
}
