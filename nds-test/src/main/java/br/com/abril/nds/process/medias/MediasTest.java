package br.com.abril.nds.process.medias;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class MediasTest {

    /**
     * Testar se a venda médias é calculada conforme a quantidade de edições menor que três.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaQuantidadeEdicoesMenorTresList", dataProviderClass = MediasDataProvider.class)
    public void quantidadeEdicaoMenorTres(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    Medias medias = new Medias(cota);
	    medias.executar();

	    cota = (Cota) medias.getGenericDTO();
	    
	    BigDecimal vendaMediaFinal = cota.getVendaMediaFinal();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal vendaMedia = cota.getVendaMedia();

	    boolean assertVendaMedia = (vendaMedia != null && (vendaMedia.compareTo(BigDecimal.ZERO) == 1));

	    assertTrue("Venda Media : " + assertVendaMedia + " Cota : " + cota.getId(), assertVendaMedia);
	    gerarReporterLog(cota, sbReporterLog, vendaMedia);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
    
    /**
     * Testar se a venda médias é calculada conforme a quantidade de edições menor que três.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaQuantidadeEdicoesMaiorIgualTresList", dataProviderClass = MediasDataProvider.class)
    public void quantidadeEdicaoMaiorIgualTres(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    Medias medias = new Medias(cota);
	    medias.executar();

	    cota = (Cota) medias.getGenericDTO();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal vendaMedia = cota.getVendaMedia();

	    boolean assertVendaMedia = (vendaMedia != null && (vendaMedia.compareTo(BigDecimal.ZERO) == 1));

	    assertTrue("Venda Media : " + assertVendaMedia + " Cota : " + cota.getId(), assertVendaMedia);
	    gerarReporterLog(cota, sbReporterLog, vendaMedia);

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

    private void gerarProdutoEdicaoLog(StringBuilder sbReporterLog, ProdutoEdicao produtoEdicao) {
	sbReporterLog.append("<p style='margin-left: 100px'>Produto Edicao : " + produtoEdicao.getId() + "</p>");
	sbReporterLog.append("<p style='margin-left: 100px'>Produto : " + produtoEdicao.getIdProduto() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Venda Corrigida : " + produtoEdicao.getVendaCorrigida() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Peso : " + produtoEdicao.getPeso() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Reparte : " + produtoEdicao.getReparte() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Venda : " + produtoEdicao.getVenda() + "</p>");
    }

}
