package br.com.abril.nds.util;

import java.util.Calendar;

import org.junit.Test;

public class UtilTest {
    
    @Test
    public void calcularDigitoVerificadorNossoNumeroBoleto() {
        
    	String codigoCedente = "3775356";
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_MONTH, 27);
    	c.set(Calendar.MONTH, 4);
    	c.set(Calendar.YEAR, 2015);
    	System.out.println(c.getTime());
    	
    	String nossoNumero = "0000000000007";
    	String digitoVerificador = Util.calcularDigitoVerificador(nossoNumero, codigoCedente, c.getTime());
    	
    	
    	System.out.println(digitoVerificador);
    }
    
}