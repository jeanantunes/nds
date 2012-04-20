package br.com.abril.nds.service.impl;

import org.springframework.stereotype.Service;

import br.com.abril.nds.service.PdvService;

@Service
public class PdvServiceImpl implements PdvService {

	@Override
	public boolean isExcluirPdv(Long idPdv) {
		
		return Boolean.TRUE;
	}

}
