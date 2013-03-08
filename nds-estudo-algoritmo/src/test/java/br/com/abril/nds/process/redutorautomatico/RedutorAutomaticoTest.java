package br.com.abril.nds.process.redutorautomatico;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.service.EstudoServiceEstudo;

public class RedutorAutomaticoTest {

	private BigDecimal menorVenda;
	
	private Estudo criarAmbiente(boolean configurado, BigDecimal reparteDistribuir, BigDecimal vendaMedia,
			BigDecimal vendaEdicaoMaisRecenteFechada, BigDecimal reparteCalculado, BigDecimal menorVenda) {
		Estudo estudo = new Estudo();
		Cota cota = new Cota();
		if (configurado) {
			estudo.setReparteDistribuir(reparteDistribuir);
			cota.setVendaMedia(vendaMedia);
			cota.setReparteCalculado(reparteCalculado);
			cota.setVendaEdicaoMaisRecenteFechada(vendaEdicaoMaisRecenteFechada);
			this.menorVenda = menorVenda;
		}
		estudo.setCotas(new ArrayList<Cota>());
		estudo.getCotas().add(cota);
		EstudoServiceEstudo.calculate(estudo);
		return estudo;
	}
	
	@Test
	public void testReparteDistribuir100VendaMediaCota3UltimaVenda10MenorVenda5() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(3), BigDecimal.valueOf(10),
				BigDecimal.ZERO, BigDecimal.valueOf(5));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(100), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir100VendaMediaCota3UltimaVenda0MenorVenda5RepCalculadoCota20() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(3), BigDecimal.valueOf(20),
				BigDecimal.ZERO, BigDecimal.valueOf(5));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(100), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir100VendaMediaCota10UltimaVenda0MenorVenda5RepCalculadoCota20() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.ZERO,
				BigDecimal.valueOf(20), BigDecimal.valueOf(5));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(100), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.valueOf(20), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir110VendaMediaCota10UltimaVenda0MenorVendaMeio() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(110), BigDecimal.valueOf(10), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.valueOf(0.5));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(110), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir0VendaMediaCota100MenorVenda0SemUltimaVenda() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.ZERO, BigDecimal.valueOf(100), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testMenorVendaSemReparteESemVendaMediaFinal() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(false, null, null, null, null, null);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia0() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia70() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(70), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(0.25), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia50() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia90() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(100), BigDecimal.valueOf(90), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte10ESomatoriaVendaMedia50() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.valueOf(10), BigDecimal.valueOf(50), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaSemReparteESomatoriaVendaMedia50() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(0.5), redutorAutomatico.getMenorVenda());
	}
}
