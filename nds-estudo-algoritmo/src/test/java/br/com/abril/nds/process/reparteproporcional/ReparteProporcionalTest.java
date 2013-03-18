package br.com.abril.nds.process.reparteproporcional;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.service.EstudoServiceEstudo;

public class ReparteProporcionalTest {

    @Autowired
    private ReparteProporcional reparteProporcional;
    
    private Estudo criarAmbiente(BigDecimal reparteDistribuir, BigDecimal reparte1, boolean edicaoAberta1, boolean replicaEdicaoPara2Cota,
	    BigDecimal reparte2, boolean edicaoAberta2) {
	Estudo estudo = new Estudo();
	estudo.setReparteDistribuir(reparteDistribuir);
	estudo.setReparteDistribuirInicial(reparteDistribuir);

	ProdutoEdicao edicao1 = new ProdutoEdicao();
	edicao1.setEdicaoAberta(edicaoAberta1);
	edicao1.setReparte(reparte1);
	estudo.setEdicoesBase(new LinkedList<ProdutoEdicaoBase>());
	estudo.getEdicoesBase().add(edicao1);
	Cota cota = new Cota();
	cota.setId(new Long(1));
	cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicao>());
	cota.getEdicoesRecebidas().add(edicao1);
	estudo.setCotas(new ArrayList<Cota>());
	estudo.getCotas().add(cota);

	if (replicaEdicaoPara2Cota) {
	    cota = new Cota();
	    cota.setId(new Long(2));
	    cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicao>());
	    cota.getEdicoesRecebidas().add(edicao1);
	    estudo.getCotas().add(cota);
	}

	if (reparte2 != null) {
	    ProdutoEdicao edicao2 = new ProdutoEdicao();
	    edicao2.setEdicaoAberta(edicaoAberta2);
	    edicao2.setReparte(reparte2);
	    estudo.getEdicoesBase().add(edicao2);
	    cota = new Cota();
	    cota.setId(new Long(2));
	    cota.getEdicoesRecebidas().add(edicao2);
	    estudo.getCotas().add(cota);
	}

	EstudoServiceEstudo.calculate(estudo);
	return estudo;
    }

    @Test
    public void testSemEdicaoBaseAberta() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), BigDecimal.ZERO, false, false, null, false);

	// Execução do Processo
	reparteProporcional.setEstudo(estudo);
	reparteProporcional.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(100), reparteProporcional.getEstudo().getReparteDistribuir());
    }

    @Test
    public void testComEdicaoBaseAberta() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), BigDecimal.ZERO, true, false, null, false);

	// Execução do Processo
	reparteProporcional.setEstudo(estudo);
	reparteProporcional.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(100), reparteProporcional.getEstudo().getReparteDistribuir());
    }

    @Test
    public void testComEdicaoBaseAbertaECotaRecebeuEdicaoAberta() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), BigDecimal.valueOf(20), true, true, null, false);

	// Execução do Processo
	reparteProporcional.setEstudo(estudo);
	reparteProporcional.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
	for (Cota c : reparteProporcional.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(100), c.getReparteCalculado());
	}
    }

    @Test
    public void testComEdicaoBaseAbertaECotaRecebeuEdicaoAbertaCom2Cotas() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), BigDecimal.valueOf(20), true, true, null, false);

	// Execução do Processo
	reparteProporcional.setEstudo(estudo);
	reparteProporcional.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
	for (Cota c : reparteProporcional.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.valueOf(50), c.getReparteCalculado());
	}
    }

    @Test
    public void testComEdicaoBaseAbertaECotaRecebeuEdicaoAbertaCom2CotasDiferentes() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), BigDecimal.valueOf(20), true, false, BigDecimal.valueOf(30), true);

	// Execução do Processo
	reparteProporcional.setEstudo(estudo);
	reparteProporcional.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
	for (Cota c : reparteProporcional.getEstudo().getCotas()) {
	    if (c.getId().equals(new Long(1))) {
		assertEquals(BigDecimal.valueOf(60), c.getReparteCalculado());
	    } else if (c.getId().equals(new Long(2))) {
		assertEquals(BigDecimal.valueOf(40), c.getReparteCalculado());
	    }
	}
    }

    @Test
    public void testCom1EdicaoBaseAbertaE1Fechada() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(200), BigDecimal.valueOf(20), false, false, BigDecimal.valueOf(30), true);

	// Execução do Processo
	reparteProporcional.setEstudo(estudo);
	reparteProporcional.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.ZERO, reparteProporcional.getEstudo().getReparteDistribuir());
	for (Cota c : reparteProporcional.getEstudo().getCotas()) {
	    if (c.getId().equals(new Long(1))) {
		assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
	    } else if (c.getId().equals(new Long(2))) {
		assertEquals(BigDecimal.valueOf(200), c.getReparteCalculado());
	    }
	}
    }
}
