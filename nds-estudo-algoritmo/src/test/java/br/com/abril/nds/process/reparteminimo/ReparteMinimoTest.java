package br.com.abril.nds.process.reparteminimo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class ReparteMinimoTest {

	private Estudo criarAmbiente(boolean distribuicaoPorMultiplos, BigDecimal pacotePadrao,
			BigDecimal reparteDistribuir, BigDecimal reparteMinimo) {
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(distribuicaoPorMultiplos);
		estudo.setPacotePadrao(pacotePadrao);
		estudo.setReparteDistribuir(reparteDistribuir);
		Cota cota = new Cota();
		cota.setReparteMinimo(reparteMinimo);
		estudo.setCotas(new ArrayList<Cota>());
		estudo.getCotas().add(cota);
		return estudo;
	}
	
	@Test
	public void testPacotePadrao0ReparteMinimo0() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteMinimo());
		}
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo0() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(10), BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(10), c.getReparteMinimo());
		}
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo15() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(10), BigDecimal.ZERO, BigDecimal.valueOf(15));
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(15), c.getReparteMinimo());
		}
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo15RepDistribuir100() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(15));
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(15), c.getReparteMinimo());
		}
		assertEquals(BigDecimal.valueOf(85), reparteMinimo.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo15RepDistribuir10() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(15));
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		try {
			reparteMinimo.executarProcesso();
		} catch (Exception ex) {
			// Deverá sempre cair nessa exceção
		}
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(15), c.getReparteMinimo());
		}
		assertEquals(BigDecimal.valueOf(10), reparteMinimo.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testPacotePadrao8ReparteMinimo7RepDistribuir100() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(8), BigDecimal.valueOf(100), BigDecimal.valueOf(7));
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		try {
			reparteMinimo.executarProcesso();
		} catch (Exception ex) {
			// Deverá sempre cair nessa exceção
		}
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(7), c.getReparteMinimo());
		}
		assertEquals(BigDecimal.valueOf(93), reparteMinimo.getEstudo().getReparteDistribuir());
	}
}
