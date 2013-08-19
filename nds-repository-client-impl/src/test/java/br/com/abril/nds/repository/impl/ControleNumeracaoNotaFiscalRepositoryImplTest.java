package br.com.abril.nds.repository.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;
import br.com.abril.nds.repository.ControleNumeracaoNotaFiscalRepository;

public class ControleNumeracaoNotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private ControleNumeracaoNotaFiscalRepository controleNumeracaoNotaFiscalRepository;
	
	@Before
	public void setup() {
		
		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = Fixture.controleNumeracaoNotaFiscal(1L, "0001");
		save(controleNumeracaoNotaFiscal);
		
	}
		
	@Test
	public void testObterControleNumeracaoNotaFiscal() {
		
		String serieNF = "0001";
		
		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = controleNumeracaoNotaFiscalRepository.obterControleNumeracaoNotaFiscal(serieNF);
		
		Assert.assertTrue(controleNumeracaoNotaFiscal!=null);
		
	}
	
	
	
	@Test
	public void testObterForUpdateControleNumeracaoNotaFiscal() {

		String serieNF = "0001";
		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = controleNumeracaoNotaFiscalRepository.obterControleNumeracaoNotaFiscal(serieNF);

		
		controleNumeracaoNotaFiscal = 
				controleNumeracaoNotaFiscalRepository.obterForUpdateControleNumeracaoNotaFiscal(controleNumeracaoNotaFiscal.getId());
		
	}
	
	
	
}
