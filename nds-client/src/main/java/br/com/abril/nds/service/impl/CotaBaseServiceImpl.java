package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.repository.CotaBaseRepository;
import br.com.abril.nds.service.CotaBaseService;

@Service
public class CotaBaseServiceImpl implements CotaBaseService {
	
	@Autowired
	private CotaBaseRepository cotaBaseRepository;
	
	@Override
	@Transactional(readOnly = true)
	public FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota, boolean obterFaturamento) {		 
		return cotaBaseRepository.obterDadosFiltro(numeroCota, obterFaturamento);		
	}

	@Override
	@Transactional
	public List<CotaBaseDTO> obterCotasBases(Cota cotaNova) {
		return cotaBaseRepository.obterCotasBases(cotaNova);
	}

	@Override
	@Transactional
	public void salvar(CotaBase cotaBase) {
		this.cotaBaseRepository.adicionar(cotaBase); 
	}

}
