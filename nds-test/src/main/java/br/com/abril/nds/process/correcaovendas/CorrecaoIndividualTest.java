package br.com.abril.nds.process.correcaovendas;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.math.BigDecimal;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.dataprovider.CorrecaoIndividualDataProvider;

public class CorrecaoIndividualTest {

    /**
     * Testar se o índice de correção será 1.
     * 
     * @param estoqueProdutoCota
     */
    @Test(dataProvider = "getEstoqueProdutoCotaParaPercentualVendaNaoIgualUmNaoIgualMaiorZeroVirgualNoveList", dataProviderClass = CorrecaoIndividualDataProvider.class)
    public void percentualVendaNaoIgualUmNaoIgualMaiorZeroVirgualNove(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(estoqueProdutoCota.getProdutoEdicao());
	    correcaoIndividual.executar();

	    ProdutoEdicao produtoEdicao = (ProdutoEdicao) correcaoIndividual.getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    boolean assertIndice = (indiceCorrecao.compareTo(BigDecimal.ONE) == 0);

	    assertTrue(assertIndice,
		    " Indice Correcao : " + indiceCorrecao + " Estoque Produto Cota : " + estoqueProdutoCota.getId() + " Produto Edicao : " + produtoEdicao.getId()
			    + " Produto : " + produtoEdicao.getIdProduto() + " Reparte : " + produtoEdicao.getReparte() + " Venda : " + produtoEdicao.getVenda());

	    assertNotNull(produtoEdicao.getVendaCorrigida());

	    this.gerarReporterLog(estoqueProdutoCota, produtoEdicao, indiceCorrecao);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar se o índice de correção será 1.2.
     * 
     * @param estoqueProdutoCota
     */
    @Test(dataProvider = "getEstoqueProdutoCotaParaPercentualVendaIgualUmList", dataProviderClass = CorrecaoIndividualDataProvider.class)
    public void percentualVendaIgualUm(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(estoqueProdutoCota.getProdutoEdicao());
	    correcaoIndividual.executar();

	    ProdutoEdicao produtoEdicao = (ProdutoEdicao) correcaoIndividual.getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    BigDecimal oneDotTwo = BigDecimal.ONE.add(new BigDecimal(0.2)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

	    boolean assertIndice = (indiceCorrecao.compareTo(oneDotTwo) == 0);

	    assertTrue(assertIndice,
		    " Indice Correcao : " + indiceCorrecao + " Estoque Produto Cota : " + estoqueProdutoCota.getId() + " Produto Edicao : " + produtoEdicao.getId()
			    + " Produto : " + produtoEdicao.getIdProduto() + " Reparte : " + produtoEdicao.getReparte() + " Venda : " + produtoEdicao.getVenda());

	    assertNotNull(produtoEdicao.getVendaCorrigida());

	    this.gerarReporterLog(estoqueProdutoCota, produtoEdicao, indiceCorrecao);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Testar se o índice de correção será 1.1
     * 
     * @param estoqueProdutoCota
     */
    @Test(dataProvider = "getEstoqueProdutoCotaParaPercentualVendaMaiorIgualZeroVirgulaNoveList", dataProviderClass = CorrecaoIndividualDataProvider.class)
    public void percentualVendaMaiorIgualZeroVirgulaNove(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(estoqueProdutoCota.getProdutoEdicao());
	    correcaoIndividual.executar();

	    ProdutoEdicao produtoEdicao = (ProdutoEdicao) correcaoIndividual.getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    BigDecimal oneDotOne = BigDecimal.ONE.add(new BigDecimal(0.1)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

	    boolean assertIndice = (indiceCorrecao.compareTo(oneDotOne) == 0);

	    assertTrue(assertIndice,
		    " Indice Correcao : " + indiceCorrecao + " Estoque Produto Cota : " + estoqueProdutoCota.getId() + " Produto Edicao : " + produtoEdicao.getId()
			    + " Produto : " + produtoEdicao.getIdProduto() + " Reparte : " + produtoEdicao.getReparte() + " Venda : " + produtoEdicao.getVenda());

	    assertNotNull(produtoEdicao.getVendaCorrigida());

	    this.gerarReporterLog(estoqueProdutoCota, produtoEdicao, indiceCorrecao);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Gerar Reporter Log para o resultado.
     * 
     * @param estoqueProdutoCota
     * @param produtoEdicao
     * @param indiceCorrecao
     */
    private void gerarReporterLog(EstoqueProdutoCota estoqueProdutoCota, ProdutoEdicao produtoEdicao, BigDecimal indiceCorrecao) {

	Reporter.log("<p>Estoque Produto Cota</p>");
	Reporter.log("<p style='margin-left: 50px'>ID : " + estoqueProdutoCota.getId() + "</p>");
	Reporter.log("<p style='margin-left: 100px'>Produto Edicao : " + produtoEdicao.getId() + "</p>");
	Reporter.log("<p style='margin-left: 100px'>Produto : " + produtoEdicao.getIdProduto() + "</p>");

	BigDecimal quantidadeRecebida = estoqueProdutoCota.getQuantidadeRecebida();
	BigDecimal quantidadeDevolvida = estoqueProdutoCota.getQuantidadeDevolvida();
	BigDecimal vendaEdicao = produtoEdicao.getVenda();

	Reporter.log("<p style='margin-left: 100px'>Quantidade Recebida : " + quantidadeRecebida + "</p>");
	Reporter.log("<p style='margin-left: 100px'>Quantidade Devolvida : " + quantidadeDevolvida + "</p>");
	Reporter.log("<p style='margin-left: 100px'>Venda : " + vendaEdicao + "</p>");

	Reporter.log("<p style='margin-left: 100px'>-> Indice Correcao : " + indiceCorrecao + "</p>");
	Reporter.log("<p style='margin-left: 100px'>-> Venda Corrigida : " + produtoEdicao.getVendaCorrigida() + "</p>");
    }

}
