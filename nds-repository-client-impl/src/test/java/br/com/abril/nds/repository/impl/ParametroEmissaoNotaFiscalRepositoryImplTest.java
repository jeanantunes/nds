package br.com.abril.nds.repository.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;

public class ParametroEmissaoNotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private ParametroEmissaoNotaFiscalRepository parametroEmissaoNotaFiscalRepository;
	
	@Before
	public void setup() {
	
		CFOP cfopDentroEstado = Fixture.cfop1209();
		save(cfopDentroEstado);
		
		CFOP cfopForaEstado = Fixture.cfop1210();
		save(cfopForaEstado);
		
		ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscal = Fixture.parametroEmissaoNotaFiscal(
				cfopDentroEstado, 
				cfopForaEstado, 
				GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR, 
				"0001");
		
		save(parametroEmissaoNotaFiscal);
		
	}
		
	@Test
	public void testObterParametroEmissaoNotaFiscal() {
		
		ParametroEmissaoNotaFiscal parametro = parametroEmissaoNotaFiscalRepository.obterParametroEmissaoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		
		Assert.assertNotNull(parametro);
		
	}
	
}
