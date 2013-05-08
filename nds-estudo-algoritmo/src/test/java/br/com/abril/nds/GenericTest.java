package br.com.abril.nds;

import java.math.BigInteger;

import org.junit.Test;

//@Ignore
public class GenericTest {

	@Test
	public void test() {
	    BigInteger teste = BigInteger.valueOf(-4);
	    BigInteger dois = BigInteger.valueOf(5);
	    BigInteger resultado = dois.subtract(teste);
	    System.out.println(resultado);
	}
	
}
