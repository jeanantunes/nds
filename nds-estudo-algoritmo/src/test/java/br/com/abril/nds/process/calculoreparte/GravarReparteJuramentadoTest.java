package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public class GravarReparteJuramentadoTest {

    Estudo estudo = null;

    @Autowired
    private GravarReparteJuramentado gravarReparteJuramentado;

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

	gravarReparteJuramentado.setEstudo(estudo);
    }

    @Test
    public void testExecutarProcesso() {

	try {
	    gravarReparteJuramentado.executarProcesso();
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exce��o inv�lida.");
	}

    }

    @Test
    public void testExecutarParcialFalse() {

	try {
	    this.estudo.getProduto().setParcial(Boolean.TRUE);
	    gravarReparteJuramentado.executarProcesso();
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exce��o inv�lida.");
	}

    }

    @Test
    public void testExecutarFimProcesso() {

	try {
	    gravarReparteJuramentado.executarProcesso();
	    gravarReparteJuramentado.fimProcesso();
	} catch (Exception e) {
	    fail("Exce��o inv�lida.");
	}

    }

}
