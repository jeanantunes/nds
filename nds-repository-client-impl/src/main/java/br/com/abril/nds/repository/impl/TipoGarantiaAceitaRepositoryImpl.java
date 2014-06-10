package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoGarantiaAceitaRepository;

@Repository
public class TipoGarantiaAceitaRepositoryImpl extends AbstractRepositoryModel<TipoGarantiaAceita, Long> implements TipoGarantiaAceitaRepository {

	public TipoGarantiaAceitaRepositoryImpl() {
		super(TipoGarantiaAceita.class);
	}

	@Override
	public void alterarOuCriar(TipoGarantiaAceita tipoGarantiaAceita) {
		getSession().saveOrUpdate(tipoGarantiaAceita);
	}

}
