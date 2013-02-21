package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.service.EstudoService;

public class MinimoMaximoTest {

	private Estudo criarAmbiente(BigDecimal reparteMinimo, BigDecimal reparteMaximo,
			BigDecimal reparteCalculado, boolean mix) {
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		cota.setReparteMinimo(reparteMinimo);
		cota.setReparteMaximo(reparteMaximo);
		cota.setReparteCalculado(reparteCalculado);
		cota.setMix(mix);
		estudo.setCotas(new ArrayList<Cota>());
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);
		return estudo;
	}
	
	@Test
	public void testReparteMinimoMaiorQueMaximo() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(new BigDecimal(40), new BigDecimal(10), new BigDecimal(40), false);

		// Execução do Processo
		MinimoMaximo minimoMaximo = new MinimoMaximo(estudo);
		try {
			minimoMaximo.executarProcesso();
		} catch (Exception e) {
		}
		
		// Validação do teste
		for (Cota c : minimoMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(40), c.getReparteCalculado());
			assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
		}
	}
	
	@Test
	public void testRepCalculado40RepMinimo20RepMaximo50() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(new BigDecimal(20), new BigDecimal(50), new BigDecimal(40), false);

		// Execução do Processo
		MinimoMaximo minimoMaximo = new MinimoMaximo(estudo);
		minimoMaximo.executarProcesso();
		
		// Validação do teste
		for (Cota c : minimoMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(40), c.getReparteCalculado());
			assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
		}
	}
	
	@Test
	public void testRepCalculado10RepMinimo20RepMaximo50() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(new BigDecimal(20), new BigDecimal(50), new BigDecimal(10), false);

		// Execução do Processo
		MinimoMaximo minimoMaximo = new MinimoMaximo(estudo);
		minimoMaximo.executarProcesso();
		
		// Validação do teste
		for (Cota c : minimoMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(20), c.getReparteCalculado());
			assertEquals(ClassificacaoCota.MaximoMinimo, c.getClassificacao());
		}
	}
	
	@Test
	public void testRepCalculado60RepMinimo20RepMaximo50() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(new BigDecimal(20), new BigDecimal(50), new BigDecimal(60), true);

		// Execução do Processo
		MinimoMaximo minimoMaximo = new MinimoMaximo(estudo);
		minimoMaximo.executarProcesso();
		
		// Validação do teste
		for (Cota c : minimoMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(50), c.getReparteCalculado());
			assertEquals(ClassificacaoCota.CotaMix, c.getClassificacao());
		}
	}
}
