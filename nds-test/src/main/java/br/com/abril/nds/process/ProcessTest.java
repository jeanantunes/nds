package br.com.abril.nds.process;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.calculoreparte.AjusteFinalReparte;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.montatabelaestudos.MontaTabelaEstudos;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;
import br.com.abril.nds.process.verificartotalfixacoes.VerificarTotalFixacoes;
import br.com.abril.nds.service.EstudoService;

public class ProcessTest {
    
    @Test
    @Parameters({"produtos"})
    public void testAllProcess(String produtos) throws Exception {
	Estudo estudo = new Estudo();
	estudo.setPacotePadrao(BigDecimal.valueOf(10));
	estudo.setReparteDistribuir(BigDecimal.valueOf(1000));
	estudo.setReparteDistribuirInicial(BigDecimal.valueOf(1000));
	
	DefinicaoBases definicaoBases = new DefinicaoBases(estudo);
	definicaoBases.setEdicoesRecebidasParaEstudoRaw(montaListEdicoesPorProduto(produtos));
	definicaoBases.executar();
	
	SomarFixacoes somarFixacoes = new SomarFixacoes(estudo);
	somarFixacoes.executar();
	
	VerificarTotalFixacoes verificarTotalFixacoes = new VerificarTotalFixacoes(estudo);
	verificarTotalFixacoes.executar();
	
	EstudoService.calculate(estudo);
	
	MontaTabelaEstudos montaTabelaEstudos = new MontaTabelaEstudos(estudo);
	montaTabelaEstudos.executar();
	
	for(Cota cota : estudo.getCotas()) {
	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();
	    
	    Medias medias = new Medias(cota);
	    medias.executar();
	}
	
	Bonificacoes bonificacoes = new Bonificacoes(estudo);
	bonificacoes.executar();
	
	AjusteCota ajusteCota = new AjusteCota(estudo);
	ajusteCota.executar();
	
	JornaleirosNovos jornaleirosNovos = new JornaleirosNovos(estudo);
	jornaleirosNovos.executar();
	
	for(Cota cota : estudo.getCotas()) {
	    VendaMediaFinal vendaMediaFinal = new VendaMediaFinal(cota);
	    vendaMediaFinal.executar();
	}
	
	AjusteReparte ajusteReparte = new AjusteReparte(estudo);
	ajusteReparte.executar();
	
	EstudoService.calculate(estudo);
	
	RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
	redutorAutomatico.executar();
	
	ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
	reparteMinimo.executar();
	
	ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
	reparteProporcional.executar();
	
	EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
	encalheMaximo.executar();
	
	ComplementarAutomatico complementarAutomatico = new ComplementarAutomatico(estudo);
	complementarAutomatico.executar();
	
	CalcularReparte calcularReparte = new CalcularReparte(estudo);
	calcularReparte.executar();
	
	AjusteFinalReparte ajusteFinalReparte = new AjusteFinalReparte(estudo);
	ajusteFinalReparte.executar();
	
	Reporter.log("<div id='content' style='overflow:scroll;width: 910px;height: 490px;'>");
	imprimeResultadoFinalEstudo(estudo);
	Reporter.log("<script> $('#content').parent().on('click', function() { $('.navigator-root').hide();$('.wrapper').css({'position':'relative','left':'0'}); }).end().css({'width': '1300px'}); </script>");
	Reporter.log("</div>");
    }
    
    private void imprimeResultadoFinalEstudo(Estudo estudo) {
	imprimeEdicaoBase(estudo.getEdicoesBase());
	
	Reporter.log("<br>Total de Cotas do Estudo: " + estudo.getCotas().size());
//	for(Cota cota : estudo.getCotas()) {
//	    Reporter.log("<br>Numero da cota: " + String.valueOf(cota.getNumero()));    
//	}
	
	for(Cota cota : estudo.getCotas()) {
	    imprimeCota(cota);
	    imprimeEdicaoDaCota(cota.getEdicoesRecebidas());
	}
    }
    
    private void imprimeCota(Cota cota) {
	Reporter.log("<p>Cota:<table border='1' cellspacing='0' cellpadding='2'>");
	imprimeCabecalhoCota();
	
	Class<? extends Cota> clazz = cota.getClass();
	Method[] methods = clazz.getMethods();
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		try {
		    logTD(method.invoke(cota));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		    e.printStackTrace();
		}
	    }
	}
	Reporter.log("</table>");
    }

    private void imprimeCabecalhoCota() {
	Method[] methods = Cota.class.getMethods();
	Reporter.log("<tr>");
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		Reporter.log("<td>");
		Reporter.log(name.replaceAll("is|get", ""));
		Reporter.log("</td>");
	    }
	}
	Reporter.log("</tr>");
    }

    private void imprimeEdicaoBase(List<ProdutoEdicaoBase> edicoesBase) {
	Reporter.log("<p>Edi&ccedil;&otilde;es Base:<table border='1' cellspacing='0' cellpadding='2'>");
	imprimeCabecalhoEdicaoBase();
	for (ProdutoEdicaoBase produtoEdicaoBase : edicoesBase) {
	    Reporter.log("<tr>");
	    edicaoBaseToTD(produtoEdicaoBase);
	    Reporter.log("</tr>");
	}
	Reporter.log("</table>");
    }

    private void imprimeCabecalhoEdicaoBase() {
	Method[] methods = ProdutoEdicaoBase.class.getMethods();
	Reporter.log("<tr>");
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		Reporter.log("<td>");
		Reporter.log(name.replaceAll("is|get", ""));
		Reporter.log("</td>");
	    }
	}
	Reporter.log("</tr>");
    }

    private void edicaoBaseToTD(ProdutoEdicaoBase produtoEdicaoBase) {
	Class<? extends ProdutoEdicaoBase> clazz = produtoEdicaoBase.getClass();
	Method[] methods = clazz.getMethods();
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		try {
		    logTD(method.invoke(produtoEdicaoBase));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		    e.printStackTrace();
		}
	    }
	}	
    }

    private void imprimeEdicaoDaCota(List<ProdutoEdicao> edicoes) {
	Reporter.log("<p>Edi&ccedil;&otilde;es da Cota:<table border='1' cellspacing='0' cellpadding='2'>");
	imprimeCabecalhoEdicao();
	for (ProdutoEdicao edicao : edicoes) {
	    Reporter.log("<tr>");
	    edicaoToTD(edicao);
	    Reporter.log("</tr>");
	}
	Reporter.log("</table>");
    }

    private void imprimeCabecalhoEdicao() {
	Method[] methods = ProdutoEdicao.class.getMethods();
	Reporter.log("<tr>");
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		Reporter.log("<td>");
		Reporter.log(name.replaceAll("is|get", ""));
		Reporter.log("</td>");
	    }
	}
	Reporter.log("</tr>");	
    }

    private void edicaoToTD(ProdutoEdicao edicao) {
	Class<? extends ProdutoEdicao> clazz = edicao.getClass();
	Method[] methods = clazz.getMethods();
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		try {
		    logTD(method.invoke(edicao));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private void logTD(Object object) {
	Reporter.log(envolveTD(object));
    }

    private String envolveTD(Object object) {
	return "<td>".concat(String.valueOf(object)).concat("</td>");
    }
    
    private List<ProdutoEdicaoBase> montaListEdicoesPorProduto(String produtos) {
	String[] listProdutos = produtos.split(",");
	List<ProdutoEdicaoBase> edicoes = new ArrayList<>();
	for (String codigoProduto : listProdutos) {
	    ProdutoEdicaoBase edicao = new ProdutoEdicaoBase();
	    edicao.setCodigoProduto(Long.parseLong(codigoProduto));
	    edicoes.add(edicao);
	}
	return edicoes;
    }
}
