package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.TipoPontoPDVRepository;

public class TipoPontoPDVRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TipoPontoPDVRepository tipoPontoPDVRepository;

	@Test
	public void buscarTodosPdvPrincipal() {

		List<TipoPontoPDV> lista = tipoPontoPDVRepository
				.buscarTodosPdvPrincipal();

		Assert.assertNotNull(lista);
	}

	@SuppressWarnings("unused")
	@Test
	public void buscarTipoPontoPdvPrincipal() {

		TipoPontoPDV tipoPontoPDV = tipoPontoPDVRepository
				.buscarTipoPontoPdvPrincipal(1L);

	}
}
