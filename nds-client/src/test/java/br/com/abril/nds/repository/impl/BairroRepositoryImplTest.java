package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.dne.Bairro;

public class BairroRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private BairroRepositoryImpl bairroRepositoryImpl;

	@Test
	public void testarPesquisarBairros() {

		List<Bairro> bairros;

		bairros = bairroRepositoryImpl.pesquisarBairros("teste");

		Assert.assertNotNull(bairros);

	}

}
