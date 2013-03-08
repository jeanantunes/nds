package br.com.abril.nds.process.verificartotalfixacoes;

import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class SelecaoBancasTest {

    @Autowired
    private SelecaoBancas selecaoBancas;

    @Before
    public void setUp() throws Exception {
	selecaoBancas.setEstudo(getEstudo());
    }

    private Estudo getEstudo() {
	Estudo estudo = new Estudo();
	estudo.setEdicoesBase(getEdicoesBase());
	return estudo;
    }

    private LinkedList<ProdutoEdicaoBase> getEdicoesBase() {
	LinkedList<ProdutoEdicaoBase> edicoes = new LinkedList<ProdutoEdicaoBase>();
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
