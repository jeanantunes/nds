package br.com.abril.nds.process.complementarautomatico;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.service.EstudoService;

public class ComplementarAutomaticoTest {

	@Test
	public void testSemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(20));
		estudo.setComplementarAutomatico(true);
		
		ProdutoEdicao edicao = new ProdutoEdicao();
		estudo.getEdicoesBase().add(edicao);
		
		Cota cota = new Cota();
		estudo.getCotas().add(cota);

		// Execução do processo
		ComplementarAutomatico complementarAutomatico = new ComplementarAutomatico(estudo);
		complementarAutomatico.executarProcesso();
		
		// Validação do teste
		assertEquals(new BigDecimal(20), complementarAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : complementarAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	@Ignore
	public void testConfiguracao() {
		// Criação do ambiente
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(new BigDecimal(100));
		estudo.setComplementarAutomatico(true);
	
		ProdutoEdicao produto = new ProdutoEdicao();
		produto.setColecao(true);
		estudo.setProduto(produto);
		
		ProdutoEdicao edicao = new ProdutoEdicao();
		estudo.getEdicoesBase().add(edicao);
		
		Cota cota = new Cota();
		cota.setVendaMedia(new BigDecimal(20));
		estudo.getCotas().add(cota);
		EstudoService.calculate(estudo);

		// Execução do processo
		ComplementarAutomatico complementarAutomatico = new ComplementarAutomatico(estudo);
		complementarAutomatico.executarProcesso();

		// Validação do teste
		assertEquals(new BigDecimal(20), complementarAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : complementarAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
}
