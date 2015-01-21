package br.com.abril.nds.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class RoundingCurrencyUtilTest {
	
	@Test
	public void testCalculoSimples() {
		
		BigDecimal precoVenda = new BigDecimal(7.99);
		BigDecimal desconto = new BigDecimal(23.00);
		BigDecimal reparte = new BigDecimal(5.0);
		
		Assert.assertEquals(BigDecimal.valueOf(9.1885), CurrencyUtil.calculoValores(precoVenda, desconto, reparte));
		
		Assert.assertEquals(BigDecimal.valueOf(15.3295), CurrencyUtil.calculoValores(new BigDecimal(13.33), desconto, reparte));
		
		Assert.assertEquals(BigDecimal.valueOf(24.9205), CurrencyUtil.calculoValores(new BigDecimal(21.67), desconto, reparte));
		
		Assert.assertEquals(BigDecimal.valueOf(381.4205), CurrencyUtil.calculoValores(new BigDecimal(331.67), desconto, reparte));
		
	}
	
	@Test
	public void testCalculoValorTotal() {
		
		List<PrecoReparte> listaValores = new ArrayList<>();
		
		PrecoReparte precoReparte1 = new PrecoReparte();
		precoReparte1.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte1.setQuantidade(new BigDecimal(2.0));
		listaValores.add(precoReparte1);
		
		PrecoReparte precoReparte2 = new PrecoReparte();
		precoReparte2.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte2.setQuantidade(new BigDecimal(1.0));
		listaValores.add(precoReparte2);
		
		PrecoReparte precoReparte3 = new PrecoReparte();
		precoReparte3.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte3.setQuantidade(new BigDecimal(5.0));
		listaValores.add(precoReparte3);
		
		PrecoReparte precoReparte4 = new PrecoReparte();
		precoReparte4.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte4.setQuantidade(new BigDecimal(6.0));
		listaValores.add(precoReparte4);
		
		PrecoReparte precoReparte5 = new PrecoReparte();
		precoReparte5.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte5.setQuantidade(new BigDecimal(8.0));
		listaValores.add(precoReparte5);
		
		PrecoReparte precoReparte6 = new PrecoReparte();
		precoReparte6.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte6.setQuantidade(new BigDecimal(9.0));
		listaValores.add(precoReparte6);
		
		PrecoReparte precoReparte7 = new PrecoReparte();
		precoReparte7.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte7.setQuantidade(new BigDecimal(2.0));
		listaValores.add(precoReparte7);
		
		PrecoReparte precoReparte8 = new PrecoReparte();
		precoReparte8.setPrecoDesconto(new BigDecimal(10.0000));
		precoReparte8.setQuantidade(new BigDecimal(4.0));
		listaValores.add(precoReparte8);
		
		Assert.assertEquals(BigDecimal.valueOf(732360.00).setScale(2), CurrencyUtil.calculoValorTotal(listaValores));
		
	}

}	