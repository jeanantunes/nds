package br.com.abril.nds.util;

import java.util.Calendar;

import org.junit.Test;

public class UtilTest {
    
	@Test
    public void gerarNossoNumeroBoleto() {
		
		Integer numeroCota = 29;
		
		Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_MONTH, 27);
    	c.set(Calendar.MONTH, 4);
    	c.set(Calendar.YEAR, 2015);
//		String auxData = DateUtil.formatarData(c.getTime(), "ddMMyy");
		
		String numeroBanco = "399";
		Long idFornecedor = 1L;
		Long idDivida = 2L;
		Long agencia = 454L;
		Long contaCorrente = 1646L;
		String codigoCedente = "3775356";
		String codigoSacado = numeroCota.toString();
		Integer carteira = 2;
		
		String nossoNumero = "0000000000013";
//		String nossoNumero = Util.gerarNossoNumero(numeroCota, c.getTime(), numeroBanco, idFornecedor, idDivida, agencia, contaCorrente, carteira);
		String digitoVerificador = Util.calcularDigitoVerificador(nossoNumero, codigoCedente, c.getTime(), numeroBanco);
		System.out.println("NN: "+ nossoNumero);
    	System.out.println("NN+DV: "+ nossoNumero +"-"+ digitoVerificador);
	}
	
//    @Test
    public void calcularDigitoVerificadorNossoNumeroBoleto() {
        
    	String numeroBanco = "004";
    	String codigoCedente = "3775356";
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_MONTH, 27);
    	c.set(Calendar.MONTH, 4);
    	c.set(Calendar.YEAR, 2015);
    	System.out.println(c.getTime());
    	
    	String nossoNumero = "0000000000007";
    	String digitoVerificador = Util.calcularDigitoVerificador(nossoNumero, codigoCedente, c.getTime(), numeroBanco);
    	
    	System.out.println(digitoVerificador);
    }
    
}