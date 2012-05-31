package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CobrancaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Test
	public void testarobterCobrancasPorIDS() {
		
		List<Long> listaIdsCobrancas = new ArrayList<Long>();

		listaIdsCobrancas.add(1L);
		listaIdsCobrancas.add(2L);
		listaIdsCobrancas.add(3L);
		
		List<Cobranca> listaCobrancas =
			this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobrancas);
		
		Assert.assertNotNull(listaCobrancas);
	}
	
}
