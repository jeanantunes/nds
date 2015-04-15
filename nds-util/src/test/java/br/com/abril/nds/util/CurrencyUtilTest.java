package br.com.abril.nds.util;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

public class CurrencyUtilTest {
	
	@Test
	public void testValorExtensoSimples() {
		BigDecimal valor = BigDecimal.TEN;
		String extenso = CurrencyUtil.valorExtenso(valor);
		String expected = "dez reais";
		Assert.assertEquals(expected, extenso);
	}
	
	@Test
	public void testValorExtensoComplexo() {
		BigDecimal valor = new BigDecimal(1234.59);
		String extenso = CurrencyUtil.valorExtenso(valor);
		String expected = "um mil e duzentos e trinta e quatro reais e cinquenta e nove centavos";
		Assert.assertEquals(expected, extenso);
	}
}
