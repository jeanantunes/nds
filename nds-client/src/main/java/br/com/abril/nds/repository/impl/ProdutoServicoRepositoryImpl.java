package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.repository.ProdutoServicoRepository;

@Repository
public class ProdutoServicoRepositoryImpl extends AbstractRepositoryModel<ProdutoServico, Long> implements ProdutoServicoRepository {

	public ProdutoServicoRepositoryImpl() {
		super(ProdutoServico.class);
	}

}
