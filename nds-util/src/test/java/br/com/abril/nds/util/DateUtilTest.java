package br.com.abril.nds.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
	public void obterDataDaPrimeiraSemanaDoAno2012() {
		
		Date dataEsperada = DateUtil.parseDataPTBR("01/01/2012");
		
		Date dataObtida = DateUtil.obterDataDaSemanaNoAno(1, Calendar.SUNDAY, null);
		
		Assert.assertEquals(dataEsperada, dataObtida);
	}
	
	@Test
	public void obterDiaDaSemana() {
		
		Date data = DateUtil.parseDataPTBR("18/04/2012");
		
		int diaDaSemana = DateUtil.obterDiaDaSemana(data);
		
		Assert.assertEquals(Calendar.WEDNESDAY, diaDaSemana);
	}
	
	@Test
	public void obterPeriodoDeAcordoComDiasDaSemana() {
		
		Date dataInicial = DateUtil.parseDataPTBR("15/04/2012");
		Date dataFinal = DateUtil.parseDataPTBR("21/04/2012");
		
		List<Integer> listaCodigosDiasSemana = new ArrayList<Integer>();
		
		listaCodigosDiasSemana.add(Calendar.MONDAY);
		listaCodigosDiasSemana.add(Calendar.TUESDAY);
		listaCodigosDiasSemana.add(Calendar.THURSDAY);
		listaCodigosDiasSemana.add(Calendar.SUNDAY);
		
		Set<Date> periodo = 
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(
				dataInicial, dataFinal, listaCodigosDiasSemana);
		
		Set<Date> periodoEsperado = new TreeSet<Date>();
		
		periodoEsperado.add(DateUtil.parseDataPTBR("15/04/2012"));
		periodoEsperado.add(DateUtil.parseDataPTBR("16/04/2012"));
		periodoEsperado.add(DateUtil.parseDataPTBR("17/04/2012"));
		periodoEsperado.add(DateUtil.parseDataPTBR("19/04/2012"));
		
		Assert.assertEquals(periodoEsperado, periodo);
	}
	
	@Test
	public void obterDataDaSemana01011999() {
		Integer numeroSemana = 1;
		Integer inicioSemana = 4;
		Date data = DateUtil.parseDataPTBR("01/01/1999");

		Date expected = DateUtil.parseDataPTBR("30/12/1998");
		Date actual = DateUtil.obterDataDaSemanaNoAno(numeroSemana, inicioSemana, data);
		
		Assert.assertEquals(expected, actual);
	}

}
