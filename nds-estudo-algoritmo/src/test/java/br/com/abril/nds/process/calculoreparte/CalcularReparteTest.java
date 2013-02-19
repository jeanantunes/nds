package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.service.EstudoService;

public class CalcularReparteTest {

	private Estudo criarAmbiente(boolean configurado, BigDecimal pacotePadrao, BigDecimal reparteCalculado) {
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		if (configurado) {
			
		}
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);
		return estudo;
	}
	
	@Test
	public void testSemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(false, null, null);

		// Execução do Processo
		CalcularReparte calcularReparte = new CalcularReparte(estudo);
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, calcularReparte.getEstudo().getReparteDistribuir());
		for (Cota cota : calcularReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}
	
	@Test
	public void testAjustarReparteCalculadoComMultiplos() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, new BigDecimal(15), new BigDecimal(10));

		// Execução do Processo
		CalcularReparte calcularReparte = new CalcularReparte(estudo);
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, calcularReparte.getEstudo().getReparteDistribuir());
		for (Cota cota : calcularReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}
}
