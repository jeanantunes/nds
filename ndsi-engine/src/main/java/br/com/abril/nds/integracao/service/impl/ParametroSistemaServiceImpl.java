package br.com.abril.nds.integracao.service.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Service
public class ParametroSistemaServiceImpl implements ParametroSistemaService {
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Transactional
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema) {
		return parametroSistemaRepository.buscarParametroPorTipoParametro(tipoParametroSistema);
	}

	@Override
	@Transactional
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = parametroSistemaRepository.buscarParametroSistemaGeral();
		
		ParametroSistemaGeralDTO dto = new ParametroSistemaGeralDTO();
		dto.setParametrosSistema(lst);
		
		return dto;
	}
	
	@Override
	@Transactional
	public void salvar(ParametroSistemaGeralDTO dto, InputStream imgLogotipo, String imgContentType) {
		
		List<ParametroSistema> lst = dto.getParametrosSistema();
		parametroSistemaRepository.salvar(lst);
	}
	
	@Override
	@Transactional
	public void salvar(Collection<ParametroSistema> parametrosSistema) {
		parametroSistemaRepository.salvar(parametrosSistema);
	}	
}
