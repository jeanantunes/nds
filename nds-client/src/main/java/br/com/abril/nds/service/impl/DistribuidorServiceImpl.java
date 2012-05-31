package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.DistribuidorService;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	@Transactional
	public Distribuidor obter() {
		return distribuidorRepository.obter();
	}

	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		return distribuidorRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
	}

	@Override
	@Transactional
	public ResultadoCurvaABCDistribuidor obterCurvaABCDistribuidorTotal(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		return distribuidorRepository.obterCurvaABCDistribuidorTotal(filtroCurvaABCDistribuidorDTO);
	}

}
