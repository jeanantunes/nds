package br.com.abril.nds.process.reparteproporcional;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.service.EstudoService;

public class ReparteProporcionalTest {

	@Test
	public void testSemEdicaoBaseAberta() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setReparteDistribuirInicial(new BigDecimal(100));
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setEdicaoAberta(false);
		estudo.getEdicoesBase().add(edicao);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(100), reparteProporcional.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testComEdicaoBaseAberta() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setReparteDistribuirInicial(new BigDecimal(100));
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setEdicaoAberta(true);
		estudo.getEdicoesBase().add(edicao);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(100), reparteProporcional.getEstudo().getReparteDistribuir());
	}
	
	@Test
	public void testComEdicaoBaseAbertaECotaRecebeuEdicaoAberta() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setReparteDistribuirInicial(new BigDecimal(100));
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setEdicaoAberta(true);
		edicao.setReparte(new BigDecimal(20));
		estudo.getEdicoesBase().add(edicao);
		Cota cota = new Cota();
		cota.getEdicoesRecebidas().add(edicao);
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
		for (Cota c : reparteProporcional.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(100), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testComEdicaoBaseAbertaECotaRecebeuEdicaoAbertaCom2Cotas() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setReparteDistribuirInicial(new BigDecimal(100));
		
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setEdicaoAberta(true);
		edicao.setReparte(new BigDecimal(20));
		estudo.getEdicoesBase().add(edicao);
		
		Cota cota = new Cota();
		cota.getEdicoesRecebidas().add(edicao);
		estudo.getCotas().add(cota);
		
		cota = new Cota();
		cota.getEdicoesRecebidas().add(edicao);
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
		for (Cota c : reparteProporcional.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(50), c.getReparteCalculado());
		}
	}
	
	@Test
	public void testComEdicaoBaseAbertaECotaRecebeuEdicaoAbertaCom2CotasDiferentes() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setReparteDistribuirInicial(new BigDecimal(100));
		
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setEdicaoAberta(true);
		edicao.setReparte(new BigDecimal(20));
		estudo.getEdicoesBase().add(edicao);
		
		ProdutoEdicao edicao2 = new ProdutoEdicao();
		edicao2.setEdicaoAberta(true);
		edicao2.setReparte(new BigDecimal(30));
		estudo.getEdicoesBase().add(edicao2);
		
		Cota cota = new Cota();
		cota.setId(new Long(1));
		cota.getEdicoesRecebidas().add(edicao2);
		estudo.getCotas().add(cota);
		
		cota = new Cota();
		cota.setId(new Long(2));
		cota.getEdicoesRecebidas().add(edicao);
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
		for (Cota c : reparteProporcional.getEstudo().getCotas()) {
			if (c.getId().equals(new Long(1))) {
				assertEquals(new BigDecimal(60), c.getReparteCalculado());
			} else if (c.getId().equals(new Long(2))) {
				assertEquals(new BigDecimal(40), c.getReparteCalculado());
			}			
		}
	}
	
	@Test
	public void testCom1EdicaoBaseAbertaE1Fechada() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(200));
		estudo.setReparteDistribuirInicial(new BigDecimal(200));
		
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setEdicaoAberta(false);
		edicao.setReparte(new BigDecimal(20));
		estudo.getEdicoesBase().add(edicao);
		
		Cota cota = new Cota();
		cota.setId(new Long(1));
		cota.getEdicoesRecebidas().add(edicao);
		estudo.getCotas().add(cota);
		
		ProdutoEdicao edicao2 = new ProdutoEdicao();
		edicao2.setEdicaoAberta(true);
		edicao2.setReparte(new BigDecimal(30));
		estudo.getEdicoesBase().add(edicao2);
		
		Cota cota2 = new Cota();
		cota2.setId(new Long(2));
		cota2.getEdicoesRecebidas().add(edicao2);
		estudo.getCotas().add(cota2);
		EstudoService.calculate(estudo);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
		for (Cota c : reparteProporcional.getEstudo().getCotas()) {
			if (c.getId().equals(new Long(1))) {
				assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
			} else if (c.getId().equals(new Long(2))) {
				assertEquals(new BigDecimal(200), c.getReparteCalculado());
			}			
		}
	}
}
