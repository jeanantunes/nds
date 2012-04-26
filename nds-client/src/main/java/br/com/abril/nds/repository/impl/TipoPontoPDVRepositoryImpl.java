package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.TipoPontoPDVRepository;

public class TipoPontoPDVRepositoryImpl extends AbstractRepository<TipoPontoPDV, Long> implements TipoPontoPDVRepository {
	
	public TipoPontoPDVRepositoryImpl() {
		super(TipoPontoPDV.class);
	}
}
