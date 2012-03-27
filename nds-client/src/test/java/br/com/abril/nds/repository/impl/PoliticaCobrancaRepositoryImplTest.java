package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;

public class PoliticaCobrancaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Before
	public void setUp() {
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteira);
		
		Banco banco = Fixture.hsbc(carteira); 
		save(banco);
				
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1);
		
		save(politicaCobranca);
	}
	
	@Test
	public void obterPorTipoCobranca() {
		PoliticaCobranca politicaCobranca = 
				politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		Assert.assertTrue(politicaCobranca != null);
	}
	
}
