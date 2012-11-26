package br.com.abril.nds.model.estoque;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public class DiferencaTest {

    private ProdutoEdicao edicao;

    @Before
    public void setUp() {
        Produto produto = new Produto();
        produto.setCodigo("123");
        produto.setNome("Veja");
        produto.setNomeComercial("Veja");

        edicao = new ProdutoEdicao();
        edicao.setProduto(produto);
        edicao.setPrecoVenda(BigDecimal.valueOf(11.25));
        edicao.setPacotePadrao(10);
    }

    @Test
    public void testValorTotalSobraDe() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(2));
        diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_DE);

        Assert.assertEquals(BigDecimal.valueOf(225).setScale(2), diferenca.getValorTotal());
    }

    @Test
    public void testValorTotalSobraEm() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(2));
        diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_EM);

        Assert.assertEquals(BigDecimal.valueOf(22.5).setScale(2), diferenca.getValorTotal());
    }

    @Test
    public void testValorTotalFaltaDe() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(3));
        diferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE);

        Assert.assertEquals(BigDecimal.valueOf(337.5).setScale(2), diferenca.getValorTotal());
    }

    @Test
    public void testValorTotalFaltaEm() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(3));
        diferenca.setTipoDiferenca(TipoDiferenca.FALTA_EM);

        Assert.assertEquals(BigDecimal.valueOf(33.75).setScale(2), diferenca.getValorTotal());
    }
    
    @Test
    public void testQtdeExemplaresSobraDe() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(2));
        diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_DE);

        Assert.assertEquals(BigInteger.valueOf(20), diferenca.getQtdeExemplares());  
    }
    
    @Test
    public void testQtdeExemplaresSobraEm() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(2));
        diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_EM);

        Assert.assertEquals(BigInteger.valueOf(2), diferenca.getQtdeExemplares());  
    }
    
    @Test
    public void testQtdeExemplaresFaltaDe() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(2));
        diferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE);

        Assert.assertEquals(BigInteger.valueOf(20), diferenca.getQtdeExemplares());  
    }
    
    @Test
    public void testQtdeExemplaresFaltaEm() {
        Diferenca diferenca = new Diferenca();
        diferenca.setProdutoEdicao(edicao);
        diferenca.setQtde(BigInteger.valueOf(2));
        diferenca.setTipoDiferenca(TipoDiferenca.FALTA_EM);

        Assert.assertEquals(BigInteger.valueOf(2), diferenca.getQtdeExemplares());  
    }

}
