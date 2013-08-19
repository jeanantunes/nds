package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametroContratoCotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * @author infoA2
 */
@Repository
public class ParametroContratoCotaRepositoryImpl extends AbstractRepositoryModel<ParametroContratoCota, Long> implements ParametroContratoCotaRepository {

	public ParametroContratoCotaRepositoryImpl() {
		super(ParametroContratoCota.class);
	}

}
