package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Contrato;
import br.com.abril.nds.repository.ContratoRepository;
import br.com.abril.nds.service.ContratoService;

@Service
public class ContratoServiceImpl implements ContratoService {
	
	@Autowired
	private ContratoRepository contratoRespository;
	
	@Override
	@Transactional
	public void salvarContrato(Contrato contrato) {
		this.contratoRespository.merge(contrato);
	}

	
}
