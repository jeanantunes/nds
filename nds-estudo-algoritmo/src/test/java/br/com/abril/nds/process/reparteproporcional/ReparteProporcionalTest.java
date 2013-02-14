package br.com.abril.nds.process.reparteproporcional;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

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
		estudo.calculate();
		estudo.getCotas().add(cota);
		
		// Execução do Processo
		ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
		reparteProporcional.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(100), reparteProporcional.getEstudo().getReparteDistribuir());
		for (Cota c : reparteProporcional.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(0), c.getReparteCalculado());
		}
	}
}
