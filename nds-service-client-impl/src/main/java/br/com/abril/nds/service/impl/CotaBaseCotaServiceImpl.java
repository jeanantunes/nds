package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBaseCota;
import br.com.abril.nds.repository.CotaBaseCotaRepository;
import br.com.abril.nds.service.CotaBaseCotaService;

@Service
public class CotaBaseCotaServiceImpl implements CotaBaseCotaService {

	@Autowired
	private CotaBaseCotaRepository cotaBaseCotaRepository;
	
	@Override
	@Transactional
	public void salvar(CotaBaseCota cotaBaseCota) {
		this.cotaBaseCotaRepository.adicionar(cotaBaseCota);
	}

	@Override
	@Transactional
	public Long verificarExistenciaCotaBaseCota(Cota cota) {		 
		return this.cotaBaseCotaRepository.verificarExistenciaCotaBaseCota(cota);
	}

	@Override
	@Transactional
	public boolean isCotaBaseAtiva(Cota cota) {		 
		return this.cotaBaseCotaRepository.isCotaBaseAtiva(cota);
	}

}
