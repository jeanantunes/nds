package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class CalcularReparteTest {

	private Estudo criarAmbiente(boolean configurado) {
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		if (configurado) {
			
		}
		estudo.getCotas().add(cota);
		estudo.calculate();
		return estudo;
	}
	
	@Test
	public void testSemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(false);

		// Execução do Processo
		CalcularReparte calcularReparte = new CalcularReparte(estudo);
		
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, calcularReparte.getEstudo().getReparteDistribuir());
		for (Cota cota : calcularReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}
	
	@Test
	public void testemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true);

		// Execução do Processo
		CalcularReparte calcularReparte = new CalcularReparte(estudo);
		
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, calcularReparte.getEstudo().getReparteDistribuir());
		for (Cota cota : calcularReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}
}
