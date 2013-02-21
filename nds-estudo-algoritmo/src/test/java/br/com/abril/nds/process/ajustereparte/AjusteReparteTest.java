package br.com.abril.nds.process.ajustereparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class AjusteReparteTest {
	
	private Estudo criarAmbiente(boolean configurado, BigDecimal pacotePadrao, BigDecimal vendaMediaMaisN, BigDecimal vendaMedia) {
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		if (configurado) {
			estudo.setPacotePadrao(pacotePadrao);
			cota.setVendaMediaMaisN(vendaMediaMaisN);
			cota.setVendaMedia(vendaMedia);
		}
		estudo.setCotas(new ArrayList<Cota>());
		estudo.getCotas().add(cota);
		// calculate() não esta sendo chamado porque a venda média já esta sendo informada
		//estudo.calculate(); 
		return estudo;
	}
	
	@Test
	public void testCotaComParametroZerado() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

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
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.ZERO);

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();

		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(100), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaComParametro20EVendaMedia50() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(5), BigDecimal.valueOf(20), BigDecimal.valueOf(50));

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(70), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaComParametro20VendaMedia50EPacotePadrao25() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(25), BigDecimal.valueOf(20), BigDecimal.valueOf(50));

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(75), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaSemConfiguracao() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(false, null, null, null);
		
		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testCotaSemPacotePadrao() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.ZERO, BigDecimal.valueOf(30), BigDecimal.valueOf(10));

		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(40), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testVerificaClassificacaoCotaComParametroConfigurado() throws Exception {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.ZERO, BigDecimal.valueOf(30), BigDecimal.valueOf(10));

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
		Estudo estudo = criarAmbiente(false, null, null, null);
		
		// Execução do processo
		AjusteReparte ajusteReparte = new AjusteReparte(estudo);
		ajusteReparte.executarProcesso();
		
		// Validação do teste
		for (Cota c : ajusteReparte.getEstudo().getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
		}
	}
}
