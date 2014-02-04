package br.com.abril.xrequers.integration.repository.tests.querycompile;

import java.util.Date;

import org.junit.Test;

import br.com.abril.xrequers.integration.repository.tests.ProdutoRepositoryTest;

public class QCProdutoRepository extends ProdutoRepositoryTest {

    @Test
    public void testQCBuscarProdutosBalanceadosOrdenadosNome() {

        this.produtoRepository.buscarProdutosBalanceadosOrdenadosNome(new Date());
    }

    @Test
    public void testQCExisteProdutoRegional() {

        this.produtoRepository.existeProdutoRegional("10");
    }

    @Test
    public void testQCObterDescontoLogistica() {

        this.produtoRepository.obterDescontoLogistica(1L);
    }

    @Test
    public void testQCObterGrupoProduto() {

        this.produtoRepository.obterGrupoProduto("10");
    }

    @Test
    public void testQCObterNomeProdutoPorCodigo() {

        this.produtoRepository.obterNomeProdutoPorCodigo("10");
    }

    @Test
    public void testQCObterProdutoBalanceadosPorCodigo() {

        this.produtoRepository.obterProdutoBalanceadosPorCodigo("10", new Date());
    }

    @Test
    public void testQCObterProdutoLikeCodigo() {

        this.produtoRepository.obterProdutoLikeCodigo("10");
    }

    @Test
    public void testQCObterProdutoLikeNome() {

        this.produtoRepository.obterProdutoLikeNome("VEJA");
    }

    @Test
    public void testQCObterProdutoPorCodigoICD() {

        this.produtoRepository.obterProdutoPorCodigoICD("10");
    }

    @Test
    public void testQCObterProdutoPorCodigoICDLike() {

        this.produtoRepository.obterProdutoPorCodigoICDLike("10");
    }

    @Test
    public void testQCObterProdutoPorCodigoProdin() {

        this.produtoRepository.obterProdutoPorCodigoProdin("10");
    }

    @Test
    public void testQCObterProdutoPorCodigoProdinLike() {

        this.produtoRepository.obterProdutoPorCodigoProdinLike("10");
    }

    @Test
    public void testQCObterProdutoPorID() {

        this.produtoRepository.obterProdutoPorID(1L);
    }

    @Test
    public void testQCObterProdutoPorNome() {

        this.produtoRepository.obterProdutoPorNome("VEJA");
    }

    @Test
    public void testQCObterProdutoPorNomeProdutoOuCodigo() {

        this.produtoRepository.obterProdutoPorNomeProdutoOuCodigo("VEJA", "10");
    }

    @Test
    public void testQCObterUltimoCodigoProdutoRegional() {

        this.produtoRepository.obterUltimoCodigoProdutoRegional();
    }

    @Test
    public void testQCPesquisarCountProdutos() {

        this.produtoRepository.pesquisarCountProdutos("10", "VEJA", "1", "1", 1L, true);
    }

    @Test
    public void testQCPesquisarProdutos() {

        this.produtoRepository.pesquisarProdutos("10", "VEJA", "1", "1", 1L, null, null, 1, 15, true);
    }
}
