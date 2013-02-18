package br.com.abril.nds.process.definicaobases;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public class DefinicaoBasesTest {

    private DefinicaoBases bases;

    @BeforeMethod
    public void setUp() throws Exception {
	bases = new DefinicaoBases(new Estudo());
//	bases.setEdicoesRecebidasParaEstudoRaw(MockEdicoes.getEdicoesRandom());
    }

    @Test(dataProvider="getEdicoesInput", dataProviderClass=DefinicaoBasesDataProvider.class)
    public void testExecutarProcesso(List<ProdutoEdicao> edicoes) throws Exception {
	Reporter.log("<p>Edi&ccedil;&otilde;es recebidas da interface:<ul>");
	for (ProdutoEdicao edicao : edicoes) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getId().toString());
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
	
	bases.setEdicoesRecebidasParaEstudoRaw(edicoes);
	bases.executarProcesso();
	List<ProdutoEdicao> edicoesBase = bases.getEstudo().getEdicoesBase();
	assertNotNull(edicoesBase);
	assertTrue(bases.getEstudo().getEdicoesBase().size() > 0);
	
	Reporter.log("<p>Edi&ccedil;&otilde;es Base:<ul>");
	for (ProdutoEdicao edicao : edicoesBase) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getId().toString());
	    Reporter.log(edicao.getDataLancamento().toString());
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
    }
}
