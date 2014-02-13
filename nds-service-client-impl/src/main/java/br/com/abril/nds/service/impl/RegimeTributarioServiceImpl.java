package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.RegimeTributario;
import br.com.abril.nds.repository.RegimeTributarioRepository;
import br.com.abril.nds.service.integracao.RegimeTributarioService;

@Service
public class RegimeTributarioServiceImpl implements RegimeTributarioService {

	@Autowired
	private RegimeTributarioRepository regimeTributarioRepository;
	
	@Override
	@Transactional
	public List<RegimeTributario> obterRegimesTributarios() {
		
		return regimeTributarioRepository.buscarTodos();
	}

}