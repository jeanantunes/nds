package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.dataprovider.CorrecaoVendasDataProvider;

public class CorrecaoVendasTest {

    /**
     * Testar os índices de correção de venda de quantidade edicao base maior do que um e nao primeira edicao.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaEdicaoNaoPrimeiraEdicaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void quantidadeEdicaoBaseMaiorUmNaoPrimeiraEdicao(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();
	    
	    assertNotNull(" Cota : " + cota.getId() + "nao contem edicao base ", cota.getEdicoesRecebidas());

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

		assertNotNull("Indice Correcao : " + indiceCorrecao + " Cota : " + cota.getId() + " Produto Edicao : " + produtoEdicao.getId() + " Produto : "
			+ produtoEdicao.getIdProduto(), indiceCorrecao);

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    assertNotNull("Indice Correcao Tendencia : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceCorrecaoTendencia);
	    assertNotNull("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceVendaCrescente);

	    gerarReporterLog(cota, sbReporterLog, indiceCorrecaoTendencia, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices de correção de venda de quantidade edicao base maior do que um e primeira edicao.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaEdicaoPrimeiraEdicaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void quantidadeEdicaoBaseMaiorUmPrimeiraEdicao(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    assertNotNull(" Cota : " + cota.getId() + "nao contem edicao base ", cota.getEdicoesRecebidas());

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

		assertNotNull("Indice Correcao : " + indiceCorrecao + " Cota : " + cota.getId() + " Produto Edicao : " + produtoEdicao.getId() + " Produto : "
			+ produtoEdicao.getIdProduto(), indiceCorrecao);

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    assertNotNull("Indice Correcao Tendencia : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceCorrecaoTendencia);
	    assertNotNull("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceVendaCrescente);

	    gerarReporterLog(cota, sbReporterLog, indiceCorrecaoTendencia, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices de correção de venda de quantidade edicao base maior do que um e primeira edicao e coleção.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaEdicaoPrimeiraEdicaoColecaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void quantidadeEdicaoBaseMaiorUmPrimeiraEdicaoColecao(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();
	    
	    assertNotNull(" Cota : " + cota.getId() + "nao contem edicao base ", cota.getEdicoesRecebidas());

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

		assertNotNull("Indice Correcao : " + indiceCorrecao + " Cota : " + cota.getId() + " Produto Edicao : " + produtoEdicao.getId() + " Produto : "
			+ produtoEdicao.getIdProduto(), indiceCorrecao);

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    assertNotNull("Indice Correcao Tendencia : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceCorrecaoTendencia);
	    assertNotNull("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceVendaCrescente);

	    gerarReporterLog(cota, sbReporterLog, indiceCorrecaoTendencia, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices de correção de venda de quantidade edicao base maior do que um e primeira edicao e não coleção.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaEdicaoNaoPrimeiraEdicaoNaoColecaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void quantidadeEdicaoBaseMaiorUmNaoPrimeiraEdicaoNaoColecao(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();
	    
	    assertNotNull(" Cota : " + cota.getId() + "nao contem edicao base ", cota.getEdicoesRecebidas());

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

		assertNotNull("Indice Correcao : " + indiceCorrecao + " Cota : " + cota.getId() + " Produto Edicao : " + produtoEdicao.getId() + " Produto : "
			+ produtoEdicao.getIdProduto(), indiceCorrecao);

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    assertNotNull("Indice Correcao Tendencia : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceCorrecaoTendencia);
	    assertNotNull("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceVendaCrescente);

	    gerarReporterLog(cota, sbReporterLog, indiceCorrecaoTendencia, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices de correção de venda com quantidade de edicoes fechadas maior ou igual a quatro.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaEdicaoFechadasList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void quatroOuMaisEdicoesFechadasList(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();
	    
	    assertNotNull(" Cota : " + cota.getId() + "nao contem edicao base ", cota.getEdicoesRecebidas());

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

		assertNotNull("Indice Correcao : " + indiceCorrecao + " Cota : " + cota.getId() + " Produto Edicao : " + produtoEdicao.getId() + " Produto : "
			+ produtoEdicao.getIdProduto(), indiceCorrecao);

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    assertNotNull("Indice Correcao Tendencia : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceCorrecaoTendencia);
	    assertNotNull("Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId(), indiceVendaCrescente);

	    gerarReporterLog(cota, sbReporterLog, indiceCorrecaoTendencia, indiceVendaCrescente);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void gerarReporterLog(Cota cota, StringBuilder sbReporterLog, BigDecimal indiceCorrecaoTendencia, BigDecimal indiceVendaCrescente) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : " + indiceVendaCrescente + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao Tendencia : " + indiceCorrecaoTendencia + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

    private void gerarProdutoEdicaoLog(StringBuilder sbReporterLog, ProdutoEdicao produtoEdicao) {
	sbReporterLog.append("<p style='margin-left: 100px'>Produto Edicao : " + produtoEdicao.getId() + "</p>");
	sbReporterLog.append("<p style='margin-left: 100px'>Produto : " + produtoEdicao.getIdProduto() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Aberta : " + produtoEdicao.isEdicaoAberta() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Reparte : " + produtoEdicao.getReparte() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Venda : " + produtoEdicao.getVenda() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>-> Indice de Correcao : " + produtoEdicao.getIndiceCorrecao() + "</p>");
    }

}
