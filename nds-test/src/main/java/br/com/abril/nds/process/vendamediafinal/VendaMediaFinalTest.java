package br.com.abril.nds.process.vendamediafinal;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.dataprovider.VendaMediaFinalDataProvider;

public class VendaMediaFinalTest {

    @Autowired
    private VendaMediaFinal vendaMediaFinal;
    
    /**
     * Testar se a venda médias final é calculada conforme a cota.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaParaCalculoList", dataProviderClass = VendaMediaFinalDataProvider.class)
    public void calcular(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    vendaMediaFinal.setGenericDTO(cota);
	    vendaMediaFinal.executar();

	    BigDecimal vendaMediaFinalValue = vendaMediaFinal.getValue();

	    boolean assertVendaMedia = (vendaMediaFinalValue != null && (vendaMediaFinalValue.compareTo(BigDecimal.ZERO) == 1));

	    assertTrue(assertVendaMedia, "Venda Media Final : " + assertVendaMedia + " Cota : " + cota.getId());
	    gerarReporterLog(cota, sbReporterLog, vendaMediaFinalValue);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void gerarReporterLog(Cota cota, StringBuilder sbReporterLog, BigDecimal vendaMediaFinal) {

	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Venda Media Final : " + vendaMediaFinal + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Venda Media : " + cota.getVendaMedia() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Ajuste : " + cota.getIndiceAjusteCota() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : " + cota.getIndiceVendaCrescente() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Tratamento Regional : " + cota.getIndiceTratamentoRegional() + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

}
