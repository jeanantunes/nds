package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.util.DateUtil;

public class FeriadoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	public static Date FERIADO_SETE_SETEMBRO = DateUtil.parseDataPTBR("07/09/2012");
	
	@Before
	public void setUp() {

		Feriado feriado =
			Fixture.feriado(FERIADO_SETE_SETEMBRO, "Dia da Independência do Brasil");
		save(feriado);
	}
	
	@Test
	public void obterFeriadoPorData() {
		
		Feriado feriado = feriadoRepository.obterPorData(FERIADO_SETE_SETEMBRO);
		
		Assert.assertTrue(feriado != null);
	}
		
}
