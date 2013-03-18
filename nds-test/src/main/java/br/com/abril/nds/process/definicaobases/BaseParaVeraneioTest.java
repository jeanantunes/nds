package br.com.abril.nds.process.definicaobases;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.LinkedList;

<<<<<<< HEAD
//import org.joda.time.LocalDate;
=======
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class BaseParaVeraneioTest {

    @Autowired
    private BaseParaVeraneio baseParaVeraneio;
    
    @BeforeMethod
    public void setUp() throws Exception {
	LinkedList<ProdutoEdicaoBase> edicoes = new LinkedList<ProdutoEdicaoBase>();
	edicoes.add(getEdicao());
	Estudo estudo = new Estudo();
	estudo.setPracaVeraneio(true);
	estudo.setEdicoesBase(edicoes);
	baseParaVeraneio.setEstudo(estudo);
    }

    @Test
    public void testBasesParaVeraneio() throws Exception {
	baseParaVeraneio.executar();
	LinkedList<ProdutoEdicaoBase> edicoesBase = baseParaVeraneio.getEstudo().getEdicoesBase();
	assertNotNull(edicoesBase);
	assertTrue(edicoesBase.size() > 0);
	
	Reporter.log("<p>Edi&ccedil;&otilde;es Base Veraneio:<ul>");
	for (ProdutoEdicaoBase edicao : edicoesBase) {
	    Reporter.log("<li>");
	    Reporter.log(edicao.getId().toString());
	    Reporter.log(edicao.getDataLancamento().toString());
	    Reporter.log("</li>");
	}
	Reporter.log("</ul>");
    }

    private ProdutoEdicaoBase getEdicao() {
	ProdutoEdicaoBase produtoEdicao = new ProdutoEdicaoBase();
	produtoEdicao.setId(134437L);
	produtoEdicao.setIdLancamento(92826L);
	produtoEdicao.setEdicaoAberta(true);
//	produtoEdicao.setDataLancamento(LocalDate.parse("2013-02-11").toDate());
	produtoEdicao.setColecao(false);
	produtoEdicao.setParcial(false);
	produtoEdicao.setCodigoProduto("61310001");
	
	return produtoEdicao;
    }
}
