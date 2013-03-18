package br.com.abril.nds.process.correcaovendas;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.dataprovider.CorrecaoVendasDataProvider;

public class CorrecaoVendasTest {

    /**
     * Testar os índices da(s) cota(s) que as edições não encontra(m)-se na primeira edição.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEdicaoNaoPrimeiraEdicaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void cotaComEdicaoNaoPrimeiraEdicaoTest(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edicao base ");

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    int qtdeEdicaoRecebida = cota.getEdicoesRecebidas().size();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		if (!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();
		BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

		StringBuffer sbAssert = new StringBuffer();
		sbAssert.append("\n Indice Correcao : ");
		sbAssert.append(indiceCorrecao);
		sbAssert.append("\n Venda Corrigida :");
		sbAssert.append(vendaCorrigida);
		sbAssert.append("\n Cota : ");
		sbAssert.append(cota.getId());
		sbAssert.append("\n Produto Edicao : ");
		sbAssert.append(produtoEdicao.getId());
		sbAssert.append("\n Produto : ");
		sbAssert.append(produtoEdicao.getIdProduto());
		sbAssert.append("\n Numero Edicao : ");
		sbAssert.append(produtoEdicao.getNumeroEdicao());
		sbAssert.append("\n Colecao : ");
		sbAssert.append(produtoEdicao.isColecao());
		sbAssert.append("\n");

		if ((produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1 && produtoEdicao.isColecao()) || qtdeEdicaoRecebida <= 1) {
		    assertNull(indiceCorrecao, sbAssert.toString());
		    assertNull(vendaCorrigida, sbAssert.toString());
		} else {
		    assertNotNull(indiceCorrecao, sbAssert.toString());
		    assertNotNull(vendaCorrigida, sbAssert.toString());
		}

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    int qtdeEdicaoFechada = listEdicaoFechada.size();

	    if (qtdeEdicaoRecebida > 1)
		assertNotNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());
	    else
		assertNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());

	    if (qtdeEdicaoFechada >= 4 && qtdeEdicaoRecebida > 1)
		assertNotNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());
	    else
		assertNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());

	    gerarReporterLog(cota, qtdeEdicaoFechada, sbReporterLog);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices da(s) cota(s) que as edições não encontra(m)-se como Fascículo/Coleção.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEdicaoFasciculoColecaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void cotaComEdicaoFasciculoColecaoTest(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edicao base ");

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    int qtdeEdicaoRecebida = cota.getEdicoesRecebidas().size();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		if (!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();
		BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

		StringBuffer sbAssert = new StringBuffer();
		sbAssert.append("\n Indice Correcao : ");
		sbAssert.append(indiceCorrecao);
		sbAssert.append("\n Venda Corrigida :");
		sbAssert.append(vendaCorrigida);
		sbAssert.append("\n Cota : ");
		sbAssert.append(cota.getId());
		sbAssert.append("\n Produto Edicao : ");
		sbAssert.append(produtoEdicao.getId());
		sbAssert.append("\n Produto : ");
		sbAssert.append(produtoEdicao.getIdProduto());
		sbAssert.append("\n Numero Edicao : ");
		sbAssert.append(produtoEdicao.getNumeroEdicao());
		sbAssert.append("\n Colecao : ");
		sbAssert.append(produtoEdicao.isColecao());
		sbAssert.append("\n");

		if ((produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1 && produtoEdicao.isColecao()) || qtdeEdicaoRecebida <= 1) {
		    assertNull(indiceCorrecao, sbAssert.toString());
		    assertNull(vendaCorrigida, sbAssert.toString());
		} else {
		    assertNotNull(indiceCorrecao, sbAssert.toString());
		    assertNotNull(vendaCorrigida, sbAssert.toString());
		}

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    int qtdeEdicaoFechada = listEdicaoFechada.size();

	    if (qtdeEdicaoRecebida > 1)
		assertNotNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());
	    else
		assertNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());

	    if (qtdeEdicaoFechada >= 4 && qtdeEdicaoRecebida > 1)
		assertNotNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());
	    else
		assertNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());

	    gerarReporterLog(cota, qtdeEdicaoFechada, sbReporterLog);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices da(s) cota(s) que as edições encontra(m)-se na primeira edição.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEdicaoPrimeiraEdicaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void cotaComEdicaoPrimeiraEdicaoTest(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edicao base ");

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    int qtdeEdicaoRecebida = cota.getEdicoesRecebidas().size();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		if (!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();
		BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

		StringBuffer sbAssert = new StringBuffer();
		sbAssert.append("\n Indice Correcao : ");
		sbAssert.append(indiceCorrecao);
		sbAssert.append("\n Venda Corrigida :");
		sbAssert.append(vendaCorrigida);
		sbAssert.append("\n Cota : ");
		sbAssert.append(cota.getId());
		sbAssert.append("\n Produto Edicao : ");
		sbAssert.append(produtoEdicao.getId());
		sbAssert.append("\n Produto : ");
		sbAssert.append(produtoEdicao.getIdProduto());
		sbAssert.append("\n Numero Edicao : ");
		sbAssert.append(produtoEdicao.getNumeroEdicao());
		sbAssert.append("\n Colecao : ");
		sbAssert.append(produtoEdicao.isColecao());
		sbAssert.append("\n");

		if ((produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1 && produtoEdicao.isColecao()) || qtdeEdicaoRecebida <= 1) {
		    assertNull(indiceCorrecao, sbAssert.toString());
		    assertNull(vendaCorrigida, sbAssert.toString());
		} else {
		    assertNotNull(indiceCorrecao, sbAssert.toString());
		    assertNotNull(vendaCorrigida, sbAssert.toString());
		}

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    int qtdeEdicaoFechada = listEdicaoFechada.size();

	    if (qtdeEdicaoRecebida > 1)
		assertNotNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());
	    else
		assertNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());

	    if (qtdeEdicaoFechada >= 4 && qtdeEdicaoRecebida > 1)
		assertNotNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());
	    else
		assertNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());

	    gerarReporterLog(cota, qtdeEdicaoFechada, sbReporterLog);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices da(s) cota(s) que as edições não encontra(m)-se como Fascículo/Coleção.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEdicaoNaoFasciculoColecaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void cotaComEdicaoNaoFasciculoColecaoTest(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edicao base ");

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    int qtdeEdicaoRecebida = cota.getEdicoesRecebidas().size();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		if (!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();
		BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

		StringBuffer sbAssert = new StringBuffer();
		sbAssert.append("\n Indice Correcao : ");
		sbAssert.append(indiceCorrecao);
		sbAssert.append("\n Venda Corrigida :");
		sbAssert.append(vendaCorrigida);
		sbAssert.append("\n Cota : ");
		sbAssert.append(cota.getId());
		sbAssert.append("\n Produto Edicao : ");
		sbAssert.append(produtoEdicao.getId());
		sbAssert.append("\n Produto : ");
		sbAssert.append(produtoEdicao.getIdProduto());
		sbAssert.append("\n Numero Edicao : ");
		sbAssert.append(produtoEdicao.getNumeroEdicao());
		sbAssert.append("\n Colecao : ");
		sbAssert.append(produtoEdicao.isColecao());
		sbAssert.append("\n");

		if ((produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1 && produtoEdicao.isColecao()) || qtdeEdicaoRecebida <= 1) {
		    assertNull(indiceCorrecao, sbAssert.toString());
		    assertNull(vendaCorrigida, sbAssert.toString());
		} else {
		    assertNotNull(indiceCorrecao, sbAssert.toString());
		    assertNotNull(vendaCorrigida, sbAssert.toString());
		}

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    int qtdeEdicaoFechada = listEdicaoFechada.size();

	    if (qtdeEdicaoRecebida > 1)
		assertNotNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());
	    else
		assertNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());

	    if (qtdeEdicaoFechada >= 4 && qtdeEdicaoRecebida > 1)
		assertNotNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());
	    else
		assertNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());

	    gerarReporterLog(cota, qtdeEdicaoFechada, sbReporterLog);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices da(s) cota(s) que contém edições com status diferente de 'Fechada'.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEdicaoNaoFechadaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void cotaComEdicaoNaoFechadaTest(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edicao base ");

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    int qtdeEdicaoRecebida = cota.getEdicoesRecebidas().size();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		if (!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();
		BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

		StringBuffer sbAssert = new StringBuffer();
		sbAssert.append("\n Indice Correcao : ");
		sbAssert.append(indiceCorrecao);
		sbAssert.append("\n Venda Corrigida :");
		sbAssert.append(vendaCorrigida);
		sbAssert.append("\n Cota : ");
		sbAssert.append(cota.getId());
		sbAssert.append("\n Produto Edicao : ");
		sbAssert.append(produtoEdicao.getId());
		sbAssert.append("\n Produto : ");
		sbAssert.append(produtoEdicao.getIdProduto());
		sbAssert.append("\n Numero Edicao : ");
		sbAssert.append(produtoEdicao.getNumeroEdicao());
		sbAssert.append("\n Colecao : ");
		sbAssert.append(produtoEdicao.isColecao());
		sbAssert.append("\n");

		if ((produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1 && produtoEdicao.isColecao()) || qtdeEdicaoRecebida <= 1) {
		    assertNull(indiceCorrecao, sbAssert.toString());
		    assertNull(vendaCorrigida, sbAssert.toString());
		} else {
		    assertNotNull(indiceCorrecao, sbAssert.toString());
		    assertNotNull(vendaCorrigida, sbAssert.toString());
		}

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    int qtdeEdicaoFechada = listEdicaoFechada.size();

	    if (qtdeEdicaoRecebida > 1)
		assertNotNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());
	    else
		assertNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());

	    if (qtdeEdicaoFechada >= 4 && qtdeEdicaoRecebida > 1)
		assertNotNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());
	    else
		assertNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());

	    gerarReporterLog(cota, qtdeEdicaoFechada, sbReporterLog);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar os índices da(s) cota(s) que contém edições com status de 'Fechada'.
     * 
     * @param cota
     */
    @Test(dataProvider = "getCotaComEdicaoFechadaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void cotaComEdicaoFechadaTest(Cota cota) {

	try {

	    StringBuilder sbReporterLog = new StringBuilder();

	    assertNotNull(cota.getEdicoesRecebidas(), " Cota : " + cota.getId() + " nao contem edicao base ");

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    int qtdeEdicaoRecebida = cota.getEdicoesRecebidas().size();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		if (!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}

		BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();
		BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

		StringBuffer sbAssert = new StringBuffer();
		sbAssert.append("\n Indice Correcao : ");
		sbAssert.append(indiceCorrecao);
		sbAssert.append("\n Venda Corrigida :");
		sbAssert.append(vendaCorrigida);
		sbAssert.append("\n Cota : ");
		sbAssert.append(cota.getId());
		sbAssert.append("\n Produto Edicao : ");
		sbAssert.append(produtoEdicao.getId());
		sbAssert.append("\n Produto : ");
		sbAssert.append(produtoEdicao.getIdProduto());
		sbAssert.append("\n Numero Edicao : ");
		sbAssert.append(produtoEdicao.getNumeroEdicao());
		sbAssert.append("\n Colecao : ");
		sbAssert.append(produtoEdicao.isColecao());
		sbAssert.append("\n");

		if ((produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1 && produtoEdicao.isColecao()) || qtdeEdicaoRecebida <= 1) {
		    assertNull(indiceCorrecao, sbAssert.toString());
		    assertNull(vendaCorrigida, sbAssert.toString());
		} else {
		    assertNotNull(indiceCorrecao, sbAssert.toString());
		    assertNotNull(vendaCorrigida, sbAssert.toString());
		}

		gerarProdutoEdicaoLog(sbReporterLog, produtoEdicao);
	    }

	    BigDecimal indiceCorrecaoTendencia = cota.getIndiceCorrecaoTendencia();
	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    int qtdeEdicaoFechada = listEdicaoFechada.size();

	    if (qtdeEdicaoRecebida > 1)
		assertNotNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());
	    else
		assertNull(indiceCorrecaoTendencia, "Indice Correcao Tendencia : " + indiceCorrecaoTendencia + " Cota : " + cota.getId());

	    if (qtdeEdicaoFechada >= 4 && qtdeEdicaoRecebida > 1)
		assertNotNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());
	    else
		assertNull(indiceVendaCrescente, "Indice Venda Crescente : " + indiceVendaCrescente + " Cota : " + cota.getId());

	    gerarReporterLog(cota, qtdeEdicaoFechada, sbReporterLog);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    private void gerarReporterLog(Cota cota, int qtdeEdicaoFechada, StringBuilder sbReporterLog) {
	Reporter.log("<p>Cota </p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>Quantidade Edição Produto : " + cota.getEdicoesRecebidas().size() + "</p>");
	Reporter.log("<p style='margin-left: 50px'>Quantidade Edição Produto (Fechada) : " + qtdeEdicaoFechada + "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : " + (cota.getIndiceVendaCrescente() != null ? cota.getIndiceVendaCrescente() : "NULO")
		+ "</p>");
	Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao Tendencia : "
		+ (cota.getIndiceCorrecaoTendencia() != null ? cota.getIndiceCorrecaoTendencia() : "NULO") + "</p>");
	Reporter.log(sbReporterLog.toString());
    }

    private void gerarProdutoEdicaoLog(StringBuilder sbReporterLog, ProdutoEdicao produtoEdicao) {
	sbReporterLog.append("<p style='margin-left: 100px'>Produto Edicao : " + produtoEdicao.getId() + "</p>");
	sbReporterLog.append("<p style='margin-left: 100px'>Produto : " + produtoEdicao.getIdProduto() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Aberta : " + produtoEdicao.isEdicaoAberta() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Reparte : " + produtoEdicao.getReparte() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Venda : " + produtoEdicao.getVenda() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Fascículos / Coleções : " + produtoEdicao.isColecao() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>Número Edição : " + produtoEdicao.getNumeroEdicao() + "</p>");
	sbReporterLog.append("<p style='margin-left: 150px'>-> Indice de Correcao : "
		+ (produtoEdicao.getIndiceCorrecao() != null ? produtoEdicao.getIndiceCorrecao() : "NULO") + "</p>");
    }

}
