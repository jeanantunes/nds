package br.com.abril.nds.process.ajustereparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class AjusteReparteTest {
	
	@Test
	public void testCotaComParametroZerado() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setPacotePadrao(BigDecimal.ZERO);
		Cota cota = new Cota();
		cota.setVendaMediaMaisN(BigDecimal.ZERO);
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaComParametro100() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setPacotePadrao(new BigDecimal(10));
		Cota cota = new Cota();
		cota.setVendaMediaMaisN(new BigDecimal(100));
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();

		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(100), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaComParametro20EVendaMedia50() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setPacotePadrao(new BigDecimal(5));
		Cota cota = new Cota();
		cota.setVendaMediaMaisN(new BigDecimal(20));
		cota.setVendaMedia(new BigDecimal(50));
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(70), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaComParametro20VendaMedia50EPacotePadrao25() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setPacotePadrao(new BigDecimal(25));
		Cota cota = new Cota();
		cota.setVendaMediaMaisN(new BigDecimal(20));
		cota.setVendaMedia(new BigDecimal(50));
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(75), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaSemConfiguracao() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(0), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaSemPacotePadrao() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		cota.setVendaMediaMaisN(new BigDecimal(30));
		cota.setVendaMedia(new BigDecimal(10));
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(40), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testVerificaClassificacaoCotaComParametroConfigurado() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		cota.setVendaMediaMaisN(new BigDecimal(30));
		cota.setVendaMedia(new BigDecimal(10));
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.ReparteFixado, c.getClassificacao());
		}
	}
	
	@Test
	public void testVerificaClassificacaoCotaSemParametroConfigurado() throws Exception {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		estudo.getCotas().add(cota);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
		}
	}
}
