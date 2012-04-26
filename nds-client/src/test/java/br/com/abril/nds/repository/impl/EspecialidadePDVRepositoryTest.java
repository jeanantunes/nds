package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
import br.com.abril.nds.repository.EspecialidadePDVRepository;

public class EspecialidadePDVRepositoryTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EspecialidadePDVRepository especialidadePDVRepository;
	
	private EspecialidadePDV especialidadePDV;
	private EspecialidadePDV especialidadePDV1;
	private EspecialidadePDV especialidadePDV2;
	private EspecialidadePDV especialidadePDV3;

	
	@Before
	public void setup() {
		
		especialidadePDV  = Fixture.criarEspecialidadesPDV(10L, "Teste");
		
		especialidadePDV1 = Fixture.criarEspecialidadesPDV(11L, "Teste");
		
		especialidadePDV2 = Fixture.criarEspecialidadesPDV(12L, "Teste");
		
		especialidadePDV3 = Fixture.criarEspecialidadesPDV(13L, "Teste");
		
		save(especialidadePDV,especialidadePDV1,especialidadePDV2,especialidadePDV3);
	}
	
	@Test
	public void obterEspecialidades(){
		
		List<EspecialidadePDV> lista = especialidadePDVRepository.obterEspecialidades(especialidadePDV.getCodigo(),
																					  especialidadePDV1.getCodigo(),
																					  especialidadePDV2.getCodigo(),
																					  especialidadePDV3.getCodigo());
		
		Set<EspecialidadePDV> set = new HashSet<EspecialidadePDV>();
		set.addAll(lista);
		
		Assert.assertTrue(!set.isEmpty());

	}
	
	
}
