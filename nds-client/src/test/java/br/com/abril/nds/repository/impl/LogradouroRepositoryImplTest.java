package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.dne.Logradouro;

@Ignore
public class LogradouroRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private LogradouroRepositoryImpl logradouroRepositoryImpl;
	
	@Test
	public void testarPesquisarLogradouros() {
		
		List<Logradouro> listaLogradrous;
		
		String nomeLogradouro = "testeLogradouro";
		
		listaLogradrous = logradouroRepositoryImpl.pesquisarLogradouros(nomeLogradouro);
		
		Assert.assertNotNull(listaLogradrous);
		
	}

}
