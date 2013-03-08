package br.com.abril.nds.process.definicaobases;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class DefinicaoBasesTest {

    private Estudo estudo;
    private DefinicaoBases bases;

    @BeforeTest
    public void setUpEstudo(ITestContext context) {
	estudo = new Estudo();
	context.setAttribute("estudo", estudo);
    }
    
    @BeforeMethod
    public void setUp() throws Exception {
	bases = new DefinicaoBases(estudo);
//	bases.setEdicoesRecebidasParaEstudoRaw(MockEdicoes.getEdicoesRandom());
    }

    @Test(enabled=false, dataProvider="getEdicoesInput", dataProviderClass=DefinicaoBasesDataProvider.class)
    public void testExecutarProcesso(List<ProdutoEdicaoBase> edicoes) throws Exception {
	Reporter.log("<p>Edi&ccedil;&otilde;es recebidas da interface:<ul>");
	for (ProdutoEdicaoBase edicao : edicoes) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getCodigoProduto().toString());
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
	
//	bases.setEdicoesRecebidasParaEstudoRaw(edicoes);
	bases.executar();
	List<ProdutoEdicaoBase> edicoesBase = bases.getEstudo().getEdicoesBase();
	assertNotNull(edicoesBase);
	assertTrue(bases.getEstudo().getEdicoesBase().size() > 0);
	
	Reporter.log("<p>Edi&ccedil;&otilde;es Base:<ul>");
	Reporter.log("<li>p.CODIGO|l.ID|pe.NUMERO_EDICAO|l.DATA_LCTO_DISTRIBUIDOR</li>");
	for (ProdutoEdicaoBase edicao : edicoesBase) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getCodigoProduto().toString());
	    Reporter.log(edicao.getIdLancamento().toString());
	    Reporter.log(edicao.getNumeroEdicao().toString());
	    Reporter.log(edicao.getDataLancamento().toString());
	    Reporter.log(String.valueOf(edicao.isParcial()));
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
    }
    
    @Test
    @Parameters({"produtos"})
    public void testDefinicaoBasesPorParametroXML(String produtos, ITestContext context) throws Exception {
	Set<String> attributeNames = context.getAttributeNames();
	Reporter.log("@BeforeTest ITestContext AttributeNames:");
	for (String attribute : attributeNames) {
	    Reporter.log(attribute);
	    Estudo attrObj = (Estudo)context.getAttribute(attribute);
	    Reporter.log(StringUtils.join(attrObj.getEdicoesBase(), ";"));
	}
	
	Reporter.log("<p>Produtos recebidas da interface:");
	Reporter.log(produtos);
	
//	bases.setEdicoesRecebidasParaEstudoRaw(montaListEdicoesPorProduto(produtos));
	bases.executarProcesso();
	List<ProdutoEdicaoBase> edicoesBase = bases.getEstudo().getEdicoesBase();
	assertNotNull(edicoesBase);
	assertTrue(bases.getEstudo().getEdicoesBase().size() > 0);
	
	Reporter.log("<p>Edi&ccedil;&otilde;es Base:<table border='1' cellspacing='0' cellpadding='2'>");
	Reporter.log("<tr><td>p.CODIGO</td><td>l.ID</td><td>pe.NUMERO_EDICAO</td><td>l.DATA_LCTO_DISTRIBUIDOR</td><td>l.TIPO_LANCAMENTO</td></tr>");
	for (ProdutoEdicaoBase edicao : edicoesBase) {
	    Reporter.log("<tr>");
	    logTD(edicao.getCodigoProduto());
	    logTD(edicao.getIdLancamento());
	    logTD(edicao.getNumeroEdicao());
	    logTD(edicao.getDataLancamento());
	    logTD(edicao.isParcial());
	    Reporter.log("</tr>");
	}
	Reporter.log("</table>");
	
	Reporter.log(StringUtils.join(edicoesBase, "<br>"));
    }

    private void logTD(Object object) {
	Reporter.log(envolveTD(object));
    }

    private String envolveTD(Object object) {
	return "<td>".concat(String.valueOf(object)).concat("</td>");
    }

//    private List<ProdutoEdicaoBase> montaListEdicoesPorProduto(String produtos) {
//	String[] listProdutos = produtos.split(",");
//	List<ProdutoEdicaoBase> edicoes = new ArrayList<>();
//	for (String codigoProduto : listProdutos) {
//	    ProdutoEdicaoBase edicao = new ProdutoEdicaoBase();
//	    edicao.setCodigoProduto(Long.parseLong(codigoProduto));
//	    edicoes.add(edicao);
//	}
//	return edicoes;
//    }
}
