package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class CorrecaoTendenciaTest {

    @Test(dataProvider = "getCotaProdutoEdicaoPrimeiraEdicaoColecaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void primeiroCenario(Cota cota, BigDecimal totalReparte,
	    BigDecimal totalVenda) {

	try {

	    CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota,
		    totalReparte, totalVenda);

	    correcaoTendencia.executar();

	    cota = (Cota) correcaoTendencia.getGenericDTO();

	    assertNotNull(cota);

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < cota.getEdicoesRecebidas().size()) {

		ProdutoEdicao produtoEdicao = cota.getEdicoesRecebidas().get(
			iEdicaoBase);

		sbEstoqueLog
			.append("<p style='margin-left: 100px'>Produto Edicao </p>");
		sbEstoqueLog.append("<p style='margin-left: 150px'>ID : "
			+ produtoEdicao.getId() + "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 150px'>Quantidade Recebida : "
				+ produtoEdicao.getReparte() + "</p>");
		sbEstoqueLog.append("<p style='margin-left: 150px'>Venda : "
			+ produtoEdicao.getVenda() + "</p>");

		iEdicaoBase++;
	    }

	    BigDecimal indiceCorrecaoTendencia = cota
		    .getIndiceCorrecaoTendencia();

	    assertNotNull(indiceCorrecaoTendencia);

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotOne = one.add(new BigDecimal(0.1));
	    BigDecimal oneDotTwo = one.add(new BigDecimal(0.2));

	    oneDotOne = oneDotOne.divide(one, 1, BigDecimal.ROUND_FLOOR);

	    oneDotTwo = oneDotTwo.divide(one, 1, BigDecimal.ROUND_FLOOR);

	    boolean assertIndiceCorrecaoTendencia = indiceCorrecaoTendencia
		    .compareTo(one) == 0
		    || indiceCorrecaoTendencia.compareTo(oneDotOne) == 0
		    || indiceCorrecaoTendencia.compareTo(oneDotTwo) == 0;

	    assertTrue("Indice Correcao Tendencia : " + indiceCorrecaoTendencia
		    + " Cota : " + cota.getId(), assertIndiceCorrecaoTendencia);

	    Reporter.log("<p>Cota </p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId()
		    + "</p>");
	    // Reporter.log("<p style='margin-left: 50px'>Numero : "
	    // + cota.getNumero() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao Tendencia : "
		    + indiceCorrecaoTendencia + "</p>");
	    Reporter.log(sbEstoqueLog.toString());

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}
