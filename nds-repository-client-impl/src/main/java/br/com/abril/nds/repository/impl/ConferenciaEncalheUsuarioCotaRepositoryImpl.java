package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.seguranca.ConferenciaEncalheUsuarioCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheUsuarioCotaRepository;

@Repository
public class ConferenciaEncalheUsuarioCotaRepositoryImpl extends AbstractRepositoryModel<ConferenciaEncalheUsuarioCota, Long> 
		implements ConferenciaEncalheUsuarioCotaRepository {

	public ConferenciaEncalheUsuarioCotaRepositoryImpl() {
		super(ConferenciaEncalheUsuarioCota.class);
	}

	@Override
	public ConferenciaEncalheUsuarioCota obterPorNumeroCota(Integer numeroCota) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConferenciaEncalheUsuarioCota obterPorLogin(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConferenciaEncalheUsuarioCota obterPorSessionId(String sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removerPorNumeroCota(Integer numeroCota) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removerPorLogin(String login) {
		// TODO Auto-generated method stub
		return false;
	}
	
}