package br.com.abril.nds.process.medias;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.dataprovider.MediasDataProvider;

public class MediasTest {

    @Autowired
    private Medias medias;

    /**
     * Testar se a venda médias é calculada conforme a quantidade de edições menor que três.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaQuantidadeEdicoesMenorTresList", dataProviderClass = MediasDataProvider.class)
    public void cotaComQuantidadeEdicaoBaseMenorTres(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    medias.setGenericDTO(cota);
	    medias.executar();

	    cota = (Cota) medias.getGenericDTO();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal vendaMedia = cota.getVendaMedia();
	    assertNotNull(vendaMedia, "Venda Media : " + vendaMedia + " Cota : " + cota.getId());
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
    public void cotaComQuantidadeEdicaoBaseMaiorIgualTres(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    medias.setGenericDTO(cota);
	    medias.executar();

	    cota = (Cota) medias.getGenericDTO();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal vendaMedia = cota.getVendaMedia();
	    assertNotNull(vendaMedia, "Venda Media : " + vendaMedia + " Cota : " + cota.getId());
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
