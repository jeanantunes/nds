package br.com.abril.nds.process.jornaleirosnovos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.dataprovider.JornaleirosNovosVendaMediaDataProvider;

public class JornaleirosNovosVendaMediaTest {

    /**
     * Testar venda média corrigida nova.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEquivalenteList", dataProviderClass = JornaleirosNovosVendaMediaDataProvider.class)
    public void cotasNova(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(" Cota : " + cota.getId() + " nao contem cota base equivalente ", cota.getEquivalente());

	    JornaleirosNovos jornaleirosNovos = new JornaleirosNovos(cota);
	    jornaleirosNovos.executar();

	    cota = (Cota) jornaleirosNovos.getGenericDTO();

	    BigDecimal indiceAjusteEquivalente = cota.getIndiceAjusteEquivalente();
	    BigDecimal vendaMediaCorrigidaNovo = cota.getVendaMedia();

	    assertNotNull("Indice Ajuste Equivalente : " + indiceAjusteEquivalente + " Cota : " + cota.getId(), indiceAjusteEquivalente);
	    assertNotNull("Venda Media Corrigida Novo : " + vendaMediaCorrigidaNovo + " Cota : " + cota.getId(), vendaMediaCorrigidaNovo);

	    Iterator<Cota> itEquivalente = cota.getEquivalente().iterator();

	    while (itEquivalente.hasNext()) {

		Cota cotaEquivalente = itEquivalente.next();

		assertNotNull("Cota Equivalente : " + cotaEquivalente + " Cota : " + cota.getId(), cotaEquivalente);

		gerarCotaEquivalenteLog(sbReporterLog, cotaEquivalente);
	    }

	    gerarReporterLog(cota, sbReporterLog, indiceAjusteEquivalente, vendaMediaCorrigidaNovo);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void gerarReporterLog(Cota cota, StringBuilder sbReporterLog, BigDecimal indiceAjusteEquivalente, BigDecimal vendaMediaCorrigidaNovo) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Ajuste Equivalente : " + indiceAjusteEquivalente + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Venda Media Corrigida Novo : " + vendaMediaCorrigidaNovo + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

    private void gerarCotaEquivalenteLog(StringBuilder sbReporterLog, Cota cota) {
	sbReporterLog.append("<p style='margin-left: 100px'>Cota Equivalente : " + cota.getId() + "</p>");
    }

}
