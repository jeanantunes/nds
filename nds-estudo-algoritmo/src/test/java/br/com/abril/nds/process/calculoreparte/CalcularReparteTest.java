package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.service.EstudoServiceEstudo;

public class CalcularReparteTest {

    @Autowired
    private CalcularReparte calcularReparte;
    
    private EstudoTransient criarAmbiente(boolean configurado, Integer distribuicaoPorMultiplos, BigDecimal pacotePadrao, BigDecimal vendaMedia,
	    BigDecimal reparteCalculado, BigDecimal reparteDistribuir, boolean temEdicaoBaseFechada) {
	EstudoTransient estudo = new EstudoTransient();
	estudo.setProduto(new ProdutoEdicaoEstudo());
	estudo.setEdicoesBase(new LinkedList<ProdutoEdicaoEstudo>());
	CotaEstudo cota = new CotaEstudo();
	if (configurado) {
	    if (temEdicaoBaseFechada) {
		ProdutoEdicaoEstudo edicao = new ProdutoEdicaoEstudo();
		edicao.setEdicaoAberta(false);
		estudo.getEdicoesBase().add(edicao);
	    }
	    estudo.setDistribuicaoPorMultiplos(distribuicaoPorMultiplos);
	    estudo.setPacotePadrao(pacotePadrao);
	    estudo.setReparteDistribuir(reparteDistribuir);
	    ProdutoEdicao base = new ProdutoEdicao();
	    base.setVenda(vendaMedia);
	    cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicao>());
	    cota.getEdicoesRecebidas().add(base);
	    cota.setReparteCalculado(reparteCalculado);
	}
	estudo.setCotas(new ArrayList<Cota>());
	estudo.getCotas().add(cota);
	EstudoServiceEstudo.calculate(estudo);
	estudo.setExcedente(estudo.getReparteDistribuir().subtract(estudo.getSomatoriaVendaMedia()));
	return estudo;
    }

    @Test
    public void testSemConfiguracao() throws Exception {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(false, false, null, null, null, null, false);

	// Execução do Processo
	calcularReparte.setEstudo(estudo);
	calcularReparte.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.ZERO, calcularReparte.getEstudo().getReparteDistribuir());
	for (Cota cota : calcularReparte.getEstudo().getCotas()) {
	    assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
	}
    }

    @Test
    public void testAjustarReparteCalculadoComMultiplos() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(true, true, new BigDecimal(5), new BigDecimal(15), new BigDecimal(10), new BigDecimal(100), true);

	// Execução do Processo
	calcularReparte.setEstudo(estudo);
	calcularReparte.calcularAjusteReparte();

	// Validação do teste
	assertEquals(new BigDecimal(100), calcularReparte.getEstudo().getReparteDistribuir());
	assertEquals(new BigDecimal(15), calcularReparte.getEstudo().getAjusteReparte());
	for (Cota cota : calcularReparte.getEstudo().getCotas()) {
	    assertEquals(new BigDecimal(10), cota.getReparteCalculado());
	    assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
	}
    }

    @Test
    public void testAjustarReparteCalculadoSemMultiplos() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(true, false, new BigDecimal(5), new BigDecimal(15), new BigDecimal(10), new BigDecimal(100), true);

	// Execução do Processo
	calcularReparte.setEstudo(estudo);
	calcularReparte.calcularAjusteReparte();

	// Validação do teste
	assertEquals(new BigDecimal(100), calcularReparte.getEstudo().getReparteDistribuir());
	assertEquals(new BigDecimal(15), calcularReparte.getEstudo().getAjusteReparte());
	for (Cota cota : calcularReparte.getEstudo().getCotas()) {
	    assertEquals(new BigDecimal(10), cota.getReparteCalculado());
	    assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
	}
    }
}
