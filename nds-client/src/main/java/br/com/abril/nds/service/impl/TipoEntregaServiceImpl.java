package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;
import br.com.abril.nds.service.TipoEntregaService;

@Service
public class TipoEntregaServiceImpl implements TipoEntregaService{

	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;
		
	@Override
	@Transactional
	public List<TipoEntrega> obterTodos() {
		return tipoEntregaRepository.buscarTodos();
	}

}
