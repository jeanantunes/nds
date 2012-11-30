package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.BigIntegerUtil;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class ResumoReparteFecharDiaRepositoryImplTest extends AbstractRepositoryImplTest {

    @Autowired
    private ResumoReparteFecharDiaRepository repository;

    private Distribuidor distribuidor;

    private Produto produtoVeja;

    private Produto produtoSuper;

    private Produto produtoQuatroRodas;
    
    private Produto produtoPlacar;

    private ProdutoEdicao produtoEdicaoVeja1;

    private ProdutoEdicao produtoEdicaoSuper1;

    private ProdutoEdicao produtoEdicaoQuatroRodas1;
    
    private ProdutoEdicao produtoEdicaoPlacar1;

    private EstoqueProduto estoqueProdutoVeja1;
    
    private EstoqueProduto estoqueProdutoQuatroRodas1;

    private EstoqueProduto estoqueProdutoSuper1;
    
    private EstoqueProduto estoqueProdutoPlacar1;

    private CFOP cfop5949;

    private CFOP cfop6949;

    private TipoNotaFiscal tipoNotaFiscalRemessaDistribuicao;

    private NotaFiscalEntradaFornecedor notaFiscalFornecedor;

    private ItemNotaFiscalEntrada itemNotaFiscalVeja1;

    private ItemNotaFiscalEntrada itemNotaFiscalSuper1;

    private ItemNotaFiscalEntrada itemNotaFiscalQuatroRodas1;
    
    private ItemNotaFiscalEntrada itemNotaFiscalPlacar1;

    private RecebimentoFisico recebimentoFisico;

    private ItemRecebimentoFisico itemRecebimentoFisicoVeja1;

    private ItemRecebimentoFisico itemRecebimentoFisicoSuper1;

    private ItemRecebimentoFisico itemRecebimentoFisicoQuatroRodas1;
    
    private ItemRecebimentoFisico itemRecebimentoFisicoPlacar1;

    private Lancamento lancamentoVeja1;

    private Lancamento lancamentoSuper1;

    private Lancamento lancamentoQuatroRodas1;
    
    private Lancamento lancamentoPlacar1;

    private Cota cotaManoel;

    private Cota cotaJose;

    private Cota cotaMaria;

    private Usuario usuarioJoao;

    private Fornecedor fornecedorDinap;

    private Diferenca diferencaVeja1;

    private Diferenca diferencaSuper1;

    private Diferenca diferencaQuatroRodas1;

    private EstudoCota ecManoelVeja1;

    private EstudoCota ecJoseVeja1;

    private EstudoCota ecMariaVeja1;

    private EstudoCota ecManoelQuatroRodas1;

    private EstudoCota ecJoseQuatroRodas1;

    private EstudoCota ecMariaQuatroRodas1;

    private EstudoCota ecManoelSuper1;

    private EstudoCota ecJoseSuper1;

    private EstudoCota ecMariaSuper1;
    
    private EstudoCota ecManoelPlacar1;

    private EstudoCota ecJosePlacar1;

    private EstudoCota ecMariaPlacar1;

    private TipoMovimentoEstoque tipoMovimentoEnvioJornaleiro;

    private EstoqueProdutoCota epcManoelPlacar1;
    
    private EstoqueProdutoCota epcJosePlacar1;
    
    private EstoqueProdutoCota epcMariaPlacar1;

    @Before
    public void setUp() {
        usuarioJoao = Fixture.usuarioJoao();
        save(usuarioJoao);

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

    private void criarDistribuidor() {
        PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme", "56.003.315/0001-47", "333333333333", "distrib_acme@mail.com", "99.999-9");
        save(juridicaDistrib);

        distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
        save(distribuidor);
    }

    private void criarCotas() {
        Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
        save(box1);

        PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95", "sys.discover@gmail.com", "Manoel da Silva");
        cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
        save(manoel, cotaManoel);

        PessoaFisica jose = Fixture.pessoaFisica("12345678901", "sys.discover@gmail.com", "Jose da Silva");
        cotaJose = Fixture.cota(456, jose, SituacaoCadastro.ATIVO, box1);
        save(jose, cotaJose);

        PessoaFisica maria = Fixture.pessoaFisica("12345678902", "sys.discover@gmail.com", "Maria da Silva");
        cotaMaria = Fixture.cota(789, maria, SituacaoCadastro.ATIVO, box1);
        save(maria, cotaMaria);
    }

    private void criarProdutos() {
        Editor editoraAbril = Fixture.editoraAbril();
        save(editoraAbril);

        NCM ncmRevista = Fixture.ncm(49029000l, "REVISTAS", "KG");
        save(ncmRevista);

        TipoProduto tipoProdutoRevista = Fixture.tipoProduto("Revistas", GrupoProduto.REVISTA, ncmRevista, "4902.90.00", 001L);
        save(tipoProdutoRevista);

        produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
        produtoVeja.addFornecedor(fornecedorDinap);
        produtoVeja.setEditor(editoraAbril);
        produtoVeja.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
        save(produtoVeja);

        produtoSuper = Fixture.produtoSuperInteressante(tipoProdutoRevista);
        produtoSuper.addFornecedor(fornecedorDinap);
        produtoSuper.setEditor(editoraAbril);
        produtoSuper.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
        save(produtoSuper);

        produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
        produtoQuatroRodas.addFornecedor(fornecedorDinap);
        produtoQuatroRodas.setEditor(editoraAbril);
        produtoQuatroRodas.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
        save(produtoQuatroRodas);
        
        produtoPlacar = Fixture.produtoPlacar(tipoProdutoRevista);
        produtoPlacar.addFornecedor(fornecedorDinap);
        produtoPlacar.setEditor(editoraAbril);
        produtoPlacar.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
        save(produtoPlacar);

        produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(22.5), "110", produtoVeja, null, false);
        save(produtoEdicaoVeja1);

        produtoEdicaoSuper1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(19.5), "115", produtoSuper, null, false);
        save(produtoEdicaoSuper1);

        produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(18), "120", produtoQuatroRodas, null, false);
        save(produtoEdicaoQuatroRodas1);
        
        produtoEdicaoPlacar1 = Fixture.produtoEdicao(1L, 10, 30, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(15.5), "130", produtoPlacar, null, false);
        save(produtoEdicaoPlacar1);
    }

    private void criarFornecedores() {
        TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
        save(tipoFornecedorPublicacao);

        fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
        save(fornecedorDinap);
    }

    private void criarLancamentos() {
        lancamentoVeja1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                        produtoEdicaoVeja1,  new Date(),
                        DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()), new Date(),
                        new Date(), BigInteger.valueOf(100),
                        StatusLancamento.EXPEDIDO, null, 1);
        save(lancamentoVeja1);

        lancamentoSuper1 = Fixture.lancamento(TipoLancamento.LANCAMENTO, 
                produtoEdicaoSuper1, new Date(), DateUtil.adicionarDias(new Date(), produtoEdicaoSuper1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(50), StatusLancamento.EXPEDIDO,
                null, 2);
        save(lancamentoSuper1);

        lancamentoQuatroRodas1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                produtoEdicaoQuatroRodas1, new Date(),
                DateUtil.adicionarDias(new Date(),
                        produtoEdicaoQuatroRodas1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(50), StatusLancamento.EXPEDIDO,
                null, 3);
        save(lancamentoQuatroRodas1);
        
        lancamentoPlacar1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                produtoEdicaoPlacar1,  new Date(),
                DateUtil.adicionarDias(new Date(),
                        produtoEdicaoPlacar1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(30), StatusLancamento.EXPEDIDO,
                null, 4);
        save(lancamentoPlacar1);
    }

    private void criarRecebimentoFisico() {
        Date dataOperacao = distribuidor.getDataOperacao();
        
        recebimentoFisico = Fixture.recebimentoFisico(notaFiscalFornecedor,
                usuarioJoao, dataOperacao, dataOperacao, StatusConfirmacao.CONFIRMADO);
        save(recebimentoFisico);

        itemRecebimentoFisicoVeja1 = Fixture.itemRecebimentoFisico(
                itemNotaFiscalVeja1, recebimentoFisico, BigInteger.valueOf(90));
        save(itemRecebimentoFisicoVeja1);
        lancamentoVeja1.getRecebimentos().add(itemRecebimentoFisicoVeja1);
        save(lancamentoVeja1);

        itemRecebimentoFisicoSuper1 = Fixture.itemRecebimentoFisico(
                itemNotaFiscalSuper1, recebimentoFisico, BigInteger.valueOf(60));
        save(itemRecebimentoFisicoSuper1);
        lancamentoSuper1.getRecebimentos().add(itemRecebimentoFisicoSuper1);
        save(lancamentoSuper1);
        
        itemRecebimentoFisicoQuatroRodas1 = Fixture.itemRecebimentoFisico(
                itemNotaFiscalQuatroRodas1, recebimentoFisico, BigInteger.valueOf(50));
        save(itemRecebimentoFisicoQuatroRodas1);
        lancamentoQuatroRodas1.getRecebimentos().add(itemRecebimentoFisicoQuatroRodas1);
        save(lancamentoQuatroRodas1);
        
        itemRecebimentoFisicoPlacar1 = Fixture.itemRecebimentoFisico(
                itemNotaFiscalPlacar1, recebimentoFisico, BigInteger.valueOf(30));
        save(itemRecebimentoFisicoPlacar1);
        lancamentoPlacar1.getRecebimentos().add(itemRecebimentoFisicoPlacar1);
        save(lancamentoPlacar1);
        
        TipoMovimentoEstoque tipoMovimentoRecebimentoFisico = Fixture.tipoMovimentoRecebimentoFisico();
        save(tipoMovimentoRecebimentoFisico);
        
        estoqueProdutoVeja1 = new EstoqueProduto();
        estoqueProdutoVeja1.setProdutoEdicao(produtoEdicaoVeja1);
        estoqueProdutoVeja1.setQtde(BigInteger.valueOf(90));
        save(estoqueProdutoVeja1);
        
        MovimentoEstoque movimentoVeja1 = Fixture.movimentoEstoque(null,
                produtoEdicaoVeja1, tipoMovimentoRecebimentoFisico, usuarioJoao, estoqueProdutoVeja1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(90), StatusAprovacao.APROVADO, "OK");
        save(movimentoVeja1);
        
        estoqueProdutoSuper1 = new EstoqueProduto();
        estoqueProdutoSuper1.setProdutoEdicao(produtoEdicaoSuper1);
        estoqueProdutoSuper1.setQtde(BigInteger.valueOf(60));
        save(estoqueProdutoSuper1);
        
        MovimentoEstoque movimentoSuper1 = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tipoMovimentoRecebimentoFisico, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(60), StatusAprovacao.APROVADO, "OK");
        save(movimentoSuper1);
        
        estoqueProdutoQuatroRodas1 = new EstoqueProduto();
        estoqueProdutoQuatroRodas1.setProdutoEdicao(produtoEdicaoQuatroRodas1);
        estoqueProdutoQuatroRodas1.setQtde(BigInteger.valueOf(50));
        save(estoqueProdutoQuatroRodas1);

        MovimentoEstoque movimentoQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoFisico, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(50), StatusAprovacao.APROVADO, "OK");
        save(movimentoQuatroRodas1);

        estoqueProdutoPlacar1 = new EstoqueProduto();
        estoqueProdutoPlacar1.setProdutoEdicao(produtoEdicaoPlacar1);
        estoqueProdutoPlacar1.setQtde(BigInteger.valueOf(30));
        save(estoqueProdutoPlacar1);
        
        MovimentoEstoque movimentoPlacar1 = Fixture.movimentoEstoque(null,
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoFisico, usuarioJoao, estoqueProdutoPlacar1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(30), StatusAprovacao.APROVADO, "OK");
        save(movimentoPlacar1);
    }

    private void criarNotaFiscalEntradaFornecedor() {
        notaFiscalFornecedor = Fixture.notaFiscalEntradaFornecedor(cfop5949,
                fornecedorDinap, tipoNotaFiscalRemessaDistribuicao,
                usuarioJoao, new BigDecimal(4590), BigDecimal.ZERO,
                BigDecimal.valueOf(4590));
        save(notaFiscalFornecedor);

        itemNotaFiscalVeja1 = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
                usuarioJoao, notaFiscalFornecedor,
                lancamentoVeja1.getDataLancamentoPrevista(),
                lancamentoVeja1.getDataRecolhimentoPrevista(),
                TipoLancamento.LANCAMENTO, BigInteger.valueOf(100));
        save(itemNotaFiscalVeja1);

        itemNotaFiscalSuper1 = Fixture.itemNotaFiscal(produtoEdicaoSuper1,
                usuarioJoao, notaFiscalFornecedor,
                lancamentoSuper1.getDataLancamentoPrevista(),
                lancamentoSuper1.getDataRecolhimentoPrevista(),
                TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));
        save(itemNotaFiscalSuper1);

        itemNotaFiscalQuatroRodas1 = Fixture.itemNotaFiscal(
                produtoEdicaoQuatroRodas1, usuarioJoao, notaFiscalFornecedor,
                lancamentoQuatroRodas1.getDataLancamentoPrevista(),
                lancamentoQuatroRodas1.getDataRecolhimentoPrevista(),
                TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));
        save(itemNotaFiscalQuatroRodas1);
        
        itemNotaFiscalPlacar1 = Fixture.itemNotaFiscal(
                produtoEdicaoPlacar1, usuarioJoao, notaFiscalFornecedor,
                lancamentoPlacar1.getDataLancamentoPrevista(),
                lancamentoPlacar1.getDataRecolhimentoPrevista(),
                TipoLancamento.LANCAMENTO, BigInteger.valueOf(30));
        save(itemNotaFiscalPlacar1);
    }

    private void criarParametrosNotaFiscal() {
        cfop5949 = new CFOP();
        cfop5949.setCodigo("5949");
        cfop5949.setDescricao("Outra saída de mercadoria ou prestação de serviço não especificado");
        save(cfop5949);

        cfop6949 = new CFOP();
        cfop6949.setCodigo("6949");
        cfop6949.setDescricao("Outra saída de mercadoria ou prestação de serviço não especificado");
        save(cfop6949);

        tipoNotaFiscalRemessaDistribuicao = new TipoNotaFiscal();
        tipoNotaFiscalRemessaDistribuicao.setCfopEstado(cfop5949);
        tipoNotaFiscalRemessaDistribuicao.setCfopOutrosEstados(cfop6949);
        tipoNotaFiscalRemessaDistribuicao
                .setNopDescricao("NF-E Remessa para Distribuição");
        tipoNotaFiscalRemessaDistribuicao
                .setEmitente(TipoUsuarioNotaFiscal.TREELOG);
        tipoNotaFiscalRemessaDistribuicao
                .setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
        tipoNotaFiscalRemessaDistribuicao.setContribuinte(false);
        tipoNotaFiscalRemessaDistribuicao
                .setDescricao("NF-E Remessa para Distribuição");
        tipoNotaFiscalRemessaDistribuicao.setNopCodigo(0L);
        tipoNotaFiscalRemessaDistribuicao.setTipoOperacao(TipoOperacao.SAIDA);
        tipoNotaFiscalRemessaDistribuicao
                .setGrupoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_DISTRIBUICAO);
        tipoNotaFiscalRemessaDistribuicao
                .setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
        tipoNotaFiscalRemessaDistribuicao.setSerieNotaFiscal(17);
        tipoNotaFiscalRemessaDistribuicao.setProcesso(new HashSet<Processo>());
        tipoNotaFiscalRemessaDistribuicao.getProcesso().add(
                Processo.CONSIGNACAO_REPARTE_NORMAL);
        save(tipoNotaFiscalRemessaDistribuicao);
    }

    private void criarDiferencas() {
        diferencaVeja1 = Fixture.diferenca(BigInteger.valueOf(1), usuarioJoao,
                produtoEdicaoVeja1, TipoDiferenca.FALTA_DE,
                StatusConfirmacao.CONFIRMADO, itemRecebimentoFisicoVeja1, true,
                TipoEstoque.LANCAMENTO, null, distribuidor.getDataOperacao());
        save(diferencaVeja1);

        diferencaSuper1 = Fixture.diferenca(BigInteger.valueOf(1), usuarioJoao,
                produtoEdicaoSuper1, TipoDiferenca.SOBRA_DE,
                StatusConfirmacao.CONFIRMADO, itemRecebimentoFisicoSuper1,
                true, TipoEstoque.LANCAMENTO, null,
                distribuidor.getDataOperacao());
        save(diferencaSuper1);

        diferencaQuatroRodas1 = Fixture.diferenca(BigInteger.valueOf(2),
                usuarioJoao, produtoEdicaoQuatroRodas1, TipoDiferenca.FALTA_EM,
                StatusConfirmacao.CONFIRMADO, null, false,
                TipoEstoque.LANCAMENTO, null, distribuidor.getDataOperacao());
        save(diferencaQuatroRodas1);
    }

    private void criarTransferencias() {
        TipoMovimentoEstoque tmTransferenciaSaidaLancamento = Fixture.tipoMovimentoTransferenciaSaidaLancamento();
        save(tmTransferenciaSaidaLancamento);
        
        TipoMovimentoEstoque tmTransferenciaEntradaSuplementar = Fixture.tipoMovimentoTransferenciaEntradaSuplementar();
        save(tmTransferenciaEntradaSuplementar);
        
        TipoMovimentoEstoque tmTransferenciaSaidaSuplementar = Fixture.tipoMovimentoTransferenciaSaidaSuplementar();
        save(tmTransferenciaSaidaSuplementar);
        
        TipoMovimentoEstoque tmTransferenciaEntradaLancamento = Fixture.tipoMovimentoTransferenciaEntradaLancamento();
        save(tmTransferenciaEntradaLancamento);
        
        MovimentoEstoque movimentoSaidaLancamentoQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tmTransferenciaSaidaLancamento, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(5), StatusAprovacao.APROVADO, "OK");
        
        MovimentoEstoque movimentoEntradaSuplementarQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tmTransferenciaEntradaSuplementar, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(5), StatusAprovacao.APROVADO, "OK");
        
        MovimentoEstoque movimentoSaidaSuplementarQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tmTransferenciaSaidaSuplementar, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(2), StatusAprovacao.APROVADO, "OK");
        
        MovimentoEstoque movimentoEntradaLancamentoQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tmTransferenciaEntradaLancamento, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(2), StatusAprovacao.APROVADO, "OK");
        
        save(movimentoSaidaLancamentoQuatroRodas1,
                movimentoEntradaSuplementarQuatroRodas1,
                movimentoSaidaSuplementarQuatroRodas1,
                movimentoEntradaLancamentoQuatroRodas1);
        
        estoqueProdutoQuatroRodas1.setQtde(estoqueProdutoQuatroRodas1.getQtde().subtract(BigInteger.valueOf(3)));
        estoqueProdutoQuatroRodas1.setQtdeSuplementar(BigInteger.valueOf(3));
        save(estoqueProdutoQuatroRodas1);
    }

    private void criarEstudos() {
        Estudo estudoVeja1 = Fixture.estudo(BigInteger.valueOf(90), lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
        ecManoelVeja1 = Fixture.estudoCota(BigInteger.valueOf(30), BigInteger.valueOf(30), estudoVeja1, cotaManoel);
        ecJoseVeja1 = Fixture.estudoCota(BigInteger.valueOf(40), BigInteger.valueOf(40), estudoVeja1, cotaJose);
        ecMariaVeja1 = Fixture.estudoCota(BigInteger.valueOf(20), BigInteger.valueOf(20), estudoVeja1, cotaMaria);
        save(estudoVeja1, ecManoelVeja1, ecJoseVeja1, ecMariaVeja1);

        Estudo estudoQuatroRodas1 = Fixture.estudo(BigInteger.valueOf(43), lancamentoQuatroRodas1.getDataLancamentoDistribuidor(), produtoEdicaoQuatroRodas1);
        ecManoelQuatroRodas1 = Fixture.estudoCota(BigInteger.valueOf(20), BigInteger.valueOf(20), estudoQuatroRodas1, cotaManoel);
        ecJoseQuatroRodas1 = Fixture.estudoCota(BigInteger.valueOf(15), BigInteger.valueOf(15), estudoQuatroRodas1, cotaJose);
        ecMariaQuatroRodas1 = Fixture.estudoCota(BigInteger.valueOf(8), BigInteger.valueOf(8), estudoQuatroRodas1, cotaMaria);
        save(estudoQuatroRodas1, ecManoelQuatroRodas1, ecJoseQuatroRodas1, ecMariaQuatroRodas1);

        Estudo estudoSuper1 = Fixture.estudo(BigInteger.valueOf(50), lancamentoSuper1.getDataLancamentoDistribuidor(), produtoEdicaoSuper1);
        ecManoelSuper1 = Fixture.estudoCota(BigInteger.valueOf(15), BigInteger.valueOf(15), estudoSuper1, cotaManoel);
        ecJoseSuper1 = Fixture.estudoCota(BigInteger.valueOf(15), BigInteger.valueOf(15), estudoSuper1, cotaJose);
        ecMariaSuper1 = Fixture.estudoCota(BigInteger.valueOf(20), BigInteger.valueOf(20), estudoSuper1, cotaMaria);
        save(estudoSuper1, ecManoelSuper1, ecJoseSuper1, ecMariaSuper1);
        
        Estudo estudoPlacar1 = Fixture.estudo(BigInteger.valueOf(30), lancamentoPlacar1.getDataLancamentoDistribuidor(), produtoEdicaoPlacar1);
        ecManoelPlacar1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoPlacar1, cotaManoel);
        ecJosePlacar1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoPlacar1, cotaJose);
        ecMariaPlacar1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoPlacar1, cotaMaria);
        save(estudoPlacar1, ecManoelPlacar1, ecJosePlacar1, ecMariaPlacar1);
    }

    private void criarExpedicao() {
        TipoMovimentoEstoque tipoMovimentoRecebimentoReparte = Fixture.tipoMovimentoRecebimentoReparte();
        save(tipoMovimentoRecebimentoReparte);
        
        tipoMovimentoEnvioJornaleiro = Fixture.tipoMovimentoEnvioJornaleiro();
        save(tipoMovimentoEnvioJornaleiro);

        Expedicao expedicao = new Expedicao();
        expedicao.setDataExpedicao(distribuidor.getDataOperacao());
        expedicao.setResponsavel(usuarioJoao);

        lancamentoVeja1.setExpedicao(expedicao);
        expedicao.getLancamentos().add(lancamentoVeja1);

        lancamentoSuper1.setExpedicao(expedicao);
        expedicao.getLancamentos().add(lancamentoSuper1);

        lancamentoQuatroRodas1.setExpedicao(expedicao);
        expedicao.getLancamentos().add(lancamentoQuatroRodas1);
        
        lancamentoPlacar1.setExpedicao(expedicao);
        expedicao.getLancamentos().add(lancamentoPlacar1);
        
        save(expedicao, lancamentoVeja1, lancamentoSuper1, lancamentoQuatroRodas1, lancamentoPlacar1);

        EstoqueProdutoCota epcManoelVeja1 = Fixture.estoqueProdutoCota(produtoEdicaoVeja1, ecManoelVeja1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelVeja1);
        
        MovimentoEstoqueCota mecManoelVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelVeja1,
                ecManoelVeja1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelVeja1.setMovimentos(Arrays.asList(mecManoelVeja1));
        save(mecManoelVeja1, epcManoelVeja1);
        
        EstoqueProdutoCota epcManoelQuatroRodas1 = Fixture.estoqueProdutoCota(produtoEdicaoQuatroRodas1, ecManoelQuatroRodas1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelQuatroRodas1);
        
        MovimentoEstoqueCota mecManoelQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelQuatroRodas1,
                ecManoelQuatroRodas1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelQuatroRodas1.setMovimentos(Arrays.asList(mecManoelQuatroRodas1));
        save(mecManoelQuatroRodas1, epcManoelQuatroRodas1);
        
        EstoqueProdutoCota epcManoelSuper1 = Fixture.estoqueProdutoCota(produtoEdicaoSuper1, ecManoelSuper1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelSuper1);
        
        MovimentoEstoqueCota mecManoelSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelSuper1,
                ecManoelSuper1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelSuper1.setMovimentos(Arrays.asList(mecManoelSuper1));
        save(mecManoelSuper1, epcManoelSuper1);
        
        epcManoelPlacar1 = Fixture.estoqueProdutoCota(produtoEdicaoPlacar1, ecManoelPlacar1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelPlacar1);
        
        MovimentoEstoqueCota mecManoelPlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelPlacar1,
                ecManoelPlacar1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelPlacar1.setMovimentos(Arrays.asList(mecManoelPlacar1));
        save(mecManoelPlacar1, epcManoelPlacar1);
        
        EstoqueProdutoCota epcJoseVeja1 = Fixture.estoqueProdutoCota(produtoEdicaoVeja1, ecJoseVeja1.getQtdeEfetiva(), cotaJose, null);
        save(epcJoseVeja1);
        
        MovimentoEstoqueCota mecJoseVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJoseVeja1,
                ecJoseVeja1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJoseVeja1.setMovimentos(Arrays.asList(mecJoseVeja1));
        save(mecJoseVeja1, epcJoseVeja1);
        
        EstoqueProdutoCota epcJoseQuatroRodas1 = Fixture.estoqueProdutoCota(produtoEdicaoQuatroRodas1, ecJoseQuatroRodas1.getQtdeEfetiva(), cotaJose, null);
        save(epcJoseQuatroRodas1);
        
        MovimentoEstoqueCota mecJoseQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJoseQuatroRodas1,
                ecJoseQuatroRodas1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJoseQuatroRodas1.setMovimentos(Arrays.asList(mecJoseQuatroRodas1));
        save(mecJoseQuatroRodas1, epcJoseQuatroRodas1);
        
        EstoqueProdutoCota epcJoseSuper1 = Fixture.estoqueProdutoCota(produtoEdicaoSuper1, ecJoseSuper1.getQtdeEfetiva(), cotaJose, null);
        save(epcJoseSuper1);
        
        MovimentoEstoqueCota mecJoseSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJoseSuper1,
                ecJoseSuper1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJoseSuper1.setMovimentos(Arrays.asList(mecJoseSuper1));
        save(mecJoseSuper1, epcJoseSuper1);
        
        epcJosePlacar1 = Fixture.estoqueProdutoCota(produtoEdicaoPlacar1, ecJosePlacar1.getQtdeEfetiva(), cotaJose, null);
        save(epcJosePlacar1);
        
        MovimentoEstoqueCota mecJosePlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJosePlacar1,
                ecJosePlacar1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJosePlacar1.setMovimentos(Arrays.asList(mecJosePlacar1));
        save(mecJosePlacar1, epcJosePlacar1);
        
        EstoqueProdutoCota epcMariaVeja1 = Fixture.estoqueProdutoCota(produtoEdicaoVeja1, ecMariaVeja1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaVeja1);
        
        MovimentoEstoqueCota mecMariaVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaVeja1,
                ecMariaVeja1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaVeja1.setMovimentos(Arrays.asList(mecMariaVeja1));
        save(mecMariaVeja1, epcMariaVeja1);
        
        EstoqueProdutoCota epcMariaQuatroRodas1 = Fixture.estoqueProdutoCota(produtoEdicaoQuatroRodas1, ecMariaQuatroRodas1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaQuatroRodas1);
        
        MovimentoEstoqueCota mecMariaQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaQuatroRodas1,
                ecMariaQuatroRodas1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaQuatroRodas1.setMovimentos(Arrays.asList(mecMariaQuatroRodas1));
        save(mecMariaQuatroRodas1, epcMariaQuatroRodas1);
        
        EstoqueProdutoCota epcMariaSuper1 = Fixture.estoqueProdutoCota(produtoEdicaoSuper1, ecMariaSuper1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaSuper1);
        
        MovimentoEstoqueCota mecMariaSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaSuper1,
                ecMariaSuper1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaSuper1.setMovimentos(Arrays.asList(mecMariaSuper1));
        save(mecMariaSuper1, epcMariaSuper1);
        
        epcMariaPlacar1 = Fixture.estoqueProdutoCota(produtoEdicaoPlacar1, ecMariaPlacar1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaPlacar1);
        
        MovimentoEstoqueCota mecMariaPlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaPlacar1,
                ecMariaPlacar1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaPlacar1.setMovimentos(Arrays.asList(mecMariaPlacar1));
        save(mecMariaPlacar1, epcMariaPlacar1);
        
        BigInteger saidaVeja1 = BigIntegerUtil.soma(mecManoelVeja1.getQtde(), mecJoseVeja1.getQtde(), mecMariaVeja1.getQtde());

        MovimentoEstoque movimentoEstoqueVeja1 = Fixture.movimentoEstoque(null,
                produtoEdicaoVeja1, tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoVeja1,
                distribuidor.getDataOperacao(), saidaVeja1, StatusAprovacao.APROVADO, "OK");
        save(movimentoEstoqueVeja1);
        
        estoqueProdutoVeja1.setQtde(estoqueProdutoVeja1.getQtde().subtract(saidaVeja1));
        save(estoqueProdutoVeja1);
        
        BigInteger saidaQuatroRodas1 = BigIntegerUtil.soma(mecManoelQuatroRodas1.getQtde(), mecJoseQuatroRodas1.getQtde(), mecMariaQuatroRodas1.getQtde());
       
        MovimentoEstoque movimentoQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), saidaQuatroRodas1, StatusAprovacao.APROVADO, "OK");
        save(movimentoQuatroRodas1);
        
        estoqueProdutoQuatroRodas1.setQtde(estoqueProdutoQuatroRodas1.getQtde().subtract(saidaQuatroRodas1));
        save(estoqueProdutoQuatroRodas1);
        
        BigInteger saidaSuper1 = BigIntegerUtil.soma(mecManoelSuper1.getQtde(), mecJoseSuper1.getQtde(), mecMariaSuper1.getQtde());
        
        MovimentoEstoque movimentoSuper1 = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), saidaSuper1, StatusAprovacao.APROVADO, "OK");
        save(movimentoSuper1);
        
        estoqueProdutoSuper1.setQtde(estoqueProdutoSuper1.getQtde().subtract(saidaSuper1));
        save(estoqueProdutoSuper1);
        
        
        BigInteger saidaPlacar1 = BigIntegerUtil.soma(mecManoelPlacar1.getQtde(), mecJosePlacar1.getQtde(), mecMariaPlacar1.getQtde());

        MovimentoEstoque movimentoEstoquePlacar1 = Fixture.movimentoEstoque(null,
                produtoEdicaoPlacar1, tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoPlacar1,
                distribuidor.getDataOperacao(), saidaPlacar1, StatusAprovacao.APROVADO, "OK");
        save(movimentoEstoquePlacar1);
        
        estoqueProdutoPlacar1.setQtde(estoqueProdutoPlacar1.getQtde().subtract(saidaPlacar1));
        save(estoqueProdutoPlacar1);
    }

    private void criarFuroProduto() {
        TipoMovimentoEstoque tipoMovimentoEstornoFuroPublicacao = Fixture.tipoMovimentoEstornoFuroPublicacao();
        save(tipoMovimentoEstornoFuroPublicacao);
        
        TipoMovimentoEstoque tipoEstornoRecebimentoReparte = Fixture.tipoMovimentoEstornoCotaFuroPublicacao();
        save(tipoEstornoRecebimentoReparte);
        
        lancamentoPlacar1.setDataLancamentoDistribuidor(distribuidor.getDataOperacao());
        lancamentoPlacar1.setStatus(StatusLancamento.FURO);
        
        FuroProduto furoProduto = new FuroProduto();
        furoProduto.setData(distribuidor.getDataOperacao());
        furoProduto.setLancamento(lancamentoPlacar1);
        furoProduto.setProdutoEdicao(produtoEdicaoPlacar1);
        furoProduto.setUsuario(usuarioJoao);
        save(lancamentoPlacar1, furoProduto);
        
        MovimentoEstoque movimentoEstoquePlacar1 = Fixture.movimentoEstoque(null,
                produtoEdicaoPlacar1, tipoMovimentoEstornoFuroPublicacao, usuarioJoao, estoqueProdutoPlacar1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(30), StatusAprovacao.APROVADO, "OK");
        save(movimentoEstoquePlacar1);
        estoqueProdutoPlacar1.setQtde(BigInteger.valueOf(30));
        save(estoqueProdutoPlacar1);
        
        MovimentoEstoqueCota mecManoelPlacar1 = Fixture.movimentoEstoqueCota(produtoEdicaoPlacar1, tipoEstornoRecebimentoReparte,
                usuarioJoao, epcManoelPlacar1,
                ecManoelPlacar1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelPlacar1.setMovimentos(Arrays.asList(mecManoelPlacar1));
        epcManoelPlacar1.setQtdeRecebida(BigInteger.ZERO);
        save(mecManoelPlacar1, epcManoelPlacar1);
        
        MovimentoEstoqueCota mecJosePlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoEstornoRecebimentoReparte,
                usuarioJoao, epcJosePlacar1,
                ecJosePlacar1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJosePlacar1.setMovimentos(Arrays.asList(mecJosePlacar1));
        epcJosePlacar1.setQtdeRecebida(BigInteger.ZERO);
        save(mecJosePlacar1, epcJosePlacar1);
        
        MovimentoEstoqueCota mecMariaPlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoEstornoRecebimentoReparte,
                usuarioJoao, epcMariaPlacar1,
                ecMariaPlacar1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaPlacar1.setMovimentos(Arrays.asList(mecMariaPlacar1));
        epcMariaPlacar1.setQtdeRecebida(BigInteger.ZERO);
        save(mecMariaPlacar1, epcMariaPlacar1);
    }

    private static final Comparator<ReparteFecharDiaDTO> COMPARATOR_CODIGO_PRODUTO = new Comparator<ReparteFecharDiaDTO>() {
        @Override
        public int compare(ReparteFecharDiaDTO o1, ReparteFecharDiaDTO o2) {
            return o1.getCodigo().compareTo(o2.getCodigo());
        }
    };



}
