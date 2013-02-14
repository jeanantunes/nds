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
    public void executarProcesso(EstoqueProdutoCota estoqueProdutoCota) {

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

	    assertTrue(assertIndice);

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

	    Reporter.log("Quantidade Recebida : " + quantidadeRecebida);
	    Reporter.log("Quantidade Devolvida : " + quantidadeDevolvida);
	    Reporter.log("Venda : " + vendaEdicao);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}
