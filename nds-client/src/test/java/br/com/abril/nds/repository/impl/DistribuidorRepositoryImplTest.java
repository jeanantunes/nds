package br.com.abril.nds.repository.impl;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public class DistribuidorRepositoryImplTest extends AbstractRepositoryImplTest {

	private Distribuidor distribuidor;
	
	@Autowired
	private DistribuidorRepositoryImpl distribuidorRepository;
	
	@Before
	public void setUp() {
		PessoaJuridica pj = Fixture.pessoaJuridica("Distrib", "01.001.001/001-00",
				"000.000.000.00", "distrib@mail.com");
		distribuidor = Fixture.distribuidor(pj, new Date());
		getSession().save(pj);
		getSession().save(distribuidor);
	}
	
	@Test
	public void obter() {
		Distribuidor distribuidorDB = distribuidorRepository.obter();
		Assert.assertNotNull(distribuidorDB);
		Assert.assertSame(distribuidor, distribuidorDB);
	}

	

}
