package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class GravarReparteFinalCotaTest {

    Estudo estudo = null;

    @Autowired
    private GravarReparteFinalCota gravarReparteFinalCota;

    @Before
    public void setUp() throws Exception {
	estudo = new Estudo();
	estudo.setCotas(new ArrayList<Cota>());
	Cota cota = new Cota();
	estudo.getCotas().add(cota);
	gravarReparteFinalCota.setEstudo(estudo);
    }

    @Test
    public void testExecutar() {
	try {
	    gravarReparteFinalCota.executar();
	} catch (Exception e) {
	    fail("Exceção inválida.");
	}
    }

}
