package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;

public class CorrecaoVendasTest {

    @Test(dataProvider = "getCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(List<Cota> listCota) {

	try {

	    int iCota = 0;
	    while (iCota < listCota.size()) {

		Cota cota = listCota.get(iCota);

		CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
		correcaoVendas.executar();

		Cota cotaReturn = (Cota) correcaoVendas.getGenericDTO();

		BigDecimal one = BigDecimal.ONE;
		BigDecimal oneDotOne = one.add(new BigDecimal(0.1));
		BigDecimal oneDotTwo = one.add(new BigDecimal(0.2));

		oneDotOne = oneDotOne.divide(one, 1, BigDecimal.ROUND_FLOOR);
		oneDotTwo = oneDotTwo.divide(one, 1, BigDecimal.ROUND_FLOOR);

		boolean assertIndiceCorrecaoTendencia = (cotaReturn
			.getIndiceCorrecaoTendencia() != null && (cotaReturn
			.getIndiceCorrecaoTendencia().compareTo(one) == 0
			|| cotaReturn.getIndiceCorrecaoTendencia().compareTo(
				oneDotOne) == 0 || cotaReturn
			.getIndiceCorrecaoTendencia().compareTo(oneDotTwo) == 0));

		assertTrue(assertIndiceCorrecaoTendencia);

		Iterator<EstoqueProdutoCota> itEstoqueProdutoCota = cota
			.getEstoqueProdutoCotas().iterator();
		while (itEstoqueProdutoCota.hasNext()) {

		    EstoqueProdutoCota estoqueProdutoCota = itEstoqueProdutoCota
			    .next();

		    boolean assertIndiceCorrecao = (estoqueProdutoCota
			    .getIndiceCorrecao() != null && (estoqueProdutoCota
			    .getIndiceCorrecao().compareTo(one) == 0
			    || estoqueProdutoCota.getIndiceCorrecao()
				    .compareTo(oneDotOne) == 0 || estoqueProdutoCota
			    .getIndiceCorrecao().compareTo(oneDotTwo) == 0));

		    assertTrue(assertIndiceCorrecao);

		}

		boolean assertIndiceVendaCrescente = (cotaReturn
			.getIndiceVendaCrescente() != null && (cotaReturn
			.getIndiceVendaCrescente().compareTo(one) == 0 || cotaReturn
			.getIndiceVendaCrescente().compareTo(oneDotOne) == 0));

		assertTrue(assertIndiceVendaCrescente);

		iCota++;
	    }

	} catch (Exception e) {
	    fail(e.getMessage());
	}

    }
}
