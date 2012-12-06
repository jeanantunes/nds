package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import br.com.abril.nds.client.util.BigIntegerUtil;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
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
import br.com.abril.nds.util.DateUtil;

/**
 * Classe base para testes de repositório com métodos utilitários
 * para criação de massa de dados para testes de cenários
 * 
 * @author francisco.garcia
 *
 */
public class AbstractDataUtilRepositoryImplTest extends AbstractRepositoryImplTest {

  
    protected Distribuidor distribuidor;
    protected Produto produtoVeja;
    protected Produto produtoSuper;
    protected Produto produtoQuatroRodas;
    protected Produto produtoPlacar;
    protected Produto produtoCaras;
    protected Produto produtoCapricho;
   
    protected ProdutoEdicao produtoEdicaoVeja1;
    protected ProdutoEdicao produtoEdicaoSuper1;
    protected ProdutoEdicao produtoEdicaoQuatroRodas1;
    protected ProdutoEdicao produtoEdicaoCaras1;
    protected ProdutoEdicao produtoEdicaoPlacar1;
    protected ProdutoEdicao produtoEdicaoCapricho1;
    
    protected EstoqueProduto estoqueProdutoVeja1;
    protected EstoqueProduto estoqueProdutoQuatroRodas1;
    protected EstoqueProduto estoqueProdutoSuper1;
    protected EstoqueProduto estoqueProdutoPlacar1;
    protected EstoqueProduto estoqueProdutoCaras1;
    protected EstoqueProduto estoqueProdutoCapricho1;
    
    protected CFOP cfop5949;
    protected CFOP cfop6949;
    
    protected TipoNotaFiscal tipoNotaFiscalRemessaDistribuicao;
    
    protected NotaFiscalEntradaFornecedor notaFiscalFornecedor;
    
    protected ItemNotaFiscalEntrada itemNotaFiscalVeja1;
    protected ItemNotaFiscalEntrada itemNotaFiscalSuper1;
    protected ItemNotaFiscalEntrada itemNotaFiscalQuatroRodas1;
    protected ItemNotaFiscalEntrada itemNotaFiscalPlacar1;
    protected ItemNotaFiscalEntrada itemNotaFiscalCaras1;
    protected ItemNotaFiscalEntrada itemNotaFiscalCapricho1;
    
    protected RecebimentoFisico recebimentoFisico;
    
    protected ItemRecebimentoFisico itemRecebimentoFisicoVeja1;
    protected ItemRecebimentoFisico itemRecebimentoFisicoSuper1;
    protected ItemRecebimentoFisico itemRecebimentoFisicoQuatroRodas1;
    protected ItemRecebimentoFisico itemRecebimentoFisicoPlacar1;
    protected ItemRecebimentoFisico itemRecebimentoFisicoCaras1;
    protected ItemRecebimentoFisico itemRecebimentoFisicoCapricho1;
    
    protected Lancamento lancamentoVeja1;
    protected Lancamento lancamentoSuper1;
    protected Lancamento lancamentoQuatroRodas1;
    protected Lancamento lancamentoPlacar1;
    protected Lancamento lancamentoCaras1;
    protected Lancamento lancamentoCapricho1;
    
    protected Cota cotaManoel;
    protected Cota cotaJose;
    protected Cota cotaMaria;
    
    protected Usuario usuarioJoao;
    
    protected Fornecedor fornecedorDinap;
    
    protected Diferenca diferencaVeja1;
    protected Diferenca diferencaSuper1;
    protected Diferenca diferencaQuatroRodas1;
    protected Diferenca diferencaCaras1;
    
    protected EstudoCota ecManoelVeja1;
    protected EstudoCota ecJoseVeja1;
    protected EstudoCota ecMariaVeja1;
    protected EstudoCota ecManoelQuatroRodas1;
    protected EstudoCota ecJoseQuatroRodas1;
    protected EstudoCota ecMariaQuatroRodas1;
    protected EstudoCota ecManoelSuper1;
    protected EstudoCota ecJoseSuper1;
    protected EstudoCota ecMariaSuper1;
    protected EstudoCota ecManoelPlacar1;
    protected EstudoCota ecJosePlacar1;
    protected EstudoCota ecMariaPlacar1;
    
    protected TipoMovimentoEstoque tipoMovimentoEnvioJornaleiro;
    
    protected EstoqueProdutoCota epcManoelPlacar1;
    protected EstoqueProdutoCota epcJosePlacar1;
    protected EstoqueProdutoCota epcMariaPlacar1;
   
    protected Box box1;
    
    protected EstoqueProdutoCota epcManoelVeja1;
    protected EstoqueProdutoCota epcManoelQuatroRodas1;
    protected EstoqueProdutoCota epcManoelSuper1;
    
    protected EstoqueProdutoCota epcJoseVeja1;
    protected EstoqueProdutoCota epcJoseQuatroRodas1;
    protected EstoqueProdutoCota epcJoseSuper1;
    
    protected EstoqueProdutoCota epcMariaVeja1;
    protected EstoqueProdutoCota epcMariaQuatroRodas1;
    protected EstoqueProdutoCota epcMariaSuper1;
    
    protected void criarUsuarios() {
        usuarioJoao = Fixture.usuarioJoao();
        save(usuarioJoao);
    }
    
    protected void criarDistribuidor() {
        PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme", "56.003.315/0001-47", "333333333333", "distrib_acme@mail.com", "99.999-9");
        save(juridicaDistrib);
    
        distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
        ParametroContratoCota parametro = new ParametroContratoCota();
        parametro.setDiasAvisoRescisao(30);
        parametro.setDuracaoContratoCota(360);
      
        distribuidor.setParametroContratoCota(parametro);
        save(distribuidor);
    }
    
    protected void criarCotas() {
        box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
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
    
    protected void criarProdutos() {
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
        
        produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
        produtoCaras.addFornecedor(fornecedorDinap);
        produtoCaras.setEditor(editoraAbril);
        produtoCaras.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
        save(produtoCaras);
        
        produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
        produtoCapricho.addFornecedor(fornecedorDinap);
        produtoCapricho.setEditor(editoraAbril);
        produtoCapricho.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
        save(produtoCapricho);
    
        produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(22.5), "110", produtoVeja, null, false);
        save(produtoEdicaoVeja1);
    
        produtoEdicaoSuper1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(19.5), "115", produtoSuper, null, false);
        save(produtoEdicaoSuper1);
    
        produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(18), "120", produtoQuatroRodas, null, false);
        save(produtoEdicaoQuatroRodas1);
        
        produtoEdicaoPlacar1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(15.5), "130", produtoPlacar, null, false);
        save(produtoEdicaoPlacar1);
        
        produtoEdicaoCaras1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(30), "140", produtoCaras, null, false);
        save(produtoEdicaoCaras1);
        
        produtoEdicaoCapricho1 = Fixture.produtoEdicao(1L, 10, 14, Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(11), "150", produtoCapricho, null, false);
        save(produtoEdicaoCapricho1);
    }
    
    protected void criarFornecedores() {
        TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
        save(tipoFornecedorPublicacao);
    
        fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
        save(fornecedorDinap);
    }
    
    protected void criarLancamentos() {
        lancamentoVeja1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                        produtoEdicaoVeja1,  distribuidor.getDataOperacao(),
                        DateUtil.adicionarDias(distribuidor.getDataOperacao(), produtoEdicaoVeja1.getPeb()), new Date(),
                        new Date(), BigInteger.valueOf(100),
                        StatusLancamento.EXPEDIDO, null, 1);
        save(lancamentoVeja1);
    
        lancamentoSuper1 = Fixture.lancamento(TipoLancamento.LANCAMENTO, 
                produtoEdicaoSuper1, distribuidor.getDataOperacao(), DateUtil.adicionarDias(distribuidor.getDataOperacao(), produtoEdicaoSuper1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(50), StatusLancamento.EXPEDIDO,
                null, 2);
        save(lancamentoSuper1);
    
        lancamentoQuatroRodas1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                produtoEdicaoQuatroRodas1, distribuidor.getDataOperacao(),
                DateUtil.adicionarDias(distribuidor.getDataOperacao(),
                        produtoEdicaoQuatroRodas1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(50), StatusLancamento.EXPEDIDO,
                null, 3);
        save(lancamentoQuatroRodas1);
        
        lancamentoPlacar1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                produtoEdicaoPlacar1,  distribuidor.getDataOperacao(),
                DateUtil.adicionarDias(distribuidor.getDataOperacao(),
                        produtoEdicaoPlacar1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(30), StatusLancamento.EXPEDIDO,
                null, 4);
        save(lancamentoPlacar1);
        
        lancamentoCaras1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                produtoEdicaoCaras1,  DateUtil.adicionarDias(distribuidor.getDataOperacao(), 1),
                DateUtil.adicionarDias(distribuidor.getDataOperacao(),
                        produtoEdicaoCaras1.getPeb() + 1), new Date(),
                new Date(), BigInteger.valueOf(25), StatusLancamento.BALANCEADO,
                null, 1);
        save(lancamentoCaras1);
        
        lancamentoCapricho1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
                produtoEdicaoCapricho1,  distribuidor.getDataOperacao(),
                DateUtil.adicionarDias(distribuidor.getDataOperacao(),
                        produtoEdicaoCapricho1.getPeb()), new Date(),
                new Date(), BigInteger.valueOf(20), StatusLancamento.BALANCEADO,
                null, 5);
        save(lancamentoCapricho1);
    }
    
    protected void criarRecebimentoFisico() {
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
        
        itemRecebimentoFisicoCaras1 = Fixture.itemRecebimentoFisico(
                itemNotaFiscalCaras1, recebimentoFisico, BigInteger.valueOf(23));
        save(itemRecebimentoFisicoCaras1);
        lancamentoCaras1.getRecebimentos().add(itemRecebimentoFisicoCaras1);
        save(lancamentoCaras1);
        
        itemRecebimentoFisicoCapricho1 = Fixture.itemRecebimentoFisico(
                itemNotaFiscalCapricho1, recebimentoFisico, BigInteger.valueOf(20));
        save(itemRecebimentoFisicoCapricho1);
        lancamentoCapricho1.getRecebimentos().add(itemRecebimentoFisicoCapricho1);
        save(lancamentoCapricho1);
        
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
        
        estoqueProdutoCaras1 = new EstoqueProduto();
        estoqueProdutoCaras1.setProdutoEdicao(produtoEdicaoCaras1);
        estoqueProdutoCaras1.setQtde(BigInteger.valueOf(23));
        save(estoqueProdutoCaras1);
        
        MovimentoEstoque movimentoCaras1 = Fixture.movimentoEstoque(null,
                produtoEdicaoCaras1, tipoMovimentoRecebimentoFisico, usuarioJoao, estoqueProdutoCaras1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(23), StatusAprovacao.APROVADO, "OK");
        save(movimentoCaras1);
        
        estoqueProdutoCapricho1 = new EstoqueProduto();
        estoqueProdutoCapricho1.setProdutoEdicao(produtoEdicaoCapricho1);
        estoqueProdutoCapricho1.setQtde(BigInteger.valueOf(20));
        save(estoqueProdutoCapricho1);
        
        MovimentoEstoque movimentoCapricho1 = Fixture.movimentoEstoque(null,
                produtoEdicaoCapricho1, tipoMovimentoRecebimentoFisico, usuarioJoao, estoqueProdutoCapricho1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(20), StatusAprovacao.APROVADO, "OK");
        save(movimentoCapricho1);
    }
    
    protected void criarNotaFiscalEntradaFornecedor() {
        notaFiscalFornecedor = Fixture.notaFiscalEntradaFornecedor(cfop5949,
                fornecedorDinap, tipoNotaFiscalRemessaDistribuicao,
                usuarioJoao, new BigDecimal(5560), BigDecimal.ZERO,
                BigDecimal.valueOf(5560));
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
        
        itemNotaFiscalCaras1 = Fixture.itemNotaFiscal(
                produtoEdicaoCaras1, usuarioJoao, notaFiscalFornecedor,
                lancamentoCaras1.getDataLancamentoPrevista(),
                lancamentoCaras1.getDataRecolhimentoPrevista(),
                TipoLancamento.LANCAMENTO, BigInteger.valueOf(25));
        save(itemNotaFiscalCaras1);
        
        itemNotaFiscalCapricho1 = Fixture.itemNotaFiscal(
                produtoEdicaoCapricho1, usuarioJoao, notaFiscalFornecedor,
                lancamentoCapricho1.getDataLancamentoPrevista(),
                lancamentoCapricho1.getDataRecolhimentoPrevista(),
                TipoLancamento.LANCAMENTO, BigInteger.valueOf(20));
        save(itemNotaFiscalCapricho1);
    }
    
    protected void criarParametrosNotaFiscal() {
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
    
    protected void criarDiferencas() {
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
        
        diferencaCaras1 = Fixture.diferenca(BigInteger.valueOf(2),
                usuarioJoao, produtoEdicaoCaras1, TipoDiferenca.FALTA_EM,
                StatusConfirmacao.CONFIRMADO, null, false,
                TipoEstoque.LANCAMENTO, null, distribuidor.getDataOperacao());
        save(diferencaCaras1);
    }
    
    protected void criarTransferencias() {
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
    
    protected void criarEstudos() {
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
        
        Estudo estudoCaras1 = Fixture.estudo(BigInteger.valueOf(23), lancamentoCaras1.getDataLancamentoDistribuidor(), produtoEdicaoCaras1);
        EstudoCota ecManoelCaras1 = Fixture.estudoCota(BigInteger.valueOf(8), BigInteger.valueOf(8), estudoCaras1, cotaManoel);
        EstudoCota ecJoseCaras1 = Fixture.estudoCota(BigInteger.valueOf(8), BigInteger.valueOf(8), estudoCaras1, cotaJose);
        EstudoCota ecMariaCaras1 = Fixture.estudoCota(BigInteger.valueOf(7), BigInteger.valueOf(7), estudoCaras1, cotaMaria);
        save(estudoCaras1, ecManoelCaras1, ecJoseCaras1, ecMariaCaras1);
        
        Estudo estudoCapricho1 = Fixture.estudo(BigInteger.valueOf(20), lancamentoCapricho1.getDataLancamentoDistribuidor(), produtoEdicaoCapricho1);
        EstudoCota ecManoelCapricho1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoCapricho1, cotaManoel);
        EstudoCota ecJoseCapricho1 = Fixture.estudoCota(BigInteger.valueOf(5), BigInteger.valueOf(5), estudoCapricho1, cotaJose);
        EstudoCota ecMariaCapricho1 = Fixture.estudoCota(BigInteger.valueOf(5), BigInteger.valueOf(5), estudoCapricho1, cotaMaria);
        save(estudoCapricho1, ecManoelCapricho1, ecJoseCapricho1, ecMariaCapricho1);
    }
    
    protected void criarExpedicao() {
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
    
        epcManoelVeja1 = Fixture.estoqueProdutoCota(produtoEdicaoVeja1, ecManoelVeja1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelVeja1);
        
        MovimentoEstoqueCota mecManoelVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelVeja1,
                ecManoelVeja1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueManoelVeja = new ArrayList<>();
        movimentosEstoqueManoelVeja.add(mecManoelVeja1);
        epcManoelVeja1.setMovimentos(movimentosEstoqueManoelVeja);
        save(mecManoelVeja1, epcManoelVeja1);
        
        epcManoelQuatroRodas1 = Fixture.estoqueProdutoCota(produtoEdicaoQuatroRodas1, ecManoelQuatroRodas1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelQuatroRodas1);
        
        MovimentoEstoqueCota mecManoelQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelQuatroRodas1,
                ecManoelQuatroRodas1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentoEstoqueManoelQuatroRodas = new ArrayList<>();
        movimentoEstoqueManoelQuatroRodas.add(mecManoelQuatroRodas1);
        epcManoelQuatroRodas1.setMovimentos(movimentoEstoqueManoelQuatroRodas);
        save(mecManoelQuatroRodas1, epcManoelQuatroRodas1);
        
        epcManoelSuper1 = Fixture.estoqueProdutoCota(produtoEdicaoSuper1, ecManoelSuper1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelSuper1);
        
        MovimentoEstoqueCota mecManoelSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelSuper1,
                ecManoelSuper1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueManoelSuper = new ArrayList<>();
        movimentosEstoqueManoelSuper.add(mecManoelSuper1);
        epcManoelSuper1.setMovimentos(movimentosEstoqueManoelSuper);
        save(mecManoelSuper1, epcManoelSuper1);
        
        epcManoelPlacar1 = Fixture.estoqueProdutoCota(produtoEdicaoPlacar1, ecManoelPlacar1.getQtdeEfetiva(), cotaManoel, null);
        save(epcManoelPlacar1);
        
        MovimentoEstoqueCota mecManoelPlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcManoelPlacar1,
                ecManoelPlacar1.getQtdeEfetiva(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueManoelPlacar = new ArrayList<>();
        movimentosEstoqueManoelPlacar.add(mecManoelPlacar1);
        epcManoelPlacar1.setMovimentos(movimentosEstoqueManoelPlacar);
        save(mecManoelPlacar1, epcManoelPlacar1);
        
        epcJoseVeja1 = Fixture.estoqueProdutoCota(produtoEdicaoVeja1, ecJoseVeja1.getQtdeEfetiva(), cotaJose, null);
        save(epcJoseVeja1);
        
        MovimentoEstoqueCota mecJoseVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJoseVeja1,
                ecJoseVeja1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueJoseVeja = new ArrayList<>();
        movimentosEstoqueJoseVeja.add(mecJoseVeja1);
        epcJoseVeja1.setMovimentos(movimentosEstoqueJoseVeja);
        save(mecJoseVeja1, epcJoseVeja1);
        
        epcJoseQuatroRodas1 = Fixture.estoqueProdutoCota(produtoEdicaoQuatroRodas1, ecJoseQuatroRodas1.getQtdeEfetiva(), cotaJose, null);
        save(epcJoseQuatroRodas1);
        
        MovimentoEstoqueCota mecJoseQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJoseQuatroRodas1,
                ecJoseQuatroRodas1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueJoseQuatroRodas = new ArrayList<>();
        movimentosEstoqueJoseQuatroRodas.add(mecJoseQuatroRodas1);
        epcJoseQuatroRodas1.setMovimentos(movimentosEstoqueJoseQuatroRodas);
        save(mecJoseQuatroRodas1, epcJoseQuatroRodas1);
        
        epcJoseSuper1 = Fixture.estoqueProdutoCota(produtoEdicaoSuper1, ecJoseSuper1.getQtdeEfetiva(), cotaJose, null);
        save(epcJoseSuper1);
        
        MovimentoEstoqueCota mecJoseSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJoseSuper1,
                ecJoseSuper1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueJoseSuper = new ArrayList<>();
        movimentosEstoqueJoseSuper.add(mecJoseSuper1);
        epcJoseSuper1.setMovimentos(movimentosEstoqueJoseSuper);
        save(mecJoseSuper1, epcJoseSuper1);
        
        epcJosePlacar1 = Fixture.estoqueProdutoCota(produtoEdicaoPlacar1, ecJosePlacar1.getQtdeEfetiva(), cotaJose, null);
        save(epcJosePlacar1);
        
        MovimentoEstoqueCota mecJosePlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcJosePlacar1,
                ecJosePlacar1.getQtdeEfetiva(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueJosePlacar = new ArrayList<>();
        movimentosEstoqueJosePlacar.add(mecJosePlacar1);
        epcJosePlacar1.setMovimentos(movimentosEstoqueJosePlacar);
        save(mecJosePlacar1, epcJosePlacar1);
        
        epcMariaVeja1 = Fixture.estoqueProdutoCota(produtoEdicaoVeja1, ecMariaVeja1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaVeja1);
        
        MovimentoEstoqueCota mecMariaVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaVeja1,
                ecMariaVeja1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueMariaVeja = new ArrayList<>();
        movimentosEstoqueMariaVeja.add(mecMariaVeja1);
        epcMariaVeja1.setMovimentos(movimentosEstoqueMariaVeja);
        save(mecMariaVeja1, epcMariaVeja1);
        
        epcMariaQuatroRodas1 = Fixture.estoqueProdutoCota(produtoEdicaoQuatroRodas1, ecMariaQuatroRodas1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaQuatroRodas1);
        
        MovimentoEstoqueCota mecMariaQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaQuatroRodas1,
                ecMariaQuatroRodas1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueMariaQuatroRodas = new ArrayList<>();
        movimentosEstoqueMariaQuatroRodas.add(mecMariaQuatroRodas1);
        epcMariaQuatroRodas1.setMovimentos(movimentosEstoqueMariaQuatroRodas);
        save(mecMariaQuatroRodas1, epcMariaQuatroRodas1);
        
        epcMariaSuper1 = Fixture.estoqueProdutoCota(produtoEdicaoSuper1, ecMariaSuper1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaSuper1);
        
        MovimentoEstoqueCota mecMariaSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaSuper1,
                ecMariaSuper1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueMariaSuper = new ArrayList<>();
        movimentosEstoqueMariaSuper.add(mecMariaSuper1);
        epcMariaSuper1.setMovimentos(movimentosEstoqueMariaSuper);
        save(mecMariaSuper1, epcMariaSuper1);
        
        epcMariaPlacar1 = Fixture.estoqueProdutoCota(produtoEdicaoPlacar1, ecMariaPlacar1.getQtdeEfetiva(), cotaMaria, null);
        save(epcMariaPlacar1);
        
        MovimentoEstoqueCota mecMariaPlacar1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoPlacar1, tipoMovimentoRecebimentoReparte,
                usuarioJoao, epcMariaPlacar1,
                ecMariaPlacar1.getQtdeEfetiva(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        List<MovimentoEstoqueCota> movimentosEstoqueMariaPlacar = new ArrayList<>();
        movimentosEstoqueMariaPlacar.add(mecMariaPlacar1);
        epcMariaPlacar1.setMovimentos(movimentosEstoqueMariaPlacar);
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
    
    protected void criarFuroProduto() {
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

}
