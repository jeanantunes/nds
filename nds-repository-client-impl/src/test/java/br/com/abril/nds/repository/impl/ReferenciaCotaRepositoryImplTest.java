package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.ReferenciaCotaRepository;

public class ReferenciaCotaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ReferenciaCotaRepository referenciaCotaRepositoryImpl;

	@Test
	public void excluirReferenciaCota() {
		Long idBaseReferencia = 1L;

		referenciaCotaRepositoryImpl.excluirReferenciaCota(idBaseReferencia);

	}

}
