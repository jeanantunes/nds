package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import br.com.abril.nds.model.EstoqueProdutoCota;

public class CorrecaoIndividualTest {

    @Test(dataProvider = "getEstoqueProdutoCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(EstoqueProdutoCota estoqueProdutoCota) {

	try {

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota);

	    correcaoIndividual.executar();

	    EstoqueProdutoCota estoqueProdutoCotaReturn = (EstoqueProdutoCota) correcaoIndividual
		    .getGenericDTO();

	    BigDecimal indiceCorrecao = estoqueProdutoCotaReturn
		    .getIndiceCorrecao();
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

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}
