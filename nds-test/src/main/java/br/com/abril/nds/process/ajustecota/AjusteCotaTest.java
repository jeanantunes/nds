package br.com.abril.nds.process.ajustecota;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.math.BigDecimal;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.dataprovider.AjusteCotaDataProvider;

public class AjusteCotaTest {

    /**
     * Testar índice de ajuste de cota sem segmento.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaSemIndiceAjusteSegmentoList", dataProviderClass = AjusteCotaDataProvider.class)
    public void indiceAjusteCota(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edições ");

	    AjusteCota ajusteCota = new AjusteCota(cota);
	    ajusteCota.executar();

	    cota = (Cota) ajusteCota.getGenericDTO();

	    BigDecimal indiceAjuste = cota.getIndiceAjusteCota();

	    assertNotNull(indiceAjuste, "Indice Ajuste : " + indiceAjuste + " Cota : " + cota.getId());

	    gerarReporterLog(cota, sbReporterLog, indiceAjuste);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar índice de ajuste de cota com segmento.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComIndiceAjusteSegmentoList", dataProviderClass = AjusteCotaDataProvider.class)
    public void indiceAjusteCotaSegmento(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edições ");

	    AjusteCota ajusteCota = new AjusteCota(cota);
	    ajusteCota.executar();

	    cota = (Cota) ajusteCota.getGenericDTO();

	    BigDecimal indiceAjuste = cota.getIndiceAjusteCota();

	    assertNotNull(indiceAjuste, "Indice Ajuste : " + indiceAjuste + " Cota : " + cota.getId());

	    gerarReporterLog(cota, sbReporterLog, indiceAjuste);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar índice de ajuste de cota com segmento menor.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComIndiceAjusteSegmentoMenorList", dataProviderClass = AjusteCotaDataProvider.class)
    public void indiceAjusteCotaSegmentoMenor(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edições ");

	    AjusteCota ajusteCota = new AjusteCota(cota);
	    ajusteCota.executar();

	    cota = (Cota) ajusteCota.getGenericDTO();

	    BigDecimal indiceAjuste = cota.getIndiceAjusteCota();

	    assertNotNull(indiceAjuste, "Indice Ajuste : " + indiceAjuste + " Cota : " + cota.getId());

	    gerarReporterLog(cota, sbReporterLog, indiceAjuste);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar índice de ajuste de cota menor.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComIndiceAjusteMenorList", dataProviderClass = AjusteCotaDataProvider.class)
    public void indiceAjusteCotaMenor(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edições ");

	    AjusteCota ajusteCota = new AjusteCota(cota);
	    ajusteCota.executar();

	    cota = (Cota) ajusteCota.getGenericDTO();

	    BigDecimal indiceAjuste = cota.getIndiceAjusteCota();

	    assertNotNull(indiceAjuste, "Indice Ajuste : " + indiceAjuste + " Cota : " + cota.getId());

	    gerarReporterLog(cota, sbReporterLog, indiceAjuste);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void gerarReporterLog(Cota cota, StringBuilder sbReporterLog, BigDecimal indiceAjuste) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Ajuste : " + indiceAjuste + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

}
