package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.repository.MaterialPromocionalRepository;

public class MaterialPromocionalRepositoryTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MaterialPromocionalRepository materialPromocionalRepository;
	
	private MaterialPromocional material;
	private MaterialPromocional material1;
	private MaterialPromocional material2;
	private MaterialPromocional material3;
	private MaterialPromocional material4;
	
	@Before
	public void setup() {
		
		material = Fixture.criarMaterialPromocional(10L, "Teste");
		
		material1 = Fixture.criarMaterialPromocional(11L, "Teste");
		
		material2 = Fixture.criarMaterialPromocional(10L, "Teste");
		
		material3 = Fixture.criarMaterialPromocional(12L, "Teste");
		
		material4 = Fixture.criarMaterialPromocional(13L, "Teste");
		
		save(material,material1,material2,material3,material4);
	}
	
	@Test
	public void obterMateriais(){
		
		List<MaterialPromocional> materiais = materialPromocionalRepository.obterMateriaisPromocional(material.getCodigo(),
																									  material1.getCodigo(),
																									  material2.getCodigo(),
																									  material3.getCodigo(),
																									  material4.getCodigo());
		
		Set<MaterialPromocional> set = new HashSet<MaterialPromocional>();
		set.addAll(materiais);
		
		Assert.assertTrue(!set.isEmpty());

	}
	
	
}
