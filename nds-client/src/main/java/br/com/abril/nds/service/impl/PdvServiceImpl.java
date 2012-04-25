package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.service.PdvService;

@Service
public class PdvServiceImpl implements PdvService {

	@Autowired
	private PdvRepository pdvRepository;
	
	@Override
	public boolean isExcluirPdv(Long idPdv) {
		
		return Boolean.TRUE;
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<PDV> obterPDVsPorCota(FiltroPdvDTO filtro) {
		
		return pdvRepository.obterPDVsPorCota(filtro);
	}

}
