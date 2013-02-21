package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class VendaCrescenteTest {

    @Test(dataProvider = "getCotaEdicaoBaseUnicaPublicacaoList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void primeiroCenario(Cota cota) {

	try {

	    VendaCrescente vendaCrescente = new VendaCrescente(cota);
	    vendaCrescente.executar();
	    cota = (Cota) vendaCrescente.getGenericDTO();

	    StringBuilder sbEstoqueLog = new StringBuilder();

	    Iterator<ProdutoEdicao> itProdutoEdicao = cota
		    .getEdicoesRecebidas().iterator();

	    while (itProdutoEdicao.hasNext()) {

		ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		sbEstoqueLog
			.append("<p style='margin-left: 100px'>Produto Edicao</p>");
		sbEstoqueLog.append("<p style='margin-left: 150px'>ID : "
			+ produtoEdicao.getId() + "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 150px'>ID Produto : "
				+ produtoEdicao.getIdProduto() + "</p>");
		sbEstoqueLog
			.append("<p style='margin-left: 150px'>Quantidade Recebida : "
				+ produtoEdicao.getReparte() + "</p>");
		sbEstoqueLog.append("<p style='margin-left: 150px'>Venda : "
			+ produtoEdicao.getVenda() + "</p>");
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

	    Reporter.log("<p>Cota</p>");
	    Reporter.log("<p style='margin-left: 50px'>ID : " + cota.getId()
		    + "</p>");
	    // Reporter.log("<p style='margin-left: 50px'>Numero : "
	    // + cota.getNumero() + "</p>");
	    Reporter.log("<p style='margin-left: 50px'>-> Indice Venda Crescente : "
		    + indiceVendaCrescente + "</p>");
	    Reporter.log(sbEstoqueLog.toString());

	} catch (Exception e) {
	    Assert.fail(e.getMessage());
	}
    }
}
