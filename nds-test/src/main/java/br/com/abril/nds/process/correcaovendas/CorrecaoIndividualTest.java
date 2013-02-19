package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;

public class CorrecaoIndividualTest {

    @Test(dataProvider = "getEstoqueProdutoCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void primeiroCenario(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota);

	    correcaoIndividual.executar();

	    estoqueProdutoCota = (EstoqueProdutoCota) correcaoIndividual
		    .getGenericDTO();

	    ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotOne = one.add(new BigDecimal(0.1));
	    BigDecimal oneDotTwo = one.add(new BigDecimal(0.2));

	    oneDotOne = oneDotOne.divide(one, 1, BigDecimal.ROUND_FLOOR);

	    oneDotTwo = oneDotTwo.divide(one, 1, BigDecimal.ROUND_FLOOR);

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

    @Test(dataProvider = "getEstoqueProdutoCotaParaPercentualVendaUmList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void segundoCenario(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota);

	    correcaoIndividual.executar();

	    estoqueProdutoCota = (EstoqueProdutoCota) correcaoIndividual
		    .getGenericDTO();

	    ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

	    BigDecimal indiceCorrecao = produtoEdicao.getIndiceCorrecao();

	    assertNotNull(indiceCorrecao);

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotTwo = one.add(new BigDecimal(0.2));
	    oneDotTwo = oneDotTwo.divide(one, 1, BigDecimal.ROUND_FLOOR);

	    boolean assertIndice = indiceCorrecao.compareTo(oneDotTwo) == 0;

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
}
