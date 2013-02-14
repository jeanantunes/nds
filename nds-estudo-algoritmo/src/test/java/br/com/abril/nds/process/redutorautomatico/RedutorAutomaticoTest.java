package br.com.abril.nds.process.redutorautomatico;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class RedutorAutomaticoTest {

	@Test
	public void testReparteDistribuir100VendaMediaCota3UltimaVenda10MenorVenda5() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		Cota cota = new Cota();
		cota.setVendaMedia(new BigDecimal(3));
		cota.setVendaEdicaoMaisRecenteFechada(new BigDecimal(10));
		estudo.getCotas().add(cota);
		BigDecimal menorVenda = new BigDecimal(5);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(new BigDecimal(100), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir100VendaMediaCota3UltimaVenda0MenorVenda5RepCalculadoCota20() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		Cota cota = new Cota();
		cota.setVendaMedia(new BigDecimal(3));
		cota.setVendaEdicaoMaisRecenteFechada(BigDecimal.ZERO);
		cota.setReparteCalculado(new BigDecimal(20));
		estudo.getCotas().add(cota);
		BigDecimal menorVenda = new BigDecimal(5);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(new BigDecimal(100), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir100VendaMediaCota10UltimaVenda0MenorVenda5RepCalculadoCota20() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		Cota cota = new Cota();
		cota.setVendaMedia(new BigDecimal(10));
		cota.setVendaEdicaoMaisRecenteFechada(BigDecimal.ZERO);
		cota.setReparteCalculado(new BigDecimal(20));
		estudo.getCotas().add(cota);
		BigDecimal menorVenda = new BigDecimal(5);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(new BigDecimal(100), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(20), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir110VendaMediaCota10UltimaVenda0MenorVendaMeio() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(110));
		Cota cota = new Cota();
		cota.setVendaMedia(new BigDecimal(10));
		cota.setVendaEdicaoMaisRecenteFechada(BigDecimal.ZERO);
		estudo.getCotas().add(cota);
		BigDecimal menorVenda = new BigDecimal(0.5);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.setMenorVenda(menorVenda);
		redutorAutomatico.calcularRedutorAutomatico();
		
		// Validação do teste
		assertEquals(new BigDecimal(110), redutorAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : redutorAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testReparteDistribuir0VendaMediaCota100MenorVenda0SemUltimaVenda() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(BigDecimal.ZERO);
		Cota cota = new Cota();
		cota.setVendaMedia(new BigDecimal(100));
		estudo.getCotas().add(cota);
		BigDecimal menorVenda = BigDecimal.ZERO;
		
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
		Estudo estudo = new Estudo();
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		// TODO: checar se "É possível que o %Excedente seja igual a 0 (zero)?
		// Se sim como agir nessa situação, qual será o valor da menorVenda?"
		assertEquals(new BigDecimal(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia0() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setSomatoriaVendaMediaFinal(BigDecimal.ZERO);
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		// TODO: checar se "É possível que o %Excedente seja igual a 0 (zero)?
		// Se sim como agir nessa situação, qual será o valor da menorVenda?"
		assertEquals(new BigDecimal(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia70() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setSomatoriaVendaMediaFinal(new BigDecimal(70));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		// TODO: checar se "É possível que o %Excedente seja igual a 0 (zero)?
		// Se sim como agir nessa situação, qual será o valor da menorVenda?"
		assertEquals(new BigDecimal(0.25), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia50() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setSomatoriaVendaMediaFinal(new BigDecimal(50));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte100ESomatoriaVendaMedia90() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setSomatoriaVendaMediaFinal(new BigDecimal(90));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		assertEquals(new BigDecimal(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaComReparte10ESomatoriaVendaMedia50() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(10));
		estudo.setSomatoriaVendaMediaFinal(new BigDecimal(50));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		// TODO: verificar se é possível que a somatória de vendas seja maior que o reparte
		// a distribuir? E caso isso aconteça, qual será o valor da menorVenda?
		assertEquals(new BigDecimal(0.5), redutorAutomatico.getMenorVenda());
	}
	
	@Test
	public void testMenorVendaSemReparteESomatoriaVendaMedia50() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setSomatoriaVendaMediaFinal(new BigDecimal(50));
		
		// Execução do método
		RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
		redutorAutomatico.calcularMenorVenda();
		
		// Validação do teste
		// TODO: verificar se é possível que a somatória de vendas seja maior que o reparte
		// a distribuir? E caso isso aconteça, qual será o valor da menorVenda?
		assertEquals(new BigDecimal(0.5), redutorAutomatico.getMenorVenda());
	}
}
