package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.TipoPontoPDVRepository;

@Repository
public class TipoPontoPDVRepositoryImpl extends AbstractRepositoryModel<TipoPontoPDV, Long> implements TipoPontoPDVRepository {
	
	public TipoPontoPDVRepositoryImpl() {
		super(TipoPontoPDV.class);
	}
}
