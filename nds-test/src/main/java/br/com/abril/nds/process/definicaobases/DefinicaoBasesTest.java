package br.com.abril.nds.process.definicaobases;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
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

    @Test(enabled=false, dataProvider="getEdicoesInput", dataProviderClass=DefinicaoBasesDataProvider.class)
    public void testExecutarProcesso(List<ProdutoEdicao> edicoes) throws Exception {
	Reporter.log("<p>Edi&ccedil;&otilde;es recebidas da interface:<ul>");
	for (ProdutoEdicao edicao : edicoes) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getCodigoProduto().toString());
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
	    Reporter.log(edicao.getCodigoProduto().toString());
	    Reporter.log(edicao.getNumeroEdicao().toString());
	    Reporter.log(edicao.getDataLancamento().toString());
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
    }
    
    @Test
    @Parameters({"produtos"})
    public void testDefinicaoBasesPorParametroXML(String produtos) throws Exception {
	Reporter.log("<p>Produtos recebidas da interface:");
	Reporter.log(produtos);
	
	bases.setEdicoesRecebidasParaEstudoRaw(montaListEdicoesPorProduto(produtos));
	bases.executarProcesso();
	List<ProdutoEdicao> edicoesBase = bases.getEstudo().getEdicoesBase();
	assertNotNull(edicoesBase);
	assertTrue(bases.getEstudo().getEdicoesBase().size() > 0);
	
	Reporter.log("<p>Edi&ccedil;&otilde;es Base:<ul>");
	for (ProdutoEdicao edicao : edicoesBase) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getCodigoProduto().toString());
	    Reporter.log(edicao.getNumeroEdicao().toString());
	    Reporter.log(edicao.getDataLancamento().toString());
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
    }

    private List<ProdutoEdicao> montaListEdicoesPorProduto(String produtos) {
	String[] listProdutos = produtos.split(",");
	List<ProdutoEdicao> edicoes = new ArrayList<>();
	for (String codigoProduto : listProdutos) {
	    ProdutoEdicao edicao = new ProdutoEdicao();
	    edicao.setCodigoProduto(Long.parseLong(codigoProduto));
	    edicoes.add(edicao);
	}
	return edicoes;
    }
}
