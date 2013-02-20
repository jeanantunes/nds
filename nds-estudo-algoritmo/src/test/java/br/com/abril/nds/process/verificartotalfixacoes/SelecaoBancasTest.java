package br.com.abril.nds.process.verificartotalfixacoes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public class SelecaoBancasTest {

    SelecaoBancas selecaoBancas;
    
    @Before
    public void setUp() throws Exception {
	selecaoBancas = new SelecaoBancas(getEstudo());
    }

    private Estudo getEstudo() {
	Estudo estudo = new Estudo();
	estudo.setEdicoesBase(getEdicoesBase());
	return estudo;
    }

    private List<ProdutoEdicao> getEdicoesBase() {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	edicoes.add(getEdicao(133791L));
	edicoes.add(getEdicao(133932L));
	return edicoes;
    }

    private ProdutoEdicao getEdicao(Long id) {
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();
	produtoEdicao.setId(id);
	return produtoEdicao;
    }

    @Test
    public void test() {
	try {
	    selecaoBancas.executar();
	} catch (Exception e) {
	    fail(e.getMessage());
	    e.printStackTrace();
	}
    }
}
