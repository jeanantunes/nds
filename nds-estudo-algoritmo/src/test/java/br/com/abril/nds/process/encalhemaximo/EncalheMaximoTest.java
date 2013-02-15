package br.com.abril.nds.process.encalhemaximo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class EncalheMaximoTest {

	@Test
	public void testSemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		estudo.getCotas().add(cota);

		// Execução do Processo
		EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
		encalheMaximo.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, encalheMaximo.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testReparte100IndiceEncalhe10VendaMedia25() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		Cota cota = new Cota();
		cota.setPercentualEncalheMaximo(new BigDecimal(10));
		cota.setVendaMedia(new BigDecimal(25));
		estudo.getCotas().add(cota);
		estudo.calculate();

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
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(50));
		Cota cota = new Cota();
		cota.setPercentualEncalheMaximo(new BigDecimal(40));
		cota.setVendaMedia(new BigDecimal(20));
		estudo.getCotas().add(cota);
		estudo.calculate();

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
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(50));
		Cota cota = new Cota();
		cota.setPercentualEncalheMaximo(new BigDecimal(0));
		cota.setVendaMedia(new BigDecimal(20));
		estudo.getCotas().add(cota);
		estudo.calculate();

		// Execução do Processo
		EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
		encalheMaximo.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(50), encalheMaximo.getEstudo().getReparteDistribuir());
		for (Cota c : encalheMaximo.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(0), c.getReparteCalculado());
		}
	}
//	indiceEncalheMaximo
//	vendaMedia
//	reparteDistribuir
}
