package br.com.abril.nds.repository.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.repository.BoletoDistribuidorRepository;

public class BoletoDistribuidorRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private BoletoDistribuidorRepository boletoDistribuidorRepository;

	
	@Before
	public void setup() {
		
	}

	@Test
	public void testObterBoletoDistribuidorPorChamadaEncalheFornecedor() {
		BoletoDistribuidor boleto = 
				this.boletoDistribuidorRepository.obterBoletoDistribuidorPorChamadaEncalheFornecedor(1L);
	}
	
	
}
