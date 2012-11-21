package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;

public class ParametroCobrancaCotaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ParametroCobrancaCotaRepository parametroCobrancaCotaRepository;

	@Test
	public void comboValoresMinimos() {

		List<BigDecimal> lista = parametroCobrancaCotaRepository
				.comboValoresMinimos();
		Assert.assertNotNull(lista);

	}

}
