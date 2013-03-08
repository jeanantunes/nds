package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public class AjusteFinalReparteTest {

    Estudo estudo = null;

    @Autowired
    private AjusteFinalReparte ajusteFinalReparte;

    @Before
    public void setUp() throws Exception {
	estudo = new Estudo();
	estudo.setCotas(new ArrayList<Cota>());

	ProdutoEdicao pe = new ProdutoEdicao();
	pe.setId(133786l);

	Cota cota = new Cota();
	cota.setId(212l);

	estudo.getCotas().add(cota);
	estudo.setProduto(pe);

	estudo.setAjusteReparte(new BigDecimal(10));
    }

    @Test
    public void testExecutar() {
	try {
	    ajusteFinalReparte.executar();
	} catch (Exception e) {
	    fail("Exceção inválida.");
	}
    }

    @Test
    public void testExecutarReservaAjusteZero() {

	estudo.setAjusteReparte(new BigDecimal(1));
	try {
	    ajusteFinalReparte.executar();
	} catch (Exception e) {
	    fail("Exceção inválida.");
	}

	assertEquals(BigDecimal.ZERO, estudo.getAjusteReparte());
    }

    @Test
    public void testExecutarReparteCalculadoDois() {

	estudo.setAjusteReparte(new BigDecimal(2));
	try {
	    ajusteFinalReparte.executar();
	} catch (Exception e) {
	    fail("Exceção inválida.");
	}

	assertEquals(new BigDecimal(2), estudo.getCotas().get(0).getReparteCalculado());
    }

}
