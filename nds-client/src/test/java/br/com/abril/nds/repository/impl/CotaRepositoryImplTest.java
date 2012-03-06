package br.com.abril.nds.repository.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

public class CotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaRepositoryImpl cotaRepository;
	
	private static final Integer NUMERO_COTA = 1;
	
	@Before
	public void setup() {
		
		PessoaJuridica pessoaJuridica = 
			Fixture.pessoaJuridica("FC", "01.001.001/001-00", "000.000.000.00", "fc@mail.com");

		getSession().save(pessoaJuridica);
		
		Cota cota = Fixture.cota(NUMERO_COTA, pessoaJuridica, SituacaoCadastro.ATIVO);
		
		getSession().save(cota);
	}
	
	@Test
	public void obterPorNumeroCota() {
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);
		
		Assert.assertNotNull(cota);
		
		Assert.assertEquals(NUMERO_COTA, cota.getNumeroCota());
	}

}
