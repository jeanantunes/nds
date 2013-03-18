package br.com.abril.nds.process.complementarautomatico;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.service.EstudoServiceEstudo;

public class ComplementarAutomaticoTest {

	private Estudo criarAmbiente(BigDecimal reparteDistribuir, boolean complementarAutomatico, boolean produtoColecao,
			BigDecimal vendaMedia) {
		Estudo estudo = new Estudo();
		estudo.setReparteDistribuir(reparteDistribuir);
		estudo.setComplementarAutomatico(complementarAutomatico);
		ProdutoEdicao edicao = new ProdutoEdicao();
		if (produtoColecao) {
			edicao.setColecao(true);
			estudo.setProduto(edicao);
		}
		estudo.setEdicoesBase(new ArrayList<ProdutoEdicaoBase>());
		estudo.getEdicoesBase().add(edicao);
		Cota cota = new Cota();
		cota.setVendaMedia(vendaMedia);
		estudo.setCotas(new ArrayList<Cota>());
		estudo.getCotas().add(cota);
		EstudoServiceEstudo.calculate(estudo);
		
		return estudo;
	}
	
	@Test
	public void testSemConfiguracao() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(BigDecimal.valueOf(20), true, false, BigDecimal.ZERO);

		// Execução do processo
		ComplementarAutomatico complementarAutomatico = new ComplementarAutomatico(estudo);
		complementarAutomatico.executarProcesso();
		
		// Validação do teste
		assertEquals(BigDecimal.valueOf(20), complementarAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : complementarAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
	
	@Test
	public void testConfiguracao() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), true, true, BigDecimal.valueOf(20));
		
		// Execução do processo
		ComplementarAutomatico complementarAutomatico = new ComplementarAutomatico(estudo);
		complementarAutomatico.executarProcesso();

		// Validação do teste
		assertEquals(BigDecimal.valueOf(98), complementarAutomatico.getEstudo().getReparteDistribuir());
		for (Cota c : complementarAutomatico.getEstudo().getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}
}
