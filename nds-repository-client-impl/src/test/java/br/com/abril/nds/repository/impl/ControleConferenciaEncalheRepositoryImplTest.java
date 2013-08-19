package br.com.abril.nds.repository.impl;

import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;

public class ControleConferenciaEncalheRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Before
	public void setUp() {
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = Fixture.controleConferenciaEncalhe(
				StatusOperacao.EM_ANDAMENTO, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012));

		save(controleConferenciaEncalhe);
		
	}
	
	@Test
	public void testObterControleConferenciaEncalhe() {
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = controleConferenciaEncalheRepository.obterControleConferenciaEncalhe(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		
		Assert.assertNotNull(controleConferenciaEncalhe);
		
	}

}
