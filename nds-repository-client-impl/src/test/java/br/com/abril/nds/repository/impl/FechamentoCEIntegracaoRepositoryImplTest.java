package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.util.Intervalo;

public class FechamentoCEIntegracaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoCEIntegracaoRepositoryImpl fechamentoCEIntegracaoRepositoryImpl;

	@Test
	public void buscarItensFechamentoCeIntegracaoTest() {

		FiltroFechamentoCEIntegracaoDTO filtro = new FiltroFechamentoCEIntegracaoDTO();
		
		Intervalo<Date> intervalo = obterIntervalo();
		
		filtro.setPeriodoRecolhimento(intervalo);

		this.fechamentoCEIntegracaoRepositoryImpl.buscarItensFechamentoCeIntegracao(filtro);
	}
	
	public Intervalo<Date> obterIntervalo() {
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(2013, Calendar.MAY, 22);
		
		Date de = cal.getTime();
		
		cal.set(2013, Calendar.MAY, 28);
		
		Date ate = cal.getTime();
		
		return new Intervalo<Date>(de, ate);
	}
}
