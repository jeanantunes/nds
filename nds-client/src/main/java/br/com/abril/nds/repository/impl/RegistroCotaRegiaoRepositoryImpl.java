package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;

@Repository
public class RegistroCotaRegiaoRepositoryImpl extends AbstractRepositoryModel<RegistroCotaRegiao, Long> implements RegistroCotaRegiaoRepository {

	public RegistroCotaRegiaoRepositoryImpl() {
		super(RegistroCotaRegiao.class);
	}

}
