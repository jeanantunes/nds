package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.ClassificacaoCota;
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
	cota.setClassificacao(ClassificacaoCota.BancaSoComEdicaoBaseAberta);

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
	    fail("Exceção inválida.");
	}
    }

    @Test
    public void testExecutarParcialFalse() {

	try {
	    ProdutoEdicao p1 = new ProdutoEdicao(), p2 = new ProdutoEdicao();
	    p1.setId(133786l);
	    p2.setId(133786l);

	    List<ProdutoEdicao> l = new ArrayList<ProdutoEdicao>();
	    l.add(p1);
	    l.add(p2);

	    estudo.getCotas().get(0).setEdicoesRecebidas(l);
	    this.estudo.getProduto().setParcial(Boolean.TRUE);
	    gravarReparteJuramentado.executarProcesso();
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exceção inválida.");
	}
    }

    @Test
    public void testExecutarFimProcesso() {

	try {
	    this.estudo.setReparteDistribuir(new BigDecimal(10));
	    gravarReparteJuramentado.executarProcesso();
	    gravarReparteJuramentado.fimProcesso();
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exceção inválida.");
	}
    }
}
