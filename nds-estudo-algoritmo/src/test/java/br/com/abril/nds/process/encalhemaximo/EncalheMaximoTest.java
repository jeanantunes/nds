package br.com.abril.nds.process.encalhemaximo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.service.EstudoServiceEstudo;

public class EncalheMaximoTest {

    @Autowired
    private EncalheMaximo encalheMaximo;
    
    private Estudo criarAmbiente(boolean configurado, BigDecimal reparteDistribuir, BigDecimal percentualEncalheMaximo, BigDecimal vendaMedia) {
	Estudo estudo = new Estudo();
	Cota cota = new Cota();
	if (configurado) {
	    estudo.setReparteDistribuir(reparteDistribuir);
	    cota.setPercentualEncalheMaximo(percentualEncalheMaximo);
	    cota.setVendaMedia(vendaMedia);
	}
	estudo.setCotas(new ArrayList<Cota>());
	estudo.getCotas().add(cota);
	EstudoServiceEstudo.calculate(estudo);
	return estudo;
    }

    @Test
    public void testSemConfiguracao() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(false, null, null, null);

	// Execução do Processo
	encalheMaximo.setEstudo(estudo);
	encalheMaximo.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.ZERO, encalheMaximo.getEstudo().getReparteDistribuir());
    }

    @Test
    public void testReparte100IndiceEncalhe10VendaMedia25() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(25));

	// Execução do Processo
	encalheMaximo.setEstudo(estudo);
	encalheMaximo.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(72), encalheMaximo.getEstudo().getReparteDistribuir());
	for (Cota c : encalheMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(28), c.getReparteCalculado());
	}
    }

    @Test
    public void testReparte50IndiceEncalhe40VendaMedia20() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(50), BigDecimal.valueOf(40), BigDecimal.valueOf(20));

	// Execução do Processo
	encalheMaximo.setEstudo(estudo);
	encalheMaximo.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(50), encalheMaximo.getEstudo().getReparteDistribuir());
	for (Cota c : encalheMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
	}
    }

    @Test
    public void testReparte50IndiceEncalhe0VendaMedia20() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(50), BigDecimal.ZERO, BigDecimal.valueOf(20));

	// Execução do Processo
	encalheMaximo.setEstudo(estudo);
	encalheMaximo.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(50), encalheMaximo.getEstudo().getReparteDistribuir());
	for (Cota c : encalheMaximo.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
	}
    }
}
