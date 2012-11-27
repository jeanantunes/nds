package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.NCMRepository;

public class NCMRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private NCMRepository ncmRepository;
	
	@Test
	public void obterPorCodigo(){
		Long codigo = 1L;
		
		ncmRepository.obterPorCodigo(codigo);
	}

}
