package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.service.EstudoServiceEstudo;

public class MinimoMaximoTest {

    @Autowired
    private MinimoMaximo minimoMaximo;

    private Estudo criarAmbiente(BigDecimal reparteMinimo, BigDecimal reparteMaximo, BigDecimal reparteCalculado, boolean mix) {
	Estudo estudo = new Estudo();
	Cota cota = new Cota();
	cota.setReparteMinimo(reparteMinimo);
	cota.setReparteMaximo(reparteMaximo);
	cota.setReparteCalculado(reparteCalculado);
	cota.setMix(mix);
	estudo.setCotas(new ArrayList<Cota>());
	estudo.getCotas().add(cota);
	EstudoServiceEstudo.calculate(estudo);
	return estudo;
    }

    @Test
    public void testReparteMinimoMaiorQueMaximo() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(40), BigDecimal.valueOf(10), BigDecimal.valueOf(40), false);

	// Execução do Processo
	minimoMaximo.setEstudo(estudo);
	try {
	    minimoMaximo.executarProcesso();
	} catch (Exception e) {
	}

	// Validação do teste
	for (Cota c : minimoMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(40), c.getReparteCalculado());
	    assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
	}
    }

    @Test
    public void testRepCalculado40RepMinimo20RepMaximo50() throws Exception {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(20), BigDecimal.valueOf(50), BigDecimal.valueOf(40), false);

	// Execução do Processo
	minimoMaximo.setEstudo(estudo);
	minimoMaximo.executarProcesso();

	// Validação do teste
	for (Cota c : minimoMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(40), c.getReparteCalculado());
	    assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
	}
    }

    @Test
    public void testRepCalculado10RepMinimo20RepMaximo50() throws Exception {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(20), BigDecimal.valueOf(50), BigDecimal.valueOf(10), false);

	// Execução do Processo
	minimoMaximo.setEstudo(estudo);
	minimoMaximo.executarProcesso();

	// Validação do teste
	for (Cota c : minimoMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(20), c.getReparteCalculado());
	    assertEquals(ClassificacaoCota.MaximoMinimo, c.getClassificacao());
	}
    }

    @Test
    public void testRepCalculado60RepMinimo20RepMaximo50() throws Exception {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(20), BigDecimal.valueOf(50), BigDecimal.valueOf(60), true);

	// Execução do Processo
	minimoMaximo.setEstudo(estudo);
	minimoMaximo.executarProcesso();

	// Validação do teste
	for (Cota c : minimoMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(50), c.getReparteCalculado());
	    assertEquals(ClassificacaoCota.CotaMix, c.getClassificacao());
	}
    }
}
