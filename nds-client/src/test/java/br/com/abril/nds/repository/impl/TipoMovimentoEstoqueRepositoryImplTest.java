package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;

public class TipoMovimentoEstoqueRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;

	@SuppressWarnings("unused")
	@Test
	public void buscarTipoMovimentoEstoque() {
		TipoMovimentoEstoque tipoMovimentoEstoquex = tipoMovimentoEstoqueRepository
				.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
	}

}
