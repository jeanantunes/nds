package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;

public class ParametrosDistribuidorFaltasSobrasRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ParametrosDistribuidorFaltasSobrasRepositoryImpl parametrosDistribuidorFaltasSobrasRepositoryImpl;
	private Distribuidor distribuidor;

	@Before
	public void setup() {
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica(
				"Distribuidor Acme", "56003315000147", "110042490114",
				"distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(),
				new HashSet<PoliticaCobranca>());
		save(distribuidor);
	}

	@Test
	public void alterarOuCriar() {

		ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras = new ParametrosDistribuidorFaltasSobras();
		parametrosDistribuidorFaltasSobras.setFaltaDe(false);
		parametrosDistribuidorFaltasSobras.setFaltaEm(true);
		parametrosDistribuidorFaltasSobras.setSobraDe(false);
		parametrosDistribuidorFaltasSobras.setSobraEm(true);
		parametrosDistribuidorFaltasSobras.setDistribuidor(distribuidor);
		
		parametrosDistribuidorFaltasSobrasRepositoryImpl.alterarOuCriar(parametrosDistribuidorFaltasSobras);
	}

}
