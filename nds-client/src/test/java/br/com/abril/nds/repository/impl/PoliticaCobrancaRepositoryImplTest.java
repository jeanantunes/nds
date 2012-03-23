package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;

public class PoliticaCobrancaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Before
	public void setUp() {
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, TipoCobranca.BOLETO,
										  new BigDecimal(200), true, true, true, 1);
		
		save(politicaCobranca);
	}
	
	@Test
	public void obterPorTipoCobranca() {
		PoliticaCobranca politicaCobranca = 
				politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		Assert.assertTrue(politicaCobranca != null);
	}
	
}
