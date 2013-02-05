package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.HistoricoEstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;

public class ResumoSuplementarFecharDiaRepositoryImplTest extends AbstractDataUtilRepositoryImplTest {

    @Autowired
    private ResumoSuplementarFecharDiaRepository repository;
    


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

        criarHistoricoEstoqueProduto();

        criarRecebimentoFisico();

        criarDiferencas();

        criarTransferencias();
        
        criarVendasSuplementar();      
    }
    
    @Test
    public void testObterDadosGridSuplementar() {
        List<SuplementarFecharDiaDTO> resultado = repository.obterDadosGridSuplementar(distribuidor.getDataOperacao(), null);
        Assert.assertNotNull(resultado);
  
        Assert.assertEquals(6, resultado.size());
        
        SuplementarFecharDiaDTO suplementarVeja1 = resultado.get(0);
        Assert.assertNotNull(suplementarVeja1);
        
        Assert.assertEquals(produtoVeja.getCodigo(), suplementarVeja1.getCodigo());
        Assert.assertEquals(produtoVeja.getNome(), suplementarVeja1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoVeja1.getNumeroEdicao(), suplementarVeja1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoVeja1.getPrecoVenda().setScale(2), suplementarVeja1.getPrecoVenda().setScale(2));
        Assert.assertEquals(BigInteger.valueOf(0), suplementarVeja1.getQuantidadeContabil());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarVeja1.getQuantidadeLogico());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarVeja1.getQuantidadeVenda());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarVeja1.getQuantidadeTransferenciaEntrada());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarVeja1.getQuantidadeTransferenciaSaida());
        
        SuplementarFecharDiaDTO suplementarPlacar1 = resultado.get(1);
        Assert.assertNotNull(suplementarPlacar1);
        
        Assert.assertEquals(produtoPlacar.getCodigo(), suplementarPlacar1.getCodigo());
        Assert.assertEquals(produtoPlacar.getNome(), suplementarPlacar1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoPlacar1.getNumeroEdicao(), suplementarPlacar1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoPlacar1.getPrecoVenda().setScale(2), suplementarPlacar1.getPrecoVenda().setScale(2));
        Assert.assertEquals(BigInteger.valueOf(0), suplementarPlacar1.getQuantidadeContabil());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarPlacar1.getQuantidadeLogico());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarPlacar1.getQuantidadeVenda());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarPlacar1.getQuantidadeTransferenciaEntrada());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarPlacar1.getQuantidadeTransferenciaSaida());
        
        SuplementarFecharDiaDTO suplementarQuatroRodas1 = resultado.get(2);
        Assert.assertNotNull(suplementarQuatroRodas1);
        
        Assert.assertEquals(produtoQuatroRodas.getCodigo(), suplementarQuatroRodas1.getCodigo());
        Assert.assertEquals(produtoQuatroRodas.getNome(), suplementarQuatroRodas1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getNumeroEdicao(), suplementarQuatroRodas1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getPrecoVenda().setScale(2), suplementarQuatroRodas1.getPrecoVenda().setScale(2));
        Assert.assertEquals(BigInteger.valueOf(5), suplementarQuatroRodas1.getQuantidadeContabil());
        Assert.assertEquals(BigInteger.valueOf(5), suplementarQuatroRodas1.getQuantidadeLogico());
        Assert.assertEquals(BigInteger.valueOf(3), suplementarQuatroRodas1.getQuantidadeVenda());
        Assert.assertEquals(BigInteger.valueOf(5), suplementarQuatroRodas1.getQuantidadeTransferenciaEntrada());   
        Assert.assertEquals(BigInteger.valueOf(2), suplementarQuatroRodas1.getQuantidadeTransferenciaSaida());
        
        SuplementarFecharDiaDTO suplementarCapricho1 = resultado.get(3);
        Assert.assertNotNull(suplementarCapricho1);
        
        Assert.assertEquals(produtoCapricho.getCodigo(), suplementarCapricho1.getCodigo());
        Assert.assertEquals(produtoCapricho.getNome(), suplementarCapricho1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoCapricho1.getNumeroEdicao(), suplementarCapricho1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoCapricho1.getPrecoVenda().setScale(2), suplementarCapricho1.getPrecoVenda().setScale(2));
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCapricho1.getQuantidadeContabil());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCapricho1.getQuantidadeLogico());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCapricho1.getQuantidadeVenda());   
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCapricho1.getQuantidadeTransferenciaEntrada());  
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCapricho1.getQuantidadeTransferenciaSaida());
        
        SuplementarFecharDiaDTO suplementarSuperInteressante1 = resultado.get(4);
        Assert.assertNotNull(suplementarSuperInteressante1);
        
        Assert.assertEquals(produtoSuper.getCodigo(), suplementarSuperInteressante1.getCodigo());
        Assert.assertEquals(produtoSuper.getNome(), suplementarSuperInteressante1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoSuper1.getNumeroEdicao(), suplementarSuperInteressante1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoSuper1.getPrecoVenda().setScale(2), suplementarSuperInteressante1.getPrecoVenda().setScale(2));
        Assert.assertEquals(BigInteger.valueOf(3), suplementarSuperInteressante1.getQuantidadeContabil());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarSuperInteressante1.getQuantidadeLogico());
        Assert.assertEquals(BigInteger.valueOf(2), suplementarSuperInteressante1.getQuantidadeVenda());    
        Assert.assertEquals(BigInteger.valueOf(5), suplementarSuperInteressante1.getQuantidadeTransferenciaEntrada());  
        Assert.assertEquals(BigInteger.valueOf(0), suplementarSuperInteressante1.getQuantidadeTransferenciaSaida());
        
        SuplementarFecharDiaDTO suplementarCaras1 = resultado.get(5);
        Assert.assertNotNull(suplementarCaras1);
        
        Assert.assertEquals(produtoCaras.getCodigo(), suplementarCaras1.getCodigo());
        Assert.assertEquals(produtoCaras.getNome(), suplementarCaras1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoCaras1.getNumeroEdicao(), suplementarCaras1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoCaras1.getPrecoVenda().setScale(2), suplementarCaras1.getPrecoVenda().setScale(2));
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCaras1.getQuantidadeContabil());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCaras1.getQuantidadeLogico());
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCaras1.getQuantidadeVenda());   
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCaras1.getQuantidadeTransferenciaEntrada());     
        Assert.assertEquals(BigInteger.valueOf(0), suplementarCaras1.getQuantidadeTransferenciaSaida());
    }
    
    
    @Override
    protected void criarTransferencias() {
        super.criarTransferencias();

        MovimentoEstoque movimentoSaidaLancamentoSuper1 = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tmTransferenciaSaidaLancamento, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(5), StatusAprovacao.APROVADO, "OK");
        
        MovimentoEstoque movimentoEntradaSuplementarSuper1 = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tmTransferenciaEntradaSuplementar, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(5), StatusAprovacao.APROVADO, "OK");
        save(movimentoSaidaLancamentoSuper1, movimentoEntradaSuplementarSuper1);
        
        estoqueProdutoSuper1.setQtde(estoqueProdutoSuper1.getQtde().subtract(BigInteger.valueOf(5)));
        estoqueProdutoSuper1.setQtdeSuplementar(Util.nvl(
                estoqueProdutoSuper1.getQtdeSuplementar(), BigInteger.ZERO)
                .add(BigInteger.valueOf(5)));
        save(estoqueProdutoSuper1);
    }

    private void criarHistoricoEstoqueProduto() {
        historicoQuatroRodas1 = new HistoricoEstoqueProduto();
        historicoQuatroRodas1.setData(DateUtil.adicionarDias(distribuidor.getDataOperacao(), -1));
        historicoQuatroRodas1.setProdutoEdicao(produtoEdicaoQuatroRodas1);
        historicoQuatroRodas1.setQtde(BigInteger.ZERO);
        historicoQuatroRodas1.setQtdeSuplementar(BigInteger.valueOf(5));
        save(historicoQuatroRodas1);
    }
    
    private void criarVendasSuplementar() {
        TipoMovimentoEstoque tipoMovimentoVendaSuplementar = Fixture.tipoMovimentoVendaEncalheSuplementar();
        save(tipoMovimentoVendaSuplementar);
        
        VendaProduto vendaSuplementarSuper1Manoel = new VendaProduto();
        vendaSuplementarSuper1Manoel.setCota(cotaManoel);
        vendaSuplementarSuper1Manoel.setDataVenda(distribuidor.getDataOperacao());
        vendaSuplementarSuper1Manoel.setHorarioVenda(distribuidor.getDataOperacao());
        vendaSuplementarSuper1Manoel.setProdutoEdicao(produtoEdicaoSuper1);
        vendaSuplementarSuper1Manoel.setQntProduto(BigInteger.valueOf(2));
        vendaSuplementarSuper1Manoel.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
        vendaSuplementarSuper1Manoel.setTipoVenda(TipoVendaEncalhe.SUPLEMENTAR);
        vendaSuplementarSuper1Manoel.setUsuario(usuarioJoao);
        vendaSuplementarSuper1Manoel.setValorTotalVenda(BigDecimal.valueOf(39));
        save(vendaSuplementarSuper1Manoel);
        
        estoqueProdutoSuper1.setQtdeSuplementar(estoqueProdutoSuper1
                .getQtdeSuplementar().subtract(BigInteger.valueOf(2)));
        save(estoqueProdutoSuper1);
        
        MovimentoEstoque movimentoVendaSuplementarSuper1Manoel = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tipoMovimentoVendaSuplementar, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(2), StatusAprovacao.APROVADO, "OK");
        save(movimentoVendaSuplementarSuper1Manoel);
        
        VendaProduto vendaSuplementarQuatroRodas1Manoel = new VendaProduto();
        vendaSuplementarQuatroRodas1Manoel.setCota(cotaManoel);
        vendaSuplementarQuatroRodas1Manoel.setDataVenda(distribuidor.getDataOperacao());
        vendaSuplementarQuatroRodas1Manoel.setHorarioVenda(distribuidor.getDataOperacao());
        vendaSuplementarQuatroRodas1Manoel.setProdutoEdicao(produtoEdicaoQuatroRodas1);
        vendaSuplementarQuatroRodas1Manoel.setQntProduto(BigInteger.valueOf(3));
        vendaSuplementarQuatroRodas1Manoel.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
        vendaSuplementarQuatroRodas1Manoel.setTipoVenda(TipoVendaEncalhe.SUPLEMENTAR);
        vendaSuplementarQuatroRodas1Manoel.setUsuario(usuarioJoao);
        vendaSuplementarQuatroRodas1Manoel.setValorTotalVenda(BigDecimal.valueOf(54));
        save(vendaSuplementarQuatroRodas1Manoel);
        
        estoqueProdutoQuatroRodas1.setQtdeSuplementar(estoqueProdutoQuatroRodas1
                .getQtdeSuplementar().subtract(BigInteger.valueOf(3)));
        save(estoqueProdutoQuatroRodas1);
        
        MovimentoEstoque movimentoVendaSuplementarQuatroRodas1Manoel = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tipoMovimentoVendaSuplementar, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(3), StatusAprovacao.APROVADO, "OK");
        save(movimentoVendaSuplementarQuatroRodas1Manoel);
    }
 
}
