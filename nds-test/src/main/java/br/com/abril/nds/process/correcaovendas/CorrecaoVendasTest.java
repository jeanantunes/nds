package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;

public class CorrecaoVendasTest {

    @Test(dataProvider = "getCotaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(Cota cota) {

	try {

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    cota = (Cota) correcaoVendas.getGenericDTO();

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotOne = one.add(new BigDecimal(0.1));
	    BigDecimal oneDotTwo = one.add(new BigDecimal(0.2));

	    oneDotOne = oneDotOne.divide(one, 1, BigDecimal.ROUND_FLOOR);
	    oneDotTwo = oneDotTwo.divide(one, 1, BigDecimal.ROUND_FLOOR);

	    if (cota.getEdicoesBase() != null
		    && cota.getEdicoesBase().size() > 1) {

		BigDecimal indiceCorrecaoTendencia = cota
			.getIndiceCorrecaoTendencia();

		Iterator<EstoqueProdutoCota> itEstoqueProdutoCota = cota
			.getEstoqueProdutoCotas().iterator();

		while (itEstoqueProdutoCota.hasNext()) {

		    EstoqueProdutoCota estoqueProdutoCota = itEstoqueProdutoCota
			    .next();

		    if (estoqueProdutoCota.getProdutoEdicao().getNumeroEdicao()
			    .compareTo(new Long(1)) == 0
			    || !estoqueProdutoCota.getProdutoEdicao()
				    .isColecao()) {

			ProdutoEdicao produtoEdicao = estoqueProdutoCota
				.getProdutoEdicao();

			BigDecimal indiceCorrecao = produtoEdicao
				.getIndiceCorrecao();

			boolean assertIndiceCorrecao = (indiceCorrecao != null && (indiceCorrecao
				.compareTo(one) == 0
				|| indiceCorrecao.compareTo(oneDotOne) == 0 || indiceCorrecao
				.compareTo(oneDotTwo) == 0));

			assertTrue("Indice Correcao : " + indiceCorrecao
				+ " Cota : " + cota.getId()
				+ " Estoque Produto Cota : "
				+ estoqueProdutoCota.getId(),
				assertIndiceCorrecao);

			BigInteger quantidadeRecebida = estoqueProdutoCota
				.getQuantidadeRecebida().toBigInteger();
			BigInteger quantidadeDevolvida = estoqueProdutoCota
				.getQuantidadeDevolvida().toBigInteger();

			BigInteger venda = quantidadeRecebida
				.subtract(quantidadeDevolvida);

			sbEstoqueLog
				.append("<p style='margin-left: 100px'>Estoque Produto Cota</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 150px'>ID : "
					+ estoqueProdutoCota.getId() + "</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 100px'>Produto Edicao</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 150px'>ID : "
					+ produtoEdicao.getId() + "</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 150px'>Nome : "
					+ produtoEdicao.getNome() + "</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 150px'>Quantidade Recebida : "
					+ quantidadeRecebida + "</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 150px'>Quantidade Devolvida : "
					+ quantidadeDevolvida + "</p>");
			sbEstoqueLog
				.append("<p style='margin-left: 150px'>Venda : "
					+ venda + "</p>");
		    }

		}

		boolean assertIndiceCorrecaoTendencia = (indiceCorrecaoTendencia != null && (indiceCorrecaoTendencia
			.compareTo(one) == 0
			|| indiceCorrecaoTendencia.compareTo(oneDotOne) == 0 || indiceCorrecaoTendencia
			.compareTo(oneDotTwo) == 0));

		assertTrue("Indice Correcao Tendencia : "
			+ indiceCorrecaoTendencia + " Cota : " + cota.getId(),
			assertIndiceCorrecaoTendencia);

		sbEstoqueLog
			.append("<p style='margin-left: 50px'>-> Indice Correcao Tendencia : "
				+ indiceCorrecaoTendencia + "</p>");
	    }

	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    boolean assertIndiceVendaCrescente = (indiceVendaCrescente != null && (indiceVendaCrescente
		    .compareTo(one) == 0 || indiceVendaCrescente
		    .compareTo(oneDotOne) == 0));

	    assertTrue("Indice Venda Crescente : " + indiceVendaCrescente
		    + " Cota : " + cota.getId(), assertIndiceVendaCrescente);

	    Reporter.log("<p>Cota " + cota.getNomePessoa() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId()
		    + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>Numero : "
		    + cota.getNumero() + "</p>");

	    Reporter.log(sbEstoqueLog.toString());

	    Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : "
		    + indiceVendaCrescente + "</p>");

	} catch (Exception e) {
	    fail(e.getMessage());
	}

    }
}
