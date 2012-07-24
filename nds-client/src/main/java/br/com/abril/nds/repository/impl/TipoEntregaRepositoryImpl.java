package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;

/**
 * Classe de implementação do Repository de TipoEntrega.
 * 
 * @author Discover Technology.
 */
@Repository
public class TipoEntregaRepositoryImpl extends AbstractRepositoryModel<TipoEntrega, Long> implements TipoEntregaRepository {

	public TipoEntregaRepositoryImpl() {
		super(TipoEntrega.class);
	}
}
