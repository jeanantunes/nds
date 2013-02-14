package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;

public class CorrecaoTendenciaTest {

    @Test(dataProvider = "getCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(Cota cota) {

	try {

	    cota.setEstoqueProdutoCotas(new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesBase()));

	    CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota);

	    correcaoTendencia.executar();

	    cota = (Cota) correcaoTendencia.getGenericDTO();

	    assertNotNull(cota);

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < cota.getEstoqueProdutoCotas().size()) {

		EstoqueProdutoCota estoqueProdutoCota = cota
			.getEstoqueProdutoCotas().get(iEdicaoBase);

		BigInteger quantidadeRecebida = estoqueProdutoCota
			.getQuantidadeRecebida().toBigInteger();
		BigInteger quantidadeDevolvida = estoqueProdutoCota
			.getQuantidadeDevolvida().toBigInteger();

		BigInteger venda = quantidadeRecebida
			.subtract(quantidadeDevolvida);

		sbEstoqueLog
			.append("<p style='margin-left: 100px'>Estoque Produto Cota</p>");
		sbEstoqueLog.append("<p style='margin-left: 150px'>ID : "
			+ estoqueProdutoCota.getId() + "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 150px'>Quantidade Recebida : "
				+ quantidadeRecebida + "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 150px'>Quantidade Devolvida : "
				+ quantidadeDevolvida + "</p>");
		sbEstoqueLog.append("<p style='margin-left: 150px'>Venda : "
			+ venda + "</p>");

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

	    boolean assertIndice = indiceCorrecaoTendencia.compareTo(one) == 0
		    || indiceCorrecaoTendencia.compareTo(oneDotOne) == 0
		    || indiceCorrecaoTendencia.compareTo(oneDotTwo) == 0;

	    assertTrue(assertIndice);

	    Reporter.log("<p>Cota " + cota.getNomePessoa() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId()
		    + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>Numero : "
		    + cota.getNumero() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Correcao Tendencia : "
		    + indiceCorrecaoTendencia + "</p>");
	    Reporter.log(sbEstoqueLog.toString());

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}
