package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.GrupoCota;

public class GrupoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private GrupoRepositoryImpl grupoRepositoryImpl;
	
	@Test
	public void testarObterTodosGrupos() {
		
		List<GrupoCota> todosGrupos;
		
		todosGrupos = grupoRepositoryImpl.obterTodosGrupos();
		
		Assert.assertNotNull(todosGrupos);		
		
	}

}
