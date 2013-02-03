package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.repository.AreaInfluenciaPDVRepository;

public class AreaInfluenciaPDVRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private AreaInfluenciaPDVRepository areaInfluenciaPDVRepository;

	private AreaInfluenciaPDV areaInfluenciaPDV;
	private AreaInfluenciaPDV areaInfluenciaPDV1;

	@Before
	public void setup() {

		areaInfluenciaPDV = Fixture.criarAreaInfluenciaPDV(10L, "Teste");
		areaInfluenciaPDV1 = Fixture.criarAreaInfluenciaPDV(12L, "Teste");

		save(areaInfluenciaPDV, areaInfluenciaPDV1);

	}

	@Test
	public void obterAreaInfluenciaPDV() {

		List<AreaInfluenciaPDV> areas = areaInfluenciaPDVRepository
				.obterAreaInfluenciaPDV(areaInfluenciaPDV.getCodigo(),
						areaInfluenciaPDV1.getCodigo());

		Set<AreaInfluenciaPDV> set = new HashSet<AreaInfluenciaPDV>();
		set.addAll(areas);

		Assert.assertTrue(!set.isEmpty());

	}

	@SuppressWarnings("unused")
	@Test
	@ExpectedException(Exception.class)
	public void obterAreaInfluenciaPDVSemCodigo() {

		List<AreaInfluenciaPDV> areas = areaInfluenciaPDVRepository
				.obterAreaInfluenciaPDV();

	}

}
