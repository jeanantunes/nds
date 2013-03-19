package br.com.abril.nds.process.jornaleirosnovos;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.dataprovider.JornaleirosNovosVendaMediaDataProvider;

public class JornaleirosNovosVendaMediaTest {

    @Autowired
    private JornaleirosNovos jornaleirosNovos;
    
    /**
     * Testar venda média corrigida com Cota Nova com a quantidade de Edição Base menor ou igual a três e se existe Equivalente com a Venda Média Corrigida maior do que
     * zero.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaNovaComQtdeEdicaoBaseMenorIgualTresComEquivalenteVendaMediaCorrigidaMaiorZeroList",
	  dataProviderClass = JornaleirosNovosVendaMediaDataProvider.class)
    public void cotaNovaComQuantidadeEdicaoBaseMenorIgualTresComEquivalenteVendaMediaCorrigidaMaiorZero(Cota cota) {

	try {

	    assertNotNull(cota.getEquivalente(), " Cota : " + cota.getId() + " nao contem cota base equivalente ");

	    BigDecimal vendaMediaCorrigida = cota.getVendaMedia();

	    assertNotNull(vendaMediaCorrigida, "Venda Media Corrigida : " + vendaMediaCorrigida + " Cota : " + cota.getId());

	    StringBuilder sbReporterLog = gerarReporterLog(new StringBuilder(), cota, vendaMediaCorrigida);

	    jornaleirosNovos.setGenericDTO(cota);
	    jornaleirosNovos.executar();

	    cota = (Cota) jornaleirosNovos.getGenericDTO();

	    BigDecimal indiceAjusteEquivalente = cota.getIndiceAjusteEquivalente();
	    BigDecimal vendaMediaCorrigidaNovo = cota.getVendaMedia();

	    assertNotNull(indiceAjusteEquivalente, "Indice Ajuste Equivalente : " + indiceAjusteEquivalente + " Cota : " + cota.getId());
	    assertNotNull(vendaMediaCorrigidaNovo, "Venda Media Corrigida Novo : " + vendaMediaCorrigidaNovo + " Cota : " + cota.getId());

	    Iterator<Cota> itEquivalente = cota.getEquivalente().iterator();

	    while (itEquivalente.hasNext()) {

		Cota cotaEquivalente = itEquivalente.next();

		assertNotNull(cotaEquivalente, "Cota Equivalente : " + cotaEquivalente + " Cota : " + cota.getId());

		sbReporterLog = gerarCotaEquivalenteLog(sbReporterLog, cotaEquivalente);
	    }

	    sbReporterLog = gerarReporterVendaMediaLog(sbReporterLog, cota, indiceAjusteEquivalente, vendaMediaCorrigidaNovo);

	    Reporter.log(sbReporterLog.toString());

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar venda média corrigida com Cota Nova com a quantidade de Edição Base menor ou igual a três e sem Equivalente com a Venda Média Corrigida maior do que
     * zero.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaNovaComQtdeEdicaoBaseMenorIgualTresSemEquivalenteVendaMediaCorrigidaMaiorZeroList",
	  dataProviderClass = JornaleirosNovosVendaMediaDataProvider.class)
    public void cotaNovaComQuantidadeEdicaoBaseMenorIgualTresSemEquivalenteVendaMediaCorrigidaMaiorZero(Cota cota) {

	try {

	    assertNotNull(cota.getEquivalente(), " Cota : " + cota.getId() + " nao contem cota base equivalente ");

	    BigDecimal vendaMediaCorrigida = cota.getVendaMedia();

	    assertNotNull(vendaMediaCorrigida, "Venda Media Corrigida : " + vendaMediaCorrigida + " Cota : " + cota.getId());

	    StringBuilder sbReporterLog = gerarReporterLog(new StringBuilder(), cota, vendaMediaCorrigida);

	    cota = (Cota) jornaleirosNovos.getGenericDTO();
	    jornaleirosNovos.executar();

	    cota = (Cota) jornaleirosNovos.getGenericDTO();

	    BigDecimal indiceAjusteEquivalente = cota.getIndiceAjusteEquivalente();
	    BigDecimal vendaMediaCorrigidaNovo = cota.getVendaMedia();

	    assertNotNull(indiceAjusteEquivalente, "Indice Ajuste Equivalente : " + indiceAjusteEquivalente + " Cota : " + cota.getId());
	    assertNotNull(vendaMediaCorrigidaNovo, "Venda Media Corrigida Novo : " + vendaMediaCorrigidaNovo + " Cota : " + cota.getId());

	    Iterator<Cota> itEquivalente = cota.getEquivalente().iterator();

	    while (itEquivalente.hasNext()) {

		Cota cotaEquivalente = itEquivalente.next();

		assertNotNull(cotaEquivalente, "Cota Equivalente : " + cotaEquivalente + " Cota : " + cota.getId());

		sbReporterLog = gerarCotaEquivalenteLog(sbReporterLog, cotaEquivalente);
	    }

	    sbReporterLog = gerarReporterVendaMediaLog(sbReporterLog, cota, indiceAjusteEquivalente, vendaMediaCorrigidaNovo);

	    Reporter.log(sbReporterLog.toString());

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar venda média corrigida com Cota Nova com a quantidade de Edição Base maior que três.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaNovaComQtdeEdicaoBaseMaiorTresList", dataProviderClass = JornaleirosNovosVendaMediaDataProvider.class)
    public void cotaNovaComQuantidadeEdicaoBaseMaiorTres(Cota cota) {

	try {

	    assertNotNull(cota.getEquivalente(), " Cota : " + cota.getId() + " nao contem cota base equivalente ");

	    BigDecimal vendaMediaCorrigida = cota.getVendaMedia();

	    assertNotNull(vendaMediaCorrigida, "Venda Media Corrigida : " + vendaMediaCorrigida + " Cota : " + cota.getId());

	    StringBuilder sbReporterLog = gerarReporterLog(new StringBuilder(), cota, vendaMediaCorrigida);

	    cota = (Cota) jornaleirosNovos.getGenericDTO();
	    jornaleirosNovos.executar();

	    cota = (Cota) jornaleirosNovos.getGenericDTO();

	    BigDecimal indiceAjusteEquivalente = cota.getIndiceAjusteEquivalente();
	    BigDecimal vendaMediaCorrigidaNovo = cota.getVendaMedia();

	    assertNotNull(indiceAjusteEquivalente, "Indice Ajuste Equivalente : " + indiceAjusteEquivalente + " Cota : " + cota.getId());
	    assertNotNull(vendaMediaCorrigidaNovo, "Venda Media Corrigida Novo : " + vendaMediaCorrigidaNovo + " Cota : " + cota.getId());

	    Iterator<Cota> itEquivalente = cota.getEquivalente().iterator();

	    while (itEquivalente.hasNext()) {

		Cota cotaEquivalente = itEquivalente.next();

		assertNotNull(cotaEquivalente, "Cota Equivalente : " + cotaEquivalente + " Cota : " + cota.getId());

		sbReporterLog = gerarCotaEquivalenteLog(sbReporterLog, cotaEquivalente);
	    }

	    sbReporterLog = gerarReporterVendaMediaLog(sbReporterLog, cota, indiceAjusteEquivalente, vendaMediaCorrigidaNovo);

	    Reporter.log(sbReporterLog.toString());

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private StringBuilder gerarReporterLog(StringBuilder sbReporterLog, Cota cota, BigDecimal vendaMediaCorrigida) {
	sbReporterLog.append("<p>Cota </p>");
	sbReporterLog.append("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	sbReporterLog.append("<p style='margin-left: 50px'>Atual </p>");
	sbReporterLog.append("<p style='margin-left: 100px'>-> Venda Media Corrigida : " + vendaMediaCorrigida + "</p>");
	return sbReporterLog;
    }

    private StringBuilder gerarCotaEquivalenteLog(StringBuilder sbReporterLog, Cota cota) {
	return sbReporterLog.append("<p style='margin-left: 150px'>Cota Equivalente : " + cota.getId() + "</p>");
    }

    private StringBuilder gerarReporterVendaMediaLog(StringBuilder sbReporterLog, Cota cota, BigDecimal indiceAjusteEquivalente, BigDecimal vendaMediaCorrigidaNovo) {
	sbReporterLog.append("<p style='margin-left: 50px'>Novo </p>");
	sbReporterLog.append("<p style='margin-left: 100px'>-> Indice Ajuste Equivalente : " + indiceAjusteEquivalente + "</p>");
	sbReporterLog.append("<p style='margin-left: 100px'>-> Venda Media Corrigida Novo : " + vendaMediaCorrigidaNovo + "</p>");
	return sbReporterLog;
    }

}
