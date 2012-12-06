package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class ResumoEncalheFecharDiaRepositoryImplTest extends AbstractDataUtilRepositoryImplTest {

    @Autowired
    protected ResumoEncalheFecharDiaRepository repository;
    
    private ChamadaEncalhe ceVeja1;
    private ChamadaEncalhe ceSuper1;
    private ChamadaEncalhe ceQuatroRodas1;
  
    private ChamadaEncalheCota ceManoelVeja1;
    private ChamadaEncalheCota ceManoelSuper1;
    private ChamadaEncalheCota ceManoelQuatroRodas1;
    private ChamadaEncalheCota ceJoseVeja1;
    private ChamadaEncalheCota ceJoseSuper1;
    private ChamadaEncalheCota ceJoseQuatroRodas1;
    private ChamadaEncalheCota ceMariaVeja1;
    private ChamadaEncalheCota ceMariaSuper1;
    private ChamadaEncalheCota ceMariaQuatroRodas1;

    private FechamentoEncalhe fechamentoVeja1;
    private FechamentoEncalhe fechamentoQuatroRodas1;
    private FechamentoEncalhe fechamentoSuper1;

    private VendaProduto vendaEncalheVeja1Manoel;
    private VendaProduto vendaEncalheVeja1Maria;
    private VendaProduto vendaEncalheSuper1Manoel;
    
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
        
        Date dataRecolhimento = balancearRecolhimento();
  
        distribuidor.setDataOperacao(dataRecolhimento);
        save(distribuidor);

        conferirEncalhe();
        
        fechamentoEncalhe();
        
        vendaEncalhe();
    }
    
    @Test
    public void testObterDadosGridEncalhe() {
        List<EncalheFecharDiaDTO> resultado = repository.obterDadosGridEncalhe(distribuidor.getDataOperacao(), null);
        Assert.assertNotNull(resultado);
        Assert.assertEquals(3, resultado.size());
        
        EncalheFecharDiaDTO encalheVeja1 = resultado.get(0);
        Assert.assertNotNull(encalheVeja1);
        Assert.assertEquals(produtoVeja.getCodigo(), encalheVeja1.getCodigo());
        Assert.assertEquals(produtoVeja.getNome(), encalheVeja1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoVeja1.getNumeroEdicao(), encalheVeja1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoVeja1.getPrecoVenda().setScale(2), encalheVeja1.getPrecoVenda());
        Assert.assertEquals(BigInteger.valueOf(63), encalheVeja1.getQtdeLogico());
        Assert.assertEquals(BigInteger.valueOf(fechamentoVeja1.getQuantidade()), encalheVeja1.getQtdeFisico());
        BigInteger qtdeVendaEncalheVeja = vendaEncalheVeja1Manoel.getQntProduto().add(vendaEncalheVeja1Maria.getQntProduto());
        Assert.assertEquals(qtdeVendaEncalheVeja, encalheVeja1.getQtdeVendaEncalhe());
        Assert.assertEquals(BigInteger.ZERO, encalheVeja1.getQtdeLogicoJuramentado());
        Assert.assertEquals(BigInteger.ZERO, encalheVeja1.getQtdeDiferenca());
        
        EncalheFecharDiaDTO encalheQuatroRodas1 = resultado.get(1);
        Assert.assertNotNull(encalheQuatroRodas1);
        Assert.assertEquals(produtoQuatroRodas.getCodigo(), encalheQuatroRodas1.getCodigo());
        Assert.assertEquals(produtoQuatroRodas.getNome(), encalheQuatroRodas1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getNumeroEdicao(), encalheQuatroRodas1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getPrecoVenda().setScale(2), encalheQuatroRodas1.getPrecoVenda());
        Assert.assertEquals(BigInteger.valueOf(36), encalheQuatroRodas1.getQtdeLogico());
        Assert.assertEquals(BigInteger.valueOf(fechamentoQuatroRodas1.getQuantidade()), encalheQuatroRodas1.getQtdeFisico());
        Assert.assertEquals(BigInteger.ZERO, encalheQuatroRodas1.getQtdeVendaEncalhe());
        Assert.assertEquals(BigInteger.valueOf(19), encalheQuatroRodas1.getQtdeLogicoJuramentado());
        Assert.assertEquals(BigInteger.ZERO, encalheQuatroRodas1.getQtdeDiferenca());
        
        EncalheFecharDiaDTO encalheSuper1 = resultado.get(2);
        Assert.assertNotNull(encalheSuper1);
        Assert.assertEquals(produtoSuper.getCodigo(), encalheSuper1.getCodigo());
        Assert.assertEquals(produtoSuper.getNome(), encalheSuper1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoSuper1.getNumeroEdicao(), encalheSuper1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoSuper1.getPrecoVenda().setScale(2), encalheSuper1.getPrecoVenda());
        Assert.assertEquals(BigInteger.valueOf(45), encalheSuper1.getQtdeLogico());
        Assert.assertEquals(BigInteger.valueOf(fechamentoSuper1.getQuantidade()), encalheSuper1.getQtdeFisico());
        Assert.assertEquals(vendaEncalheSuper1Manoel.getQntProduto(), encalheSuper1.getQtdeVendaEncalhe());
        Assert.assertEquals(BigInteger.valueOf(17), encalheSuper1.getQtdeLogicoJuramentado());
        Assert.assertEquals(BigInteger.ZERO, encalheSuper1.getQtdeDiferenca());
    }
    
    @Test
    public void testObterDadosGridEncalhePaginado() {
        PaginacaoVO paginacao = new PaginacaoVO(1, 2, null);
        List<EncalheFecharDiaDTO> resultado = repository.obterDadosGridEncalhe(distribuidor.getDataOperacao(), paginacao);
        Assert.assertNotNull(resultado);
        Assert.assertEquals(2, resultado.size());
        
        EncalheFecharDiaDTO encalheVeja1 = resultado.get(0);
        Assert.assertNotNull(encalheVeja1);
        Assert.assertEquals(produtoVeja.getCodigo(), encalheVeja1.getCodigo());
        Assert.assertEquals(produtoVeja.getNome(), encalheVeja1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoVeja1.getNumeroEdicao(), encalheVeja1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoVeja1.getPrecoVenda().setScale(2), encalheVeja1.getPrecoVenda());
        
        EncalheFecharDiaDTO encalheQuatroRodas1 = resultado.get(1);
        Assert.assertNotNull(encalheQuatroRodas1);
        Assert.assertEquals(produtoQuatroRodas.getCodigo(), encalheQuatroRodas1.getCodigo());
        Assert.assertEquals(produtoQuatroRodas.getNome(), encalheQuatroRodas1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getNumeroEdicao(), encalheQuatroRodas1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getPrecoVenda().setScale(2), encalheQuatroRodas1.getPrecoVenda());
        
        paginacao = new PaginacaoVO(2, 2, null);
        resultado = repository.obterDadosGridEncalhe(distribuidor.getDataOperacao(), paginacao);
        Assert.assertNotNull(resultado);
        Assert.assertEquals(1, resultado.size());
        
        EncalheFecharDiaDTO encalheSuper1 = resultado.get(0);
        Assert.assertNotNull(encalheSuper1);
        Assert.assertEquals(produtoSuper.getCodigo(), encalheSuper1.getCodigo());
        Assert.assertEquals(produtoSuper.getNome(), encalheSuper1.getNomeProduto());
        Assert.assertEquals(produtoEdicaoSuper1.getNumeroEdicao(), encalheSuper1.getNumeroEdicao());
        Assert.assertEquals(produtoEdicaoSuper1.getPrecoVenda().setScale(2), encalheSuper1.getPrecoVenda());
    }
    
    @Test
    public void testContarProdutoEdicaoEncalhe() {
        Long resultado = repository.contarProdutoEdicaoEncalhe(distribuidor.getDataOperacao());
        Assert.assertEquals(Long.valueOf(3), resultado);
    }
    
    @Test
    public void testContarProdutoEdicaoEncalheZero() {
        Long resultado = repository.contarProdutoEdicaoEncalhe(DateUtil.adicionarDias(distribuidor.getDataOperacao(), -1));
        Assert.assertEquals(Long.valueOf(0), resultado);
    }
    
    @Test
    public void testObterResumoEncalhe() {
        ResumoEncalheFecharDiaDTO resumo = repository.obterResumoEncalhe(distribuidor.getDataOperacao());
        Assert.assertNotNull(resumo);
        
        Assert.assertEquals(BigDecimal.valueOf(2943).setScale(2), resumo.getTotalLogico().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(2073).setScale(2), resumo.getTotalFisico().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(673.5).setScale(2), resumo.getTotalJuramentado().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(196.5).setScale(2), resumo.getVenda().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(19.5).setScale(2), resumo.getTotalSobras().setScale(2));
        Assert.assertEquals(BigDecimal.ZERO.setScale(2), resumo.getTotalFaltas().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(19.5).setScale(2), resumo.getSaldo().setScale(2));
    }
    
    private Date balancearRecolhimento() {
        lancamentoVeja1.setStatus(StatusLancamento.BALANCEADO_RECOLHIMENTO);
        save(lancamentoVeja1);
        
        lancamentoSuper1.setStatus(StatusLancamento.BALANCEADO_RECOLHIMENTO);
        save(lancamentoSuper1);
        
        lancamentoQuatroRodas1.setStatus(StatusLancamento.BALANCEADO_RECOLHIMENTO);
        save(lancamentoQuatroRodas1);
        
        //Produtos Veja, Super e Quatro rodas tem a mesma data de recolhimento
        Date dataRecolhimento = lancamentoVeja1.getDataRecolhimentoDistribuidor();
        
        criarChamadasEncalhe(dataRecolhimento);
         
        return dataRecolhimento;
    }
    
    private void criarChamadasEncalhe(Date dataRecolhimento) {
        ceVeja1 = Fixture.chamadaEncalhe(dataRecolhimento,
                produtoEdicaoVeja1, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
        
        ceSuper1 = Fixture.chamadaEncalhe(dataRecolhimento,
                produtoEdicaoSuper1, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
        
        ceQuatroRodas1 = Fixture.chamadaEncalhe(dataRecolhimento,
                produtoEdicaoQuatroRodas1, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
        save(ceVeja1, ceSuper1, ceQuatroRodas1);
        
        criarChamadasEncalheCotas();
    }
    
    private void criarChamadasEncalheCotas() {
        ceManoelVeja1 = Fixture.chamadaEncalheCota(ceVeja1, false, cotaManoel, ecManoelVeja1.getQtdeEfetiva());
        save(ceManoelVeja1);
        
        ceManoelSuper1 = Fixture.chamadaEncalheCota(ceSuper1, false, cotaManoel, ecManoelSuper1.getQtdeEfetiva());
        save(ceManoelSuper1);
        
        ceManoelQuatroRodas1 = Fixture.chamadaEncalheCota(ceQuatroRodas1, false, cotaManoel, ecManoelQuatroRodas1.getQtdeEfetiva());
        save(ceManoelQuatroRodas1);
        
        ceJoseVeja1 = Fixture.chamadaEncalheCota(ceVeja1, false, cotaJose, ecJoseVeja1.getQtdeEfetiva());
        save(ceJoseVeja1);
        
        ceJoseSuper1 = Fixture.chamadaEncalheCota(ceSuper1, false, cotaJose, ecJoseSuper1.getQtdeEfetiva());
        save(ceJoseSuper1);
        
        ceJoseQuatroRodas1 = Fixture.chamadaEncalheCota(ceQuatroRodas1, false, cotaJose, ecJoseQuatroRodas1.getQtdeEfetiva());
        save(ceJoseQuatroRodas1);
        
        ceMariaVeja1 = Fixture.chamadaEncalheCota(ceVeja1, false, cotaMaria, ecMariaVeja1.getQtdeEfetiva());
        save(ceMariaVeja1);
        
        ceMariaSuper1 = Fixture.chamadaEncalheCota(ceSuper1, false, cotaMaria, ecMariaSuper1.getQtdeEfetiva());
        save(ceMariaSuper1);
        
        ceMariaQuatroRodas1 = Fixture.chamadaEncalheCota(ceQuatroRodas1, false, cotaMaria, ecMariaQuatroRodas1.getQtdeEfetiva());
        save(ceMariaQuatroRodas1);
    }

    private void conferirEncalhe() {
        ControleConferenciaEncalhe ctrConferencia = Fixture
                .controleConferenciaEncalhe(StatusOperacao.CONCLUIDO,
                        distribuidor.getDataOperacao());
        save(ctrConferencia);
        
        TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
        save(tipoMovimentoEnvioEncalhe);
        
        //CONFERENCIA ENCALHE COTA MANOEL
        ControleConferenciaEncalheCota ctrlConfManoel = Fixture
                .controleConferenciaEncalheCota(ctrConferencia, cotaManoel,
                        distribuidor.getDataOperacao(), distribuidor.getDataOperacao(),
                        distribuidor.getDataOperacao(),
                        StatusOperacao.CONCLUIDO, usuarioJoao, box1);
        
        ConferenciaEncalhe confManoelVeja1 = Fixture.conferenciaEncalhe(null,
                ceManoelVeja1, ctrlConfManoel, distribuidor.getDataOperacao(), ecManoelVeja1
                .getQtdeEfetiva().subtract(BigInteger.TEN),
                ecManoelVeja1.getQtdeEfetiva().subtract(BigInteger.TEN),
                produtoEdicaoVeja1);
        ceManoelVeja1.setFechado(true);
        save(ctrlConfManoel, confManoelVeja1, ceManoelVeja1);
        
        epcManoelVeja1.setQtdeDevolvida(BigInteger.TEN);
        save(epcManoelVeja1);
        
        MovimentoEstoqueCota mecManoelVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcManoelVeja1,
                epcManoelVeja1.getQtdeDevolvida(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelVeja1.getMovimentos().add(mecManoelVeja1);
        save(mecManoelVeja1, epcManoelVeja1);
        
        ConferenciaEncalhe confManoelSuper1 = Fixture.conferenciaEncalhe(null,
                ceManoelSuper1, ctrlConfManoel, distribuidor.getDataOperacao(), ecManoelSuper1
                .getQtdeEfetiva().subtract(BigInteger.ONE),
                ecManoelSuper1.getQtdeEfetiva().subtract(BigInteger.ONE),
                produtoEdicaoSuper1);
        ceManoelSuper1.setFechado(true);
        save(ctrlConfManoel, confManoelSuper1, ceManoelSuper1);
        
        epcManoelSuper1.setQtdeDevolvida(BigInteger.ONE);
        save(epcManoelSuper1);
        
        MovimentoEstoqueCota mecManoelSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcManoelSuper1,
                epcManoelSuper1.getQtdeDevolvida(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelSuper1.getMovimentos().add(mecManoelSuper1);
        save(mecManoelVeja1, epcManoelSuper1);
        
        ConferenciaEncalhe confManoelQuatroRodas1 = Fixture.conferenciaEncalhe(null,
                ceManoelQuatroRodas1, ctrlConfManoel, distribuidor.getDataOperacao(), ecManoelQuatroRodas1
                .getQtdeEfetiva().subtract(BigInteger.ONE),
                ecManoelQuatroRodas1.getQtdeEfetiva().subtract(BigInteger.ONE),
                produtoEdicaoQuatroRodas1);
        confManoelQuatroRodas1.setJuramentada(true);
        ceManoelQuatroRodas1.setFechado(true);
        save(ctrlConfManoel, confManoelQuatroRodas1, ceManoelQuatroRodas1);
        
        epcManoelQuatroRodas1.setQtdeDevolvida(BigInteger.ONE);
        save(epcManoelQuatroRodas1);
        
        MovimentoEstoqueCota mecManoelQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcManoelQuatroRodas1,
                epcManoelQuatroRodas1.getQtdeDevolvida(), cotaManoel,
                StatusAprovacao.APROVADO, "OK");
        epcManoelQuatroRodas1.getMovimentos().add(mecManoelQuatroRodas1);
        save(mecManoelQuatroRodas1, epcManoelQuatroRodas1);
        
        //CONFERENCIA ENCALHE COTA JOSE
        ControleConferenciaEncalheCota ctrlConfJose = Fixture
                .controleConferenciaEncalheCota(ctrConferencia, cotaJose,
                        distribuidor.getDataOperacao(), distribuidor.getDataOperacao(),
                        distribuidor.getDataOperacao(),
                        StatusOperacao.CONCLUIDO, usuarioJoao, box1);
        
        ConferenciaEncalhe confJoseVeja1 = Fixture.conferenciaEncalhe(null,
                ceJoseVeja1, ctrlConfJose, distribuidor.getDataOperacao(), ecJoseVeja1
                .getQtdeEfetiva().subtract(BigInteger.TEN),
                ecJoseVeja1.getQtdeEfetiva().subtract(BigInteger.TEN),
                produtoEdicaoVeja1);
        ceJoseVeja1.setFechado(true);
        save(ctrlConfJose, confJoseVeja1, ceJoseVeja1);
        
        epcJoseVeja1.setQtdeDevolvida(BigInteger.TEN);
        save(epcJoseVeja1);
        
        MovimentoEstoqueCota mecJoseVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcJoseVeja1,
                epcJoseVeja1.getQtdeDevolvida(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJoseVeja1.getMovimentos().add(mecJoseVeja1);
        save(mecJoseVeja1, epcJoseVeja1);
        
        ConferenciaEncalhe confJoseSuper1 = Fixture.conferenciaEncalhe(null,
                ceJoseSuper1, ctrlConfJose, distribuidor.getDataOperacao(), ecJoseSuper1
                .getQtdeEfetiva().subtract(BigInteger.ONE),
                ecJoseSuper1.getQtdeEfetiva().subtract(BigInteger.ONE),
                produtoEdicaoSuper1);
        ceJoseSuper1.setFechado(true);
        save(ctrlConfJose, confJoseSuper1, ceJoseSuper1);
        
        epcJoseSuper1.setQtdeDevolvida(BigInteger.ONE);
        save(epcJoseSuper1);
        
        MovimentoEstoqueCota mecJoseSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcJoseSuper1,
                epcJoseSuper1.getQtdeDevolvida(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJoseSuper1.getMovimentos().add(mecJoseSuper1);
        save(mecJoseSuper1, epcJoseSuper1);
        
        ConferenciaEncalhe confJoseQuatroRodas1 = Fixture.conferenciaEncalhe(null,
                ceJoseQuatroRodas1, ctrlConfJose, distribuidor.getDataOperacao(), ecJoseQuatroRodas1
                .getQtdeEfetiva().subtract(BigInteger.ONE),
                ecJoseQuatroRodas1.getQtdeEfetiva().subtract(BigInteger.ONE),
                produtoEdicaoQuatroRodas1);
        ceJoseQuatroRodas1.setFechado(true);
        save(ctrlConfJose, confJoseQuatroRodas1, ceJoseQuatroRodas1);
        
        epcJoseQuatroRodas1.setQtdeDevolvida(BigInteger.ONE);
        save(epcJoseQuatroRodas1);
        
        MovimentoEstoqueCota mecJoseQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcJoseQuatroRodas1,
                epcJoseQuatroRodas1.getQtdeDevolvida(), cotaJose,
                StatusAprovacao.APROVADO, "OK");
        epcJoseQuatroRodas1.getMovimentos().add(mecJoseQuatroRodas1);
        save(mecJoseQuatroRodas1, epcJoseQuatroRodas1);
        
        //CONFERENCIA ENCALHE COTA MARIA
        ControleConferenciaEncalheCota ctrlConfMaria = Fixture
                .controleConferenciaEncalheCota(ctrConferencia, cotaMaria,
                        distribuidor.getDataOperacao(), distribuidor.getDataOperacao(),
                        distribuidor.getDataOperacao(),
                        StatusOperacao.CONCLUIDO, usuarioJoao, box1);
        
        ConferenciaEncalhe confMariaVeja1 = Fixture.conferenciaEncalhe(null,
                ceMariaVeja1, ctrlConfMaria, distribuidor.getDataOperacao(), ecMariaVeja1
                .getQtdeEfetiva().subtract(BigInteger.valueOf(7)),
                ecMariaVeja1.getQtdeEfetiva().subtract(BigInteger.valueOf(7)),
                produtoEdicaoVeja1);
        ceMariaVeja1.setFechado(true);
        save(ctrlConfMaria, confMariaVeja1, ceMariaVeja1);
        
        epcMariaVeja1.setQtdeDevolvida(BigInteger.valueOf(7));
        save(epcMariaVeja1);
        
        MovimentoEstoqueCota mecMariaVeja1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcMariaVeja1,
                epcMariaVeja1.getQtdeDevolvida(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaVeja1.getMovimentos().add(mecMariaVeja1);
        save(mecMariaVeja1, epcMariaVeja1);
        
        ConferenciaEncalhe confMariaSuper1 = Fixture.conferenciaEncalhe(null,
                ceMariaSuper1, ctrlConfMaria, distribuidor.getDataOperacao(), ecMariaSuper1
                .getQtdeEfetiva().subtract(BigInteger.valueOf(3)),
                ecMariaSuper1.getQtdeEfetiva().subtract(BigInteger.valueOf(3)),
                produtoEdicaoSuper1);
        ceMariaSuper1.setFechado(true);
        confMariaSuper1.setJuramentada(true);
        save(ctrlConfMaria, confMariaSuper1, ceMariaSuper1);
        
        epcMariaSuper1.setQtdeDevolvida(BigInteger.valueOf(3));
        save(epcMariaSuper1);
        
        MovimentoEstoqueCota mecMariaSuper1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoSuper1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcMariaSuper1,
                epcMariaSuper1.getQtdeDevolvida(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaSuper1.getMovimentos().add(mecMariaSuper1);
        save(mecMariaSuper1, epcMariaSuper1);
        
        ConferenciaEncalhe confMariaQuatroRodas1 = Fixture.conferenciaEncalhe(null,
                ceMariaQuatroRodas1, ctrlConfMaria, distribuidor.getDataOperacao(), ecMariaQuatroRodas1
                .getQtdeEfetiva().subtract(BigInteger.valueOf(5)),
                ecMariaQuatroRodas1.getQtdeEfetiva().subtract(BigInteger.valueOf(5)),
                produtoEdicaoQuatroRodas1);
        ceMariaQuatroRodas1.setFechado(true);
        save(ctrlConfMaria, confMariaQuatroRodas1, ceMariaQuatroRodas1);
        
        epcMariaQuatroRodas1.setQtdeDevolvida(BigInteger.valueOf(5));
        save(epcMariaQuatroRodas1);
        
        MovimentoEstoqueCota mecMariaQuatroRodas1 = Fixture.movimentoEstoqueCota(
                produtoEdicaoQuatroRodas1, tipoMovimentoEnvioEncalhe,
                usuarioJoao, epcMariaQuatroRodas1,
                epcMariaQuatroRodas1.getQtdeDevolvida(), cotaMaria,
                StatusAprovacao.APROVADO, "OK");
        epcMariaQuatroRodas1.getMovimentos().add(mecMariaQuatroRodas1);
        save(mecMariaQuatroRodas1, epcMariaQuatroRodas1);
        
        //ENTRADA ENCALHE ESTOQUE DISTRIBUIDOR
        TipoMovimentoEstoque tipoMovimentoRecebimentoEncalhe = Fixture.tipoMovimentoRecebimentoEncalhe();
        save(tipoMovimentoRecebimentoEncalhe);
        
        MovimentoEstoque movimentoEncalheVeja1 = Fixture.movimentoEstoque(null,
                produtoEdicaoVeja1, tipoMovimentoRecebimentoEncalhe, usuarioJoao, estoqueProdutoVeja1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(27), StatusAprovacao.APROVADO, "OK");
        save(movimentoEncalheVeja1);
        
        estoqueProdutoVeja1.setQtdeDevolucaoEncalhe(movimentoEncalheVeja1.getQtde());
        save(estoqueProdutoVeja1);
        
        MovimentoEstoque movimentoEncalheQuatroRodas1 = Fixture.movimentoEstoque(null,
                produtoEdicaoQuatroRodas1, tipoMovimentoRecebimentoEncalhe, usuarioJoao, estoqueProdutoQuatroRodas1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(7), StatusAprovacao.APROVADO, "OK");
        save(movimentoEncalheQuatroRodas1);
        
        estoqueProdutoQuatroRodas1.setQtdeDevolucaoEncalhe(movimentoEncalheQuatroRodas1.getQtde());
        save(estoqueProdutoQuatroRodas1);

        Diferenca diferencaGanhoSuper1 = Fixture.diferenca(BigInteger.valueOf(1), usuarioJoao,
                produtoEdicaoSuper1, TipoDiferenca.SOBRA_EM,
                StatusConfirmacao.CONFIRMADO, null,
                true, TipoEstoque.SUPLEMENTAR, null,
                distribuidor.getDataOperacao());
        LancamentoDiferenca lctoGanhoSuper1 = new LancamentoDiferenca();
       
        lctoGanhoSuper1.setDataProcessamento(distribuidor.getDataOperacao());
        lctoGanhoSuper1.setDiferenca(diferencaGanhoSuper1);
        diferencaGanhoSuper1.setLancamentoDiferenca(lctoGanhoSuper1);
        lctoGanhoSuper1.setMotivo("Motivo");
        lctoGanhoSuper1.setStatus(StatusAprovacao.GANHO);
        
        save(diferencaGanhoSuper1, lctoGanhoSuper1);

        MovimentoEstoque movimentoEncalheSuper1 = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tipoMovimentoRecebimentoEncalhe, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(5), StatusAprovacao.APROVADO, "OK");
        save(movimentoEncalheSuper1);
        
        estoqueProdutoSuper1.setQtdeDevolucaoEncalhe(movimentoEncalheSuper1.getQtde());
        save(estoqueProdutoSuper1);
    }

    private void fechamentoEncalhe() {
        fechamentoVeja1 = new FechamentoEncalhe(produtoEdicaoVeja1, distribuidor.getDataOperacao());
        fechamentoVeja1.setQuantidade(Long.valueOf(56));
        save(fechamentoVeja1);
        
        fechamentoQuatroRodas1 = new FechamentoEncalhe(produtoEdicaoQuatroRodas1, distribuidor.getDataOperacao());
        fechamentoQuatroRodas1.setQuantidade(Long.valueOf(17));
        save(fechamentoQuatroRodas1);
        
        fechamentoSuper1 = new FechamentoEncalhe(produtoEdicaoSuper1, distribuidor.getDataOperacao());
        fechamentoSuper1.setQuantidade(Long.valueOf(26));
        save(fechamentoSuper1);
    }
    
    private void vendaEncalhe() {
        TipoMovimentoEstoque tipoMovimentoVendaEncalhe = Fixture.tipoMovimentoVendaEncalhe();
        save(tipoMovimentoVendaEncalhe);
        
        vendaEncalheVeja1Manoel = new VendaProduto();
        vendaEncalheVeja1Manoel.setCota(cotaManoel);
        vendaEncalheVeja1Manoel.setDataVenda(distribuidor.getDataOperacao());
        vendaEncalheVeja1Manoel.setHorarioVenda(distribuidor.getDataOperacao());
        vendaEncalheVeja1Manoel.setProdutoEdicao(produtoEdicaoVeja1);
        vendaEncalheVeja1Manoel.setQntProduto(BigInteger.valueOf(5));
        vendaEncalheVeja1Manoel.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
        vendaEncalheVeja1Manoel.setTipoVenda(TipoVendaEncalhe.ENCALHE);
        vendaEncalheVeja1Manoel.setUsuario(usuarioJoao);
        vendaEncalheVeja1Manoel.setValorTotalVenda(BigDecimal.valueOf(112.5));
        save(vendaEncalheVeja1Manoel);
        
        vendaEncalheVeja1Maria = new VendaProduto();
        vendaEncalheVeja1Maria.setCota(cotaMaria);
        vendaEncalheVeja1Maria.setDataVenda(distribuidor.getDataOperacao());
        vendaEncalheVeja1Maria.setHorarioVenda(distribuidor.getDataOperacao());
        vendaEncalheVeja1Maria.setProdutoEdicao(produtoEdicaoVeja1);
        vendaEncalheVeja1Maria.setQntProduto(BigInteger.valueOf(2));
        vendaEncalheVeja1Maria.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
        vendaEncalheVeja1Maria.setTipoVenda(TipoVendaEncalhe.ENCALHE);
        vendaEncalheVeja1Maria.setUsuario(usuarioJoao);
        vendaEncalheVeja1Maria.setValorTotalVenda(BigDecimal.valueOf(45));
        save(vendaEncalheVeja1Maria);

        estoqueProdutoVeja1.setQtdeDevolucaoEncalhe(estoqueProdutoVeja1
                .getQtdeDevolucaoEncalhe().subtract(BigInteger.valueOf(7)));
        save(estoqueProdutoVeja1);
        
        MovimentoEstoque movimentoVendaEncalheVeja1Manoel = Fixture.movimentoEstoque(null,
                produtoEdicaoVeja1, tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoVeja1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(5), StatusAprovacao.APROVADO, "OK");
        save(movimentoVendaEncalheVeja1Manoel);
        
        MovimentoEstoque movimentoVendaEncalheVeja1Maria = Fixture.movimentoEstoque(null,
                produtoEdicaoVeja1, tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoVeja1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(2), StatusAprovacao.APROVADO, "OK");
        save(movimentoVendaEncalheVeja1Maria);
                
        vendaEncalheSuper1Manoel = new VendaProduto();
        vendaEncalheSuper1Manoel.setCota(cotaManoel);
        vendaEncalheSuper1Manoel.setDataVenda(distribuidor.getDataOperacao());
        vendaEncalheSuper1Manoel.setHorarioVenda(distribuidor.getDataOperacao());
        vendaEncalheSuper1Manoel.setProdutoEdicao(produtoEdicaoSuper1);
        vendaEncalheSuper1Manoel.setQntProduto(BigInteger.valueOf(2));
        vendaEncalheSuper1Manoel.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
        vendaEncalheSuper1Manoel.setTipoVenda(TipoVendaEncalhe.ENCALHE);
        vendaEncalheSuper1Manoel.setUsuario(usuarioJoao);
        vendaEncalheSuper1Manoel.setValorTotalVenda(BigDecimal.valueOf(39));
        save(vendaEncalheSuper1Manoel);
        
        estoqueProdutoSuper1.setQtdeDevolucaoEncalhe(estoqueProdutoSuper1
                .getQtdeDevolucaoEncalhe().subtract(BigInteger.valueOf(2)));
        save(estoqueProdutoSuper1);
        
        MovimentoEstoque movimentoVendaEncalheSuper1Manoel = Fixture.movimentoEstoque(null,
                produtoEdicaoSuper1, tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoSuper1,
                distribuidor.getDataOperacao(), BigInteger.valueOf(2), StatusAprovacao.APROVADO, "OK");
        save(movimentoVendaEncalheSuper1Manoel);
    }
}
