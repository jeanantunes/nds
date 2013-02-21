package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;

public class CorrecaoIndividualTest {

    @Test(dataProvider = "getEstoqueProdutoCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void primeiroCenario(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    produtoEdicao);

	    correcaoIndividual.executar();

	    produtoEdicao = (ProdutoEdicao) correcaoIndividual.getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    MathContext mathContext = new MathContext(1, RoundingMode.FLOOR);

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotOne = one.add(new BigDecimal(0.1, mathContext));
	    BigDecimal oneDotTwo = one.add(new BigDecimal(0.2, mathContext));

	    boolean assertIndice = indiceCorrecao.compareTo(one) == 0
		    || indiceCorrecao.compareTo(oneDotOne) == 0
		    || indiceCorrecao.compareTo(oneDotTwo) == 0;

	    assertTrue("Indice Correcao : " + indiceCorrecao
		    + "Estoque Produto Cota : " + estoqueProdutoCota.getId(),
		    assertIndice);

	    Reporter.log("<p>Estoque Produto Cota</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : "
		    + estoqueProdutoCota.getId() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao : "
		    + indiceCorrecao + "</p>");

	    BigDecimal quantidadeRecebida = estoqueProdutoCota
		    .getQuantidadeRecebida();
	    BigDecimal quantidadeDevolvida = estoqueProdutoCota
		    .getQuantidadeDevolvida();
	    BigDecimal vendaEdicao = quantidadeRecebida
		    .subtract(quantidadeDevolvida);

	    Reporter.log("<p>Quantidade Recebida : " + quantidadeRecebida
		    + "</p>");
	    Reporter.log("<p>Quantidade Devolvida : " + quantidadeDevolvida
		    + "</p>");
	    Reporter.log("<p>Venda : " + vendaEdicao + "</p>");

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test(dataProvider = "getEstoqueProdutoCotaParaIndiceCorrecaoUmPontoDoisList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void indiceCorrecaoUmPontoDois(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota.getProdutoEdicao());

	    correcaoIndividual.executar();

	    ProdutoEdicao produtoEdicao = (ProdutoEdicao) correcaoIndividual
		    .getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    BigDecimal oneDotTwo = BigDecimal.ONE.add(new BigDecimal(0.2));
	    oneDotTwo = oneDotTwo.divide(BigDecimal.ONE, 1,
		    BigDecimal.ROUND_FLOOR);

	    boolean assertIndice = (indiceCorrecao.compareTo(oneDotTwo) == 0);

	    assertTrue(" Indice Correcao : " + indiceCorrecao
		    + " Estoque Produto Cota : " + estoqueProdutoCota.getId()
		    + " Produto Edicao : " + produtoEdicao.getId()
		    + " Produto : " + produtoEdicao.getIdProduto()
		    + " Reparte : " + produtoEdicao.getReparte()
		    + " Venda : " + produtoEdicao.getVenda(), assertIndice);

	    Reporter.log("<p>Estoque Produto Cota</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : "
		    + estoqueProdutoCota.getId() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao : "
		    + indiceCorrecao + "</p>");

	    BigDecimal quantidadeRecebida = estoqueProdutoCota
		    .getQuantidadeRecebida();
	    BigDecimal quantidadeDevolvida = estoqueProdutoCota
		    .getQuantidadeDevolvida();
	    BigDecimal vendaEdicao = produtoEdicao.getVenda();

	    Reporter.log("<p>Quantidade Recebida : " + quantidadeRecebida
		    + "</p>");
	    Reporter.log("<p>Quantidade Devolvida : " + quantidadeDevolvida
		    + "</p>");
	    Reporter.log("<p>Venda : " + vendaEdicao + "</p>");

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test(dataProvider = "getEstoqueProdutoCotaParaIndiceCorrecaoUmPontoUmList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void indiceCorrecaoUmPontoUm(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota.getProdutoEdicao());

	    correcaoIndividual.executar();

	    ProdutoEdicao produtoEdicao = (ProdutoEdicao) correcaoIndividual
		    .getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    BigDecimal oneDotOne = BigDecimal.ONE.add(new BigDecimal(0.1));
	    oneDotOne = oneDotOne.divide(BigDecimal.ONE, 1,
		    BigDecimal.ROUND_FLOOR);
	    
	    boolean assertIndice = (indiceCorrecao.compareTo(oneDotOne) == 0);

	    assertTrue(" Indice Correcao : " + indiceCorrecao
		    + " Estoque Produto Cota : " + estoqueProdutoCota.getId()
		    + " Produto Edicao : " + produtoEdicao.getId()
		    + " Produto : " + produtoEdicao.getIdProduto()
		    + " Reparte : " + produtoEdicao.getReparte()
		    + " Venda : " + produtoEdicao.getVenda(), assertIndice);

	    Reporter.log("<p>Estoque Produto Cota</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : "
		    + estoqueProdutoCota.getId() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao : "
		    + indiceCorrecao + "</p>");

	    BigDecimal quantidadeRecebida = estoqueProdutoCota
		    .getQuantidadeRecebida();
	    BigDecimal quantidadeDevolvida = estoqueProdutoCota
		    .getQuantidadeDevolvida();
	    BigDecimal vendaEdicao = produtoEdicao.getVenda();

	    Reporter.log("<p>Quantidade Recebida : " + quantidadeRecebida
		    + "</p>");
	    Reporter.log("<p>Quantidade Devolvida : " + quantidadeDevolvida
		    + "</p>");
	    Reporter.log("<p>Venda : " + vendaEdicao + "</p>");

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
    
    @Test(dataProvider = "getEstoqueProdutoCotaParaIndiceCorrecaoUmList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void indiceCorrecaoUm(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota.getProdutoEdicao());

	    correcaoIndividual.executar();

	    ProdutoEdicao produtoEdicao = (ProdutoEdicao) correcaoIndividual
		    .getGenericDTO();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    boolean assertIndice = (indiceCorrecao.compareTo(BigDecimal.ONE) == 0);

	    assertTrue(" Indice Correcao : " + indiceCorrecao
		    + " Estoque Produto Cota : " + estoqueProdutoCota.getId()
		    + " Produto Edicao : " + produtoEdicao.getId()
		    + " Produto : " + produtoEdicao.getIdProduto()
		    + " Reparte : " + produtoEdicao.getReparte()
		    + " Venda : " + produtoEdicao.getVenda(), assertIndice);

	    Reporter.log("<p>Estoque Produto Cota</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : "
		    + estoqueProdutoCota.getId() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao : "
		    + indiceCorrecao + "</p>");

	    BigDecimal quantidadeRecebida = estoqueProdutoCota
		    .getQuantidadeRecebida();
	    BigDecimal quantidadeDevolvida = estoqueProdutoCota
		    .getQuantidadeDevolvida();
	    BigDecimal vendaEdicao = produtoEdicao.getVenda();

	    Reporter.log("<p>Quantidade Recebida : " + quantidadeRecebida
		    + "</p>");
	    Reporter.log("<p>Quantidade Devolvida : " + quantidadeDevolvida
		    + "</p>");
	    Reporter.log("<p>Venda : " + vendaEdicao + "</p>");

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}

