package br.com.abril.nds.integracao.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	public boolean isDistribuidor(Integer codigo) {
		if (obter().getCodigo().equals(codigo))
			return true;
		return false;
	}

	@Override
	@Transactional
	public Distribuidor obter() {
		return distribuidorRepository.obter();
	}

	@Override
	@Transactional
	public void alterar(Distribuidor distribuidor) {
		distribuidorRepository.alterar(distribuidor);
	}
	
}
