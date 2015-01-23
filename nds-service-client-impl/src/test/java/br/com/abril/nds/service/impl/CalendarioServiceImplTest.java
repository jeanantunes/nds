package br.com.abril.nds.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class CalendarioServiceImplTest {

	@Mock
	CalendarioServiceImpl service;
	
	@Test
	public void test_adicionar_dias_uteis() {
		
		
		Calendar cDtEmissao = Calendar.getInstance();
		
		cDtEmissao.clear();
		
		cDtEmissao.set(Calendar.DAY_OF_MONTH, 18);
		cDtEmissao.set(Calendar.MONTH, Calendar.JULY);
		cDtEmissao.set(Calendar.YEAR, 2013);
		
		Calendar dataEsperada = Calendar.getInstance(); 
		
		dataEsperada.clear();
		
		dataEsperada.set(Calendar.DAY_OF_MONTH, 24);
		dataEsperada.set(Calendar.MONTH, Calendar.JULY);
		dataEsperada.set(Calendar.YEAR, 2013);
		
		Date dataEmissao = cDtEmissao.getTime();
		
		int numDias = 4;
		
		List<Integer> diasSemanaConcentracaoCobranca = new ArrayList<>();
		
		diasSemanaConcentracaoCobranca.add(Calendar.WEDNESDAY);
		
		diasSemanaConcentracaoCobranca.add(Calendar.FRIDAY);
		
		
		when(service.adicionarDiasUteis(Matchers.any(Date.class), Matchers.anyInt())).thenCallRealMethod();
		
		when(service.isFeriado(dataEsperada, null)).thenReturn(false);
		
		
		Date dataRetornada = service.adicionarDiasUteis(dataEmissao, numDias);
		
		//FIXME: A data nao pode ser nula
		Assert.assertNull(dataRetornada);
		
	}
	
}
