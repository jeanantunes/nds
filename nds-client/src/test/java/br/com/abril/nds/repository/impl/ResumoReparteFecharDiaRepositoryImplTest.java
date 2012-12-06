package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class ResumoReparteFecharDiaRepositoryImplTest extends AbstractDataUtilRepositoryImplTest {

    @Autowired
    private ResumoReparteFecharDiaRepository repository;

    @Before
    public void setUp() {
        criarUsuarios();
        
        criarDistribuidor();

        criarCotas();

        criarFornecedores();

        criarProdutos();

        criarLancamentos();

        criarParametrosNotaFiscal();

        criarNotaFiscalEntradaFornecedor();

        criarRecebimentoFisico();

        criarDiferencas();

        criarTransferencias();
        
        criarEstudos();

        criarExpedicao();
        
        criarFuroProduto();
    }


    @Test
    public void testObterResumoReparte() {
        List<ReparteFecharDiaDTO> resultado = repository.obterResumoReparte(distribuidor.getDataOperacao());
        Assert.assertNotNull(resultado);
        Assert.assertEquals(3, resultado.size());
        
        ReparteFecharDiaDTO reparteVeja = resultado.get(0);
        Assert.assertEquals(produtoVeja.getCodigo(), reparteVeja.getCodigo());
        Assert.assertEquals(produtoVeja.getNome(), reparteVeja.getNomeProduto());
        Assert.assertEquals(produtoEdicaoVeja1.getNumeroEdicao(), reparteVeja.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoVeja1.getPrecoVenda().setScale(2), reparteVeja.getPrecoVenda());
        Assert.assertEquals(BigInteger.valueOf(100), reparteVeja.getQtdeReparte());
        Assert.assertEquals(BigInteger.ZERO, reparteVeja.getQtdeSobraEm());
        Assert.assertEquals(BigInteger.ZERO, reparteVeja.getQtdeFaltaEm());
        Assert.assertEquals(BigInteger.valueOf(10), reparteVeja.getQtdeFaltaDe());
        Assert.assertEquals(BigInteger.ZERO, reparteVeja.getQtdeSobraDe());
        Assert.assertEquals(BigInteger.valueOf(90), reparteVeja.getQtdeDistribuido());
        Assert.assertEquals(BigInteger.ZERO, reparteVeja.getQtdeTransferencia());
        Assert.assertEquals(BigInteger.ZERO, reparteVeja.getQtdeSobra());
        Assert.assertEquals(BigInteger.valueOf(10), reparteVeja.getQtdeFalta());
        Assert.assertEquals(BigInteger.valueOf(90), reparteVeja.getQtdeDistribuir());
        Assert.assertEquals(BigInteger.ZERO, reparteVeja.getQtdeSobraDistribuicao());
        Assert.assertEquals(BigInteger.valueOf(90), reparteVeja.getQtdeDiferenca());
        
        ReparteFecharDiaDTO reparteQuatroRodas = resultado.get(1);
        Assert.assertEquals(produtoQuatroRodas.getCodigo(), reparteQuatroRodas.getCodigo());
        Assert.assertEquals(produtoQuatroRodas.getNome(), reparteQuatroRodas.getNomeProduto());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getNumeroEdicao(), reparteQuatroRodas.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getPrecoVenda().setScale(2), reparteQuatroRodas.getPrecoVenda());
        Assert.assertEquals(BigInteger.valueOf(50), reparteQuatroRodas.getQtdeReparte());
        Assert.assertEquals(BigInteger.ZERO, reparteQuatroRodas.getQtdeSobraEm());
        Assert.assertEquals(BigInteger.valueOf(2), reparteQuatroRodas.getQtdeFaltaEm());
        Assert.assertEquals(BigInteger.ZERO, reparteQuatroRodas.getQtdeFaltaDe());
        Assert.assertEquals(BigInteger.ZERO, reparteQuatroRodas.getQtdeSobraDe());
        Assert.assertEquals(BigInteger.valueOf(2), reparteQuatroRodas.getQtdeFalta());
        Assert.assertEquals(BigInteger.ZERO, reparteQuatroRodas.getQtdeSobra());
        Assert.assertEquals(BigInteger.valueOf(-3), reparteQuatroRodas.getQtdeTransferencia());
        Assert.assertEquals(BigInteger.valueOf(45), reparteQuatroRodas.getQtdeDistribuir());
        Assert.assertEquals(BigInteger.valueOf(43), reparteQuatroRodas.getQtdeDistribuido());
        Assert.assertEquals(BigInteger.valueOf(2), reparteQuatroRodas.getQtdeSobraDistribuicao());
        Assert.assertEquals(BigInteger.valueOf(41), reparteQuatroRodas.getQtdeDiferenca());

        ReparteFecharDiaDTO reparteSuper = resultado.get(2);
        Assert.assertEquals(produtoSuper.getCodigo(), reparteSuper.getCodigo());
        Assert.assertEquals(produtoSuper.getNome(), reparteSuper.getNomeProduto());
        Assert.assertEquals(produtoEdicaoSuper1.getNumeroEdicao(), reparteSuper.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoSuper1.getPrecoVenda().setScale(2), reparteSuper.getPrecoVenda());
        Assert.assertEquals(BigInteger.valueOf(50), reparteSuper.getQtdeReparte());
        Assert.assertEquals(BigInteger.ZERO, reparteSuper.getQtdeSobraEm());
        Assert.assertEquals(BigInteger.ZERO, reparteSuper.getQtdeFaltaEm());
        Assert.assertEquals(BigInteger.ZERO, reparteSuper.getQtdeFaltaDe());
        Assert.assertEquals(BigInteger.valueOf(10), reparteSuper.getQtdeSobraDe());
        Assert.assertEquals(BigInteger.ZERO, reparteSuper.getQtdeFalta());
        Assert.assertEquals(BigInteger.valueOf(10), reparteSuper.getQtdeSobra());
        Assert.assertEquals(BigInteger.ZERO, reparteSuper.getQtdeTransferencia());
        Assert.assertEquals(BigInteger.valueOf(60), reparteSuper.getQtdeDistribuir());
        Assert.assertEquals(BigInteger.valueOf(50), reparteSuper.getQtdeDistribuido());
        Assert.assertEquals(BigInteger.valueOf(10), reparteSuper.getQtdeSobraDistribuicao());
        Assert.assertEquals(BigInteger.valueOf(40), reparteSuper.getQtdeDiferenca());
    }
    
    @Test
    public void testObterResumoRepartePaginado() {
        PaginacaoVO paginacao = new PaginacaoVO(1, 2, null);
        List<ReparteFecharDiaDTO> resultado = repository.obterResumoReparte(distribuidor.getDataOperacao(), paginacao);
        Assert.assertEquals(2, resultado.size());
        
        ReparteFecharDiaDTO reparteVeja = resultado.get(0);
        Assert.assertEquals(produtoVeja.getNome(), reparteVeja.getNomeProduto());
        
        ReparteFecharDiaDTO reparteQuatroRodas = resultado.get(1);
        Assert.assertEquals(produtoQuatroRodas.getNome(), reparteQuatroRodas.getNomeProduto());
        
        paginacao = new PaginacaoVO(2, 2, null);
        resultado = repository.obterResumoReparte(distribuidor.getDataOperacao(), paginacao);
        Assert.assertEquals(1, resultado.size()); 
        
        ReparteFecharDiaDTO reparteSuper = resultado.get(0);
        Assert.assertEquals(produtoSuper.getNome(), reparteSuper.getNomeProduto());
    }
    
    
    @Test
    public void testContarLancamentosExpedidos() {
        Long resultado = repository.contarLancamentosExpedidos(distribuidor.getDataOperacao());
        Assert.assertEquals(Long.valueOf(3), resultado);
    }
    
    @Test
    public void testContarLancamentosExpedidosZero() {
        Long resultado = repository.contarLancamentosExpedidos(DateUtil.adicionarDias(distribuidor.getDataOperacao(), 1));
        Assert.assertEquals(Long.valueOf(0), resultado);
    }
    
    @Test
    public void testObterSumarioReparte() {
        SumarizacaoReparteDTO sumario = repository.obterSumarizacaoReparte(distribuidor.getDataOperacao());
        Assert.assertEquals(BigDecimal.valueOf(4125).setScale(2), sumario.getTotalReparte().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(195).setScale(2), sumario.getTotalSobras().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(261).setScale(2), sumario.getTotalFaltas().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(-54).setScale(2), sumario.getTotalTransferencias().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(3774).setScale(2), sumario.getTotalDistribuido().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(4005).setScale(2), sumario.getTotalDistribuir().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(231).setScale(2), sumario.getTotalSobraDistribuicao().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(3543).setScale(2), sumario.getTotalDiferenca().setScale(2));
        
    }

}
