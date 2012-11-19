package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Tributacao;
import br.com.abril.nds.repository.TributacaoRepository;

public class TributacaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TributacaoRepository tributacaoRepository;

	@SuppressWarnings("unused")
	@Test
	public void buscar1() {

		Tributacao tributacao = tributacaoRepository.buscar("1",
				TipoOperacao.ENTRADA, "SP", "MG", 1, "1", "1",
				Fixture.criarData(02, Calendar.FEBRUARY, 2012), "Teste");

	}

	@SuppressWarnings("unused")
	@Test
	public void buscar2() {

		List<String> ufs = new ArrayList<>();
		ufs.add("SP");
		ufs.add("MG");
		Tributacao tributacao = tributacaoRepository.buscar("1",
				TipoOperacao.ENTRADA, ufs, 1, "1", "1",
				Fixture.criarData(02, Calendar.FEBRUARY, 2012), "Teste");

	}

	@SuppressWarnings("unused")
	@Test
	public void tributacaoDefault() {

		Tributacao tributacao = tributacaoRepository.tributacaoDefault("1",
				TipoOperacao.ENTRADA, "SP", "MG", 1, "1", "1",
				Fixture.criarData(02, Calendar.FEBRUARY, 2012));

	}

}
