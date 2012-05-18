package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.ParametroSistemaService;

@Service
public class ParametroSistemaServiceImpl implements ParametroSistemaService{

	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Transactional
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema) {
		return parametroSistemaRepository.buscarParametroPorTipoParametro(tipoParametroSistema);
	}

	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return Lista dos parâmetros do sistema que são considerados gerais. 
	 */
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = parametroSistemaRepository.buscarParametroSistemaGeral();
		
		ParametroSistemaGeralDTO dto = new ParametroSistemaGeralDTO();
		dto.setParametrosSistema(lst);
		
		return dto;
	}
	
}
