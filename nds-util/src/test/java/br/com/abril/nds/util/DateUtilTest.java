package br.com.abril.nds.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class DateUtilTest {
	
	@Test
	public void obterNumeroPrimeiraSemanaAno2012() {
		
		int numeroSemana = 
			DateUtil.obterNumeroSemanaNoAno(DateUtil.parseDataPTBR("01/01/2012"));
		
		Assert.assertEquals(1, numeroSemana);		
	}
	
	@Test
	public void obterNumeroSegundaSemanaAno2012ComInicioNaTerca() {
		
		int numeroSemana = 
			DateUtil.obterNumeroSemanaNoAno(DateUtil.parseDataPTBR("04/01/2012"), Calendar.TUESDAY);
		
		Assert.assertEquals(2, numeroSemana);		
	}
	
	@Test
	public void obterDataDaDecimaSextaSemanaDoAno2012() {
		
		Date dataEsperada = DateUtil.parseDataPTBR("17/04/2012");
		
		Date dataObtida = DateUtil.obterDataDaSemanaNoAno(16, Calendar.WEDNESDAY);
		
		Assert.assertEquals(dataEsperada, dataObtida);
	}

}
