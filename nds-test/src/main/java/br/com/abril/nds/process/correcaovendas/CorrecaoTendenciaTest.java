package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;

public class CorrecaoTendenciaTest {

    @Test(dataProvider = "getCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(List<Cota> cotas) {
	try {

	    Iterator<Cota> itCota = cotas.iterator();

	    while (itCota.hasNext()) {

		Cota cota = itCota.next();

		CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(
			cota);

		correcaoTendencia.executar();

		Cota cotaReturn = (Cota) correcaoTendencia.getGenericDTO();

		assertNotNull(cotaReturn);

		BigDecimal indiceCorrecaoTendencia = cotaReturn
			.getIndiceCorrecaoTendencia();
		assertNotNull(indiceCorrecaoTendencia);

		BigDecimal one = BigDecimal.ONE;
		BigDecimal oneDotOne = one.add(new BigDecimal(0.1));
		BigDecimal oneDotTwo = one.add(new BigDecimal(0.2));

		oneDotOne = oneDotOne.divide(one, 1, BigDecimal.ROUND_FLOOR);

		oneDotTwo = oneDotTwo.divide(one, 1, BigDecimal.ROUND_FLOOR);

		boolean assertIndice = indiceCorrecaoTendencia.compareTo(one) == 0
			|| indiceCorrecaoTendencia.compareTo(oneDotOne) == 0
			|| indiceCorrecaoTendencia.compareTo(oneDotTwo) == 0;

		assertTrue(assertIndice);

	    }

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}
