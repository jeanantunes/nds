package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoAcumuloDividaRepository;

@Repository
public class HistoricoAcumuloDividaRepositoryImpl extends
		AbstractRepositoryModel<HistoricoAcumuloDivida, Long> implements HistoricoAcumuloDividaRepository {

	public HistoricoAcumuloDividaRepositoryImpl() {
		super(HistoricoAcumuloDivida.class);
	}
}