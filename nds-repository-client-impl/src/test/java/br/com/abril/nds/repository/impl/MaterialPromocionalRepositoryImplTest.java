package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.MaterialPromocional;

public class MaterialPromocionalRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MaterialPromocionalRepositoryImpl materialPromocionalRepositoryImpl;
	
	@Test
	public void testarObterMateriaisPromocional() {
		
		List<MaterialPromocional> listaMateriais;
		
		Long[] codigos = {1L, 2L, 3L};
		
		listaMateriais = materialPromocionalRepositoryImpl.obterMateriaisPromocional(codigos);
		
		Assert.assertNotNull(listaMateriais);
		
	}
	
	@Test
	public void testarObterMateriaisPromocionalNotIn() {
		
		List<MaterialPromocional> listaMateriais;
		
		Long[] codigos = {1L, 2L, 3L};
		
		listaMateriais = materialPromocionalRepositoryImpl.obterMateriaisPromocionalNotIn(codigos);
		
		Assert.assertNotNull(listaMateriais);
		
	}

}
