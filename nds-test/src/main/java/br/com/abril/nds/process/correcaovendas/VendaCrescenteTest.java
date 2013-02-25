package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.dataprovider.VendasCrescenteDataProvider;

public class VendaCrescenteTest {

    /**
     * Testar se o índice de venda crescente será 1 com uma única publicacão e com menos de quatro edições.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaParaUnicaPublicacaoMenosDeQuatroEdicoesList", dataProviderClass = VendasCrescenteDataProvider.class)
    public void unicaPublicacaoMenosDeQuatroEdicoes(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(" Cota : " + cota.getId() + " nao contem edicao base ", cota.getEdicoesRecebidas());
	    
	    VendaCrescente vendaCrescente = new VendaCrescente(cota);
	    vendaCrescente.executar();

	    cota = (Cota) vendaCrescente.getGenericDTO();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    boolean assertIndiceVendaCrescente = (indiceVendaCrescente != null && (indiceVendaCrescente.compareTo(BigDecimal.ONE) == 0));

	    assertTrue("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), assertIndiceVendaCrescente);
	    gerarReporterLog(cota, sbReporterLog, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar se o índice de venda crescente será 1 com uma única publicacão, quatro edições e sem edições fechadas.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaParaUnicaPublicacaoQuatroEdicoesSemEdicoesFechadaList", dataProviderClass = VendasCrescenteDataProvider.class)
    public void unicaPublicacaoQuatroEdicoesSemEdicoesFechada(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(" Cota : " + cota.getId() + " nao contem edicao base ", cota.getEdicoesRecebidas());
	    
	    VendaCrescente vendaCrescente = new VendaCrescente(cota);
	    vendaCrescente.executar();

	    cota = (Cota) vendaCrescente.getGenericDTO();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    boolean assertIndiceVendaCrescente = (indiceVendaCrescente != null && (indiceVendaCrescente.compareTo(BigDecimal.ONE) == 0));

	    assertTrue("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), assertIndiceVendaCrescente);
	    gerarReporterLog(cota, sbReporterLog, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
    
    /**
     * Testar se o índice de venda crescente será 1.1 com uma única publicacão, quatro edições e com edições fechadas.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaParaUnicaPublicacaoQuatroEdicoesComEdicoesFechadaList", dataProviderClass = VendasCrescenteDataProvider.class)
    public void unicaPublicacaoQuatroEdicoesComEdicoesFechada(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(" Cota : " + cota.getId() + " nao contem edicao base ", cota.getEdicoesRecebidas());
	    
	    VendaCrescente vendaCrescente = new VendaCrescente(cota);
	    vendaCrescente.executar();

	    cota = (Cota) vendaCrescente.getGenericDTO();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();
		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    assertNotNull(indiceVendaCrescente);
	    
	    BigDecimal oneDotOne = BigDecimal.ONE.add(new BigDecimal(0.1)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

	    boolean assertIndiceVendaCrescente = (indiceVendaCrescente.compareTo(oneDotOne) == 0);

	    assertTrue("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), assertIndiceVendaCrescente);
	    gerarReporterLog(cota, sbReporterLog, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void gerarReporterLog(Cota cota, StringBuilder sbReporterLog, BigDecimal indiceVendaCrescente) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : " + indiceVendaCrescente + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

    private void gerarProdutoEdicaoLog(StringBuilder sbReporterLog, ProdutoEdicao produtoEdicao) {
	sbReporterLog.append("<p style='margin-left: 100px'>Produto Edicao : " + produtoEdicao.getId() + "</p>");
	sbReporterLog.append("<p style='margin-left: 100px'>Produto : " + produtoEdicao.getIdProduto() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Aberta : " + produtoEdicao.isEdicaoAberta() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Reparte : " + produtoEdicao.getReparte() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Venda : " + produtoEdicao.getVenda() + "</p>");
    }
}
