package br.com.abril.nds.process.correcaovendas;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class VendaCrescenteTest {

    @Test(dataProvider = "getEdicaoBaseFechadaList", dataProviderClass = CorrecaoVendasDataProvider.class)
    public void executarProcesso(Cota cota,
	    List<ProdutoEdicao> listProdutoEdicaoFechada) {

	try {

	    List<ProdutoEdicao> linkedProdutoEdicaoFechada = new LinkedList<ProdutoEdicao>();

	    int iProdutoEdicaoFechada = 0;
	    while (iProdutoEdicaoFechada < listProdutoEdicaoFechada.size()) {
		ProdutoEdicao produtoEdicao = listProdutoEdicaoFechada
			.get(iProdutoEdicaoFechada);

		if (produtoEdicao.getNome().equalsIgnoreCase("MALU.")) {
		    linkedProdutoEdicaoFechada.add(produtoEdicao);
		}

		iProdutoEdicaoFechada++;
	    }

	    VendaCrescente vendaCrescente = new VendaCrescente(cota,
		    linkedProdutoEdicaoFechada);
	    vendaCrescente.executar();
	    cota = (Cota) vendaCrescente.getGenericDTO();

	    BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();

	    BigDecimal one = BigDecimal.ONE;
	    BigDecimal oneDotOne = one.add(new BigDecimal(0.1));

	    oneDotOne = oneDotOne.divide(one, 4, BigDecimal.ROUND_FLOOR);

	    boolean assertIndice = (indiceVendaCrescente != null && (indiceVendaCrescente
		    .compareTo(one) == 0 || indiceVendaCrescente
		    .compareTo(oneDotOne) == 0));

	    assertTrue(assertIndice);

	} catch (Exception e) {
	    Assert.fail(e.getMessage());
	}
    }
}
