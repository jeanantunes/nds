package br.com.abril.nds.process.encalhemaximo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.service.EstudoService;

public class EncalheMaximoTest {

	private Estudo criarAmbiente(boolean configurado, BigDecimal reparteDistribuir, BigDecimal percentualEncalheMaximo,
			BigDecimal vendaMedia) {
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		if (configurado) {
			estudo.setReparteDistribuir(reparteDistribuir);
			cota.setPercentualEncalheMaximo(percentualEncalheMaximo);
			cota.setVendaMedia(vendaMedia);
		}
		estudo.setCotas(new ArrayList<Cota>());
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);
		return estudo;
	}
	
	@Test
	public void testSemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(false, null, null, null);
		
		// Execução do Processo
		EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
		encalheMaximo.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, encalheMaximo.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testReparte100IndiceEncalhe10VendaMedia25() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, new BigDecimal(100), new BigDecimal(10), new BigDecimal(25));

		// Execução do Processo
		EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
		encalheMaximo.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(72), encalheMaximo.getEstudo().getReparteDistribuir());
		for (Cota c : encalheMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(28), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparte50IndiceEncalhe40VendaMedia20() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, new BigDecimal(50), new BigDecimal(40), new BigDecimal(20));

		// Execução do Processo
		EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
		encalheMaximo.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(50), encalheMaximo.getEstudo().getReparteDistribuir());
		for (Cota c : encalheMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(0), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparte50IndiceEncalhe0VendaMedia20() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, new BigDecimal(50), BigDecimal.ZERO, new BigDecimal(20));

		// Execução do Processo
		EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
		encalheMaximo.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(50), encalheMaximo.getEstudo().getReparteDistribuir());
		for (Cota c : encalheMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(0), c.getReparteCalculado());
		}
	}
}
