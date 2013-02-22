package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.dataprovider.CorrecaoTendenciaDataProvider;

public class CorrecaoTendenciaTest {

    /**
     * Testar se o índice de correção será 1.
     * 
     * @param cota
     * @param totalReparte
     * @param totalVenda
     */
    @Test(dataProvider = "getCotaParaPercentualVendaNaoIgualUmNaoMaiorIgualZeroVirgulaNoveList", dataProviderClass = CorrecaoTendenciaDataProvider.class)
    public void percentualVendaNaoIgualUmNaoIgualMaiorZeroVirgualNove(Cota cota, BigDecimal totalReparte, BigDecimal totalVenda) {

	try {

	    CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota, totalReparte, totalVenda);
	    correcaoTendencia.executar();

	    cota = (Cota) correcaoTendencia.getGenericDTO();

	    assertNotNull(cota);

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < cota.getEdicoesRecebidas().size()) {

		ProdutoEdicao produtoEdicao = cota.getEdicoesRecebidas().get(iEdicaoBase);
		gerarProdutoEdicaoLog(sbEstoqueLog, produtoEdicao);
		iEdicaoBase++;
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    assertNotNull(indiceCorrecaoTendencia);

	    boolean assertIndiceCorrecaoTendencia = (indiceCorrecaoTendencia.compareTo(BigDecimal.ONE) == 0);
	    assertTrue("Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId(), assertIndiceCorrecaoTendencia);

	    gerarCotaReporterLog(cota, sbEstoqueLog, indiceCorrecaoTendencia);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar se o índice de correção será 1.2.
     * 
     * @param cota
     * @param totalReparte
     * @param totalVenda
     */
    @Test(dataProvider = "getEstoqueProdutoCotaParaPercentualVendaIgualUmList", dataProviderClass = CorrecaoTendenciaDataProvider.class)
    public void percentualVendaIgualUm(Cota cota, BigDecimal totalReparte, BigDecimal totalVenda) {

	try {

	    CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota, totalReparte, totalVenda);
	    correcaoTendencia.executar();

	    cota = (Cota) correcaoTendencia.getGenericDTO();

	    assertNotNull(cota);

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < cota.getEdicoesRecebidas().size()) {

		ProdutoEdicao produtoEdicao = cota.getEdicoesRecebidas().get(iEdicaoBase);
		gerarProdutoEdicaoLog(sbEstoqueLog, produtoEdicao);
		iEdicaoBase++;
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();

	    BigDecimal oneDotTwo = BigDecimal.ONE.add(new BigDecimal(0.2)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

	    assertNotNull(indiceCorrecaoTendencia);

	    boolean assertIndiceCorrecaoTendencia = (indiceCorrecaoTendencia.compareTo(oneDotTwo) == 0);

	    assertTrue("Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId(), assertIndiceCorrecaoTendencia);

	    gerarCotaReporterLog(cota, sbEstoqueLog, indiceCorrecaoTendencia);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar se o índice de correção será 1.1.
     * 
     * @param cota
     * @param totalReparte
     * @param totalVenda
     */
    @Test(dataProvider = "getEstoqueProdutoCotaParaPercentualVendaMaiorIgualZeroVirgulaNoveList", dataProviderClass = CorrecaoTendenciaDataProvider.class)
    public void percentualVendaMaiorIgualZeroVirgulaNove(Cota cota, BigDecimal totalReparte, BigDecimal totalVenda) {

	try {

	    CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota, totalReparte, totalVenda);
	    correcaoTendencia.executar();

	    cota = (Cota) correcaoTendencia.getGenericDTO();

	    assertNotNull(cota);

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < cota.getEdicoesRecebidas().size()) {

		ProdutoEdicao produtoEdicao = cota.getEdicoesRecebidas().get(iEdicaoBase);
		gerarProdutoEdicaoLog(sbEstoqueLog, produtoEdicao);
		iEdicaoBase++;
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();

	    BigDecimal oneDotOne = BigDecimal.ONE.add(new BigDecimal(0.1)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

	    assertNotNull(indiceCorrecaoTendencia);

	    boolean assertIndiceCorrecaoTendencia = (indiceCorrecaoTendencia.compareTo(oneDotOne) == 0);

	    assertTrue("Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId(), assertIndiceCorrecaoTendencia);

	    gerarCotaReporterLog(cota, sbEstoqueLog, indiceCorrecaoTendencia);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
    
    private void gerarProdutoEdicaoLog(StringBuilder sbEstoqueLog, ProdutoEdicao produtoEdicao) {
	sbEstoqueLog.append("<p style='margin-left: 100px'>Produto Edicao </p>");
	sbEstoqueLog.append("<p style='margin-left: 150px'>ID : " + produtoEdicao.getId() + "</p>");
	sbEstoqueLog.append("<p style='margin-left: 150px'>Quantidade Recebida : " + produtoEdicao.getReparte() + "</p>");
	sbEstoqueLog.append("<p style='margin-left: 150px'>Venda : " + produtoEdicao.getVenda() + "</p>");
    }
    
    private void gerarCotaReporterLog(Cota cota, StringBuilder sbEstoqueLog, BigDecimal indiceCorrecaoTendencia) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao Tendencia : " + indiceCorrecaoTendencia + "</p>");
	Reporter.log(sbEstoqueLog.toString());
    }
}
