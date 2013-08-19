package br.com.abril.nds.repository.impl;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;

public class DescontoProximosLancamentosRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private DescontoProximosLancamentosRepository descontoProximosLancamentosRepository;

	@SuppressWarnings("unused")
	@Test
	public void obterDescontoProximosLancamentosPor() {
		DescontoProximosLancamentos desconto = descontoProximosLancamentosRepository
				.obterDescontoProximosLancamentosPor(1L,
						Fixture.criarData(5, Calendar.NOVEMBER, 2012));
	}

}
