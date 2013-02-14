package br.com.abril.nds.process.reparteminimo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class ReparteMinimoTest {

	@Test
	public void testPacotePadrao0ReparteMinimo0() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(true);
		estudo.setPacotePadrao(BigDecimal.ZERO);
		Cota cota = new Cota();
		cota.setReparteMinimo(BigDecimal.ZERO);
		estudo.getCotas().add(cota);
		
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
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(true);
		estudo.setPacotePadrao(new BigDecimal(10));
		Cota cota = new Cota();
		cota.setReparteMinimo(BigDecimal.ZERO);
		estudo.getCotas().add(cota);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(10), c.getReparteMinimo());
		}
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo15() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(true);
		estudo.setPacotePadrao(new BigDecimal(10));
		Cota cota = new Cota();
		cota.setReparteMinimo(new BigDecimal(15));
		estudo.getCotas().add(cota);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(15), c.getReparteMinimo());
		}
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo15RepDistribuir100() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(true);
		estudo.setPacotePadrao(new BigDecimal(10));
		estudo.setReparteDistribuir(new BigDecimal(100));
		Cota cota = new Cota();
		cota.setReparteMinimo(new BigDecimal(15));
		estudo.getCotas().add(cota);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		reparteMinimo.executarProcesso();
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(15), c.getReparteMinimo());
		}
		assertEquals(new BigDecimal(85), reparteMinimo.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testPacotePadrao10ReparteMinimo15RepDistribuir10() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(true);
		estudo.setPacotePadrao(new BigDecimal(10));
		estudo.setReparteDistribuir(new BigDecimal(10));
		Cota cota = new Cota();
		cota.setReparteMinimo(new BigDecimal(15));
		estudo.getCotas().add(cota);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		try {
			reparteMinimo.executarProcesso();
		} catch (Exception ex) {
			// Deverá sempre cair nessa exceção
		}
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(15), c.getReparteMinimo());
		}
		assertEquals(new BigDecimal(10), reparteMinimo.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testPacotePadrao8ReparteMinimo7RepDistribuir100() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setDistribuicaoPorMultiplos(true);
		estudo.setPacotePadrao(new BigDecimal(8));
		estudo.setReparteDistribuir(new BigDecimal(100));
		Cota cota = new Cota();
		cota.setReparteMinimo(new BigDecimal(7));
		estudo.getCotas().add(cota);
		
		// Execução do Processo
		ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
		try {
			reparteMinimo.executarProcesso();
		} catch (Exception ex) {
			// Deverá sempre cair nessa exceção
		}
		
		// Validação do teste
		for (Cota c : reparteMinimo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(7), c.getReparteMinimo());
		}
		assertEquals(new BigDecimal(93), reparteMinimo.getEstudo().getReparteDistribuir());
	}
}
