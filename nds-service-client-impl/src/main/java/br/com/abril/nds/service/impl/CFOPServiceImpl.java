package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.service.CFOPService;

@Service
public class CFOPServiceImpl implements CFOPService {

	@Autowired
	private CFOPRepository cfopRepository;
	
	/**
	 * Obtem CFOP por id
	 * @param codigo
	 * @return CFOP
	 */
	@Override
	@Transactional
	public CFOP buscarPorId(Long id) {		 
		return this.cfopRepository.buscarPorId(id);
	}

	/**
	 * Obtem CFOP por código
	 * @param codigo
	 * @return CFOP
	 */
	@Override
	@Transactional
	public CFOP buscarPorCodigo(String codigo) {
		return this.cfopRepository.obterPorCodigo(codigo);
	}

}
