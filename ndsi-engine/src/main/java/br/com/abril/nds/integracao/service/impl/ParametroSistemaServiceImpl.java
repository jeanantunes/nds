package br.com.abril.nds.integracao.service.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.util.DateUtil;

@Service
public class ParametroSistemaServiceImpl implements ParametroSistemaService {
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
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
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		if(distribuidor!= null){

			dto.setDtOperacaoCorrente(DateUtil.formatarDataPTBR(distribuidor.getDataOperacao()));
		}
		
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
