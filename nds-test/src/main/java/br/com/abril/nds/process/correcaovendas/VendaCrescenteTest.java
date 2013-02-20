package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.junit.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;

public class VendaCrescenteTest {

    @Test(dataProvider = "getCotaEdicaoBaseUnicaPublicacaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(Cota cota) {

	try {

	    VendaCrescente vendaCrescente = new VendaCrescente(cota);
	    vendaCrescente.executar();
	    cota = (Cota) vendaCrescente.getGenericDTO();

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    Iterator<EstoqueProdutoCota> itEstoqueProdutoCota = cota
		    .getEstoqueProdutoCotas().iterator();

	    while (itEstoqueProdutoCota.hasNext()) {

		EstoqueProdutoCota estoqueProdutoCota = itEstoqueProdutoCota
			.next();

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
			.append("<p style='margin-left: 150px'>Produto Edicao</p>");
		sbEstoqueLog.append("<p style='margin-left: 200px'>ID : "
			+ estoqueProdutoCota.getProdutoEdicao().getId()
			+ "</p>");
		sbEstoqueLog.append("<p style='margin-left: 200px'>Nome : "
			+ estoqueProdutoCota.getProdutoEdicao().getNome()
			+ "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 200px'>Quantidade Recebida : "
				+ quantidadeRecebida + "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 200px'>Quantidade Devolvida : "
				+ quantidadeDevolvida + "</p>");
		sbEstoqueLog.append("<p style='margin-left: 200px'>Venda : "
			+ venda + "</p>");
	    }

	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotOne = one.add(new BigDecimal(0.1));

	    oneDotOne = oneDotOne.divide(one, 4, BigDecimal.ROUND_FLOOR);

	    boolean assertIndice = (indiceVendaCrescente != null && (indiceVendaCrescente
		    .compareTo(one) == 0 || indiceVendaCrescente
		    .compareTo(oneDotOne) == 0));

	    assertTrue("Indice Venda Crescente : " + indiceVendaCrescente
		    + " Cota : " + cota.getId(), assertIndice);

	    Reporter.log("<p>Cota " + cota.getNomePessoa() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId()
		    + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>Numero : "
		    + cota.getNumero() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : "
		    + indiceVendaCrescente + "</p>");
	    Reporter.log(sbEstoqueLog.toString());

	} catch (Exception e) {
	    Assert.fail(e.getMessage());
	}
    }
}
