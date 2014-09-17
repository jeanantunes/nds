package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.repository.BrindeRepository;
import br.com.abril.nds.service.BrindeService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Brinde}
 * 
 * @author Discover Technology
 */
@Service
public class BrindeServiceImpl implements BrindeService {
	
	@Autowired
	private BrindeRepository brindeRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Brinde> obterBrindes() {
		
		return this.brindeRepository.buscarTodos();
	}
	
}