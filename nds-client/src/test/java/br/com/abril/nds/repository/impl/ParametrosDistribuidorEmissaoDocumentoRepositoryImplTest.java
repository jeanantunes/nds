package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;

public class ParametrosDistribuidorEmissaoDocumentoRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ParametrosDistribuidorEmissaoDocumentoRepositoryImpl parametrosDistribuidorEmissaoDocumentoRepositoryImpl;
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

		ParametrosDistribuidorEmissaoDocumento parametros = new ParametrosDistribuidorEmissaoDocumento();

		parametros.setDistribuidor(distribuidor);
		parametros.setUtilizaImpressao(false);
		parametros.setUtilizaEmail(true);
		parametros
				.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.BOLETO);

		parametrosDistribuidorEmissaoDocumentoRepositoryImpl
				.alterarOuCriar(parametros);
	}
}
