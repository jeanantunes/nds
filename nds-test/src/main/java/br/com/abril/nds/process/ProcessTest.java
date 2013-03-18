package br.com.abril.nds.process;

import java.math.BigDecimal;
import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import br.com.abril.nds.dao.ProdutoEdicaoDAO;
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
import br.com.abril.nds.service.EstudoServiceEstudo;
import br.com.abril.nds.util.HTMLTableUtil;

public class ProcessTest {

    @Test
    @Parameters({ "produto" })
    public void testAllProcess(String produto) throws Exception {
	Estudo estudo = new Estudo();
	estudo.setPacotePadrao(BigDecimal.valueOf(10));
	estudo.setReparteDistribuir(BigDecimal.valueOf(1000));
	estudo.setReparteDistribuirInicial(BigDecimal.valueOf(1000));
	estudo.setProduto(montaProduto(produto));

	DefinicaoBases definicaoBases = new DefinicaoBases(estudo);
	definicaoBases.executar();

	SomarFixacoes somarFixacoes = new SomarFixacoes(estudo);
	somarFixacoes.executar();

	VerificarTotalFixacoes verificarTotalFixacoes = new VerificarTotalFixacoes(estudo);
	verificarTotalFixacoes.executar();

	EstudoServiceEstudo.calculate(estudo);

	MontaTabelaEstudos montaTabelaEstudos = new MontaTabelaEstudos(estudo);
	montaTabelaEstudos.executar();

	for (Cota cota : estudo.getCotas()) {

	    CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
	    correcaoVendas.executar();

	    Medias medias = new Medias(cota);
	    medias.executar();

	    VendaMediaFinal vendaMediaFinal = new VendaMediaFinal(cota);
	    vendaMediaFinal.executar();

	    Bonificacoes bonificacoes = new Bonificacoes(cota);
	    bonificacoes.executar();

	    AjusteCota ajusteCota = new AjusteCota(cota);
	    ajusteCota.executar();

	    JornaleirosNovos jornaleirosNovos = new JornaleirosNovos(cota);
	    jornaleirosNovos.executar();
	}

	AjusteReparte ajusteReparte = new AjusteReparte(estudo);
	ajusteReparte.executar();

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
	// for(Cota cota : estudo.getCotas()) {
	// Reporter.log("<br>Numero da cota: " + String.valueOf(cota.getNumero()));
	// }

	for (Cota cota : estudo.getCotas()) {
	    imprimeCota(cota);
	    imprimeEdicaoDaCota(cota.getEdicoesRecebidas());
	}
    }

    private void imprimeCota(Cota cota) {
	Reporter.log("<p>Cota:");
	Reporter.log(HTMLTableUtil.buildHTMLTable(cota));
    }

    private void imprimeEdicaoBase(List<ProdutoEdicaoBase> edicoesBase) {
	Reporter.log("<p>Edi&ccedil;&otilde;es Base:");
	Reporter.log(HTMLTableUtil.buildHTMLTable(edicoesBase));
    }

    private void imprimeEdicaoDaCota(List<ProdutoEdicao> edicoes) {
	Reporter.log("<p>Edi&ccedil;&otilde;es da Cota:");
	Reporter.log(HTMLTableUtil.buildHTMLTable(edicoes));
    }

    private ProdutoEdicaoBase montaProduto(String produto) {
	ProdutoEdicaoDAO produtoEdicaoDAO = new ProdutoEdicaoDAO();
	ProdutoEdicaoBase edicao = produtoEdicaoDAO.getLastProdutoEdicaoByIdProduto(produto);

	return edicao;
    }
}
