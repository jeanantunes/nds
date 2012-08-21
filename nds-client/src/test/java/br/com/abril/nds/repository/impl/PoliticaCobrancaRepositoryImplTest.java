package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;

public class PoliticaCobrancaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Before
	public void setUp() {
	
		Banco banco = Fixture.hsbc(); 
		save(banco);
				
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
  		save(parametroCobranca);
		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE,parametroCobranca);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem",true,FormaEmissao.INDIVIDUAL_BOX);
		
		save(politicaCobranca);
	}
	
	@Test
	public void obterPorTipoCobranca() {
		PoliticaCobranca politicaCobranca = 
				politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		Assert.assertTrue(politicaCobranca != null);
	}
	
}
