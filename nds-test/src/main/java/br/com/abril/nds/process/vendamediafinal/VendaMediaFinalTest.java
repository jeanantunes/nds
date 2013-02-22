package br.com.abril.nds.process.vendamediafinal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.dataprovider.VendaMediaFinalDataProvider;

public class VendaMediaFinalTest {

    /**
     * Testar se a venda médias final é calculada conforme a cota.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaParaCalculoList", dataProviderClass = VendaMediaFinalDataProvider.class)
    public void calcular(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    VendaMediaFinal vendaMediaFinal = new VendaMediaFinal(cota);
	    vendaMediaFinal.executar();
	    
	    BigDecimal vendaMediaFinalValue = vendaMediaFinal.getValue();
	    
	    boolean assertVendaMedia = (vendaMediaFinalValue != null && (vendaMediaFinalValue.compareTo(BigDecimal.ZERO) == 1));

	    assertTrue("Venda Media Final : " + assertVendaMedia + " Cota : " + cota.getId(), assertVendaMedia);
	    gerarReporterLog(cota, sbReporterLog, vendaMediaFinalValue);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
    
    private void gerarReporterLog(Cota cota, StringBuilder sbReporterLog, BigDecimal vendaMedia) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Venda Media: " + vendaMedia + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

}
