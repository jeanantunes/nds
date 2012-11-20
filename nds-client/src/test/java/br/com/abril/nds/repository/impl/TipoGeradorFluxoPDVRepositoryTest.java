package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;

public class TipoGeradorFluxoPDVRepositoryTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private TipoGeradorFluxoPDVRepsitory tipoGeradorFluxoPDVRepsitory;
	
	private TipoGeradorFluxoPDV tipoGeradorFluxoPDV;
	private TipoGeradorFluxoPDV tipoGeradorFluxoPDV1;
	
	@Before
	public void setup() {
		
		tipoGeradorFluxoPDV  = Fixture.criarTipoGeradorFluxoPDV(10L, "Teste");
		tipoGeradorFluxoPDV1  = Fixture.criarTipoGeradorFluxoPDV(12L, "Teste");
		
		save(tipoGeradorFluxoPDV,tipoGeradorFluxoPDV1);
		
	}
	
	@Test
	public void obterTiposGeradorFluxo(){
		
		List<TipoGeradorFluxoPDV> tipos = tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxo(tipoGeradorFluxoPDV.getCodigo(),tipoGeradorFluxoPDV1.getCodigo());
		
		Set<TipoGeradorFluxoPDV> set = new HashSet<TipoGeradorFluxoPDV>();
		set.addAll(tipos);
		
		Assert.assertTrue(!set.isEmpty());

	}
	
	@Test
	public void obterTiposGeradorFluxoNotIn(){
		
		List<TipoGeradorFluxoPDV> tipos = tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxoNotIn(tipoGeradorFluxoPDV.getCodigo(),tipoGeradorFluxoPDV1.getCodigo());
		
		Set<TipoGeradorFluxoPDV> set = new HashSet<TipoGeradorFluxoPDV>();
		set.addAll(tipos);
		
		Assert.assertNotNull(tipos);
				

	}
	
	
}
