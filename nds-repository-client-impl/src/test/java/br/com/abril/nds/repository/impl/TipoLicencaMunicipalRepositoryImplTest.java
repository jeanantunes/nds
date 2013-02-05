package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.repository.TipoLicencaMunicipalRepository;

public class TipoLicencaMunicipalRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private TipoLicencaMunicipalRepository tipoLicencaMunicipalRepository;

	@SuppressWarnings("unused")
	@Test
	public void obterTipoLicencaMunicipal() {
		TipoLicencaMunicipal tipoLicencaMunicipal = tipoLicencaMunicipalRepository
				.obterTipoLicencaMunicipal(1L);

	}

}
