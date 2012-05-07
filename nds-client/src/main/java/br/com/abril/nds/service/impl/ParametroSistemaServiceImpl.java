package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.ParametroSistemaService;

@Service
public class ParametroSistemaServiceImpl implements ParametroSistemaService{

	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema) {
		return parametroSistemaRepository.buscarParametroPorTipoParametro(tipoParametroSistema);
	}

}
