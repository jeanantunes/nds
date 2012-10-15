package br.com.abril.nds.service.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;

public class ContasAPagarRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ContasAPagarRepository contasAPagarRepository;

	@Test
	public void testPesquisaProdutoContasAPagar() {
	
		Long num = null;
	    
		List<ContasAPagarConsultaProdutoDTO> resultado = contasAPagarRepository.pesquisaProdutoContasAPagar("1", num);
		
		Assert.assertNotNull(resultado);
	}

}
