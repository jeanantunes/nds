package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import br.com.abril.nds.dto.chamadaencalhe.ChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ChamadasEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.IdentificacaoChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ItemChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ItemResumoChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.PessoaJuridicaChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ResumoChamadaEncalheFornecedorDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.FormaDevolucao;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;

public class ContagemDevolucaoServiceImplTest {
    
    private ContagemDevolucaoServiceImpl contagemDevolucaoService;
    
    private DistribuidorService distribuidorService;

    private ChamadaEncalheFornecedorRepository chamadaEncalheFornecedorRepository;

    private Fornecedor fornecedorDinap;
    
    private Distribuidor distribuidor;
    
    private TipoProduto tipoProdutoRevista;

    private ProdutoEdicao produtoEdicaoSuper1;

    private ProdutoEdicao produtoEdicaoInfoExame1;

    private ProdutoEdicao produtoEdicaoQuatroRodas1;

    private ProdutoEdicao produtoEdicaoBoaForma1;

    private ProdutoEdicao produtoEdicaoBravo1;

    private ProdutoEdicao produtoEdicaoCaras1;

    private ProdutoEdicao produtoEdicaoCasaClaudia1;

    private ProdutoEdicao produtoEdicaoContigo1;

    private ProdutoEdicao produtoEdicaoManequim1;
    
    private Editor abril;
    
    private static final  Comparator<ChamadaEncalheFornecedorDTO> COMPARATOR_CE_NUMERO_DOCUMENTO = new Comparator<ChamadaEncalheFornecedorDTO>() {
        @Override
        public int compare(ChamadaEncalheFornecedorDTO o1, ChamadaEncalheFornecedorDTO o2) {
            return o1.getNumeroDocumento().compareTo(o2.getNumeroDocumento());
        }
    };
    
    private static final  Comparator<ItemResumoChamadaEncalheFornecedorDTO> COMPARATOR_ITEM_RESUMO_NUMERO_DOCUMENTO = new Comparator<ItemResumoChamadaEncalheFornecedorDTO>() {

        @Override
        public int compare(ItemResumoChamadaEncalheFornecedorDTO o1, ItemResumoChamadaEncalheFornecedorDTO o2) {
            return o1.getNumeroDocumento().compareTo(o2.getNumeroDocumento());
        }
        
    };
    
    @Before
    public void setUp() {
        abril = Fixture.editoraAbril();
        fornecedorDinap = Fixture.fornecedorDinap(Fixture.tipoFornecedorPublicacao());
        fornecedorDinap.setMargemDistribuidor(BigDecimal.valueOf(9.7));
        distribuidor = Fixture.distribuidor(1, Fixture.pessoaJuridica("Distribuidor Acme",
                "56003315000147", "110042490114", "distrib_acme@mail.com", "99.999-9"), new Date(), null);
        tipoProdutoRevista = Fixture.tipoProduto("Revistas", GrupoProduto.REVISTA, Fixture.ncm(49029000l,"REVISTAS","KG"), "4902.90.00", 001L);
        
        contagemDevolucaoService = Mockito.spy(new ContagemDevolucaoServiceImpl());
        distribuidorService = Mockito.mock(DistribuidorService.class);
        Mockito.when(distribuidorService.obter()).thenReturn(distribuidor);

        chamadaEncalheFornecedorRepository = Mockito.mock(ChamadaEncalheFornecedorRepository.class);
        
        Whitebox.setInternalState(contagemDevolucaoService, "distribuidorService", distribuidorService);
        Whitebox.setInternalState(contagemDevolucaoService, "chamadaEncalheFornecedorRepository", chamadaEncalheFornecedorRepository);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void gerarImpressaoChamadaEncalheFornecedor() {
        List<ChamadaEncalheFornecedor> chamadas = criarChamadasEncalheFornecedor();
        final List<ChamadasEncalheFornecedorDTO> chamadasDTO = new ArrayList<>();
        Mockito.when(chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(fornecedorDinap.getId(), 41, null)).thenReturn(chamadas);
       
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                chamadasDTO.addAll(Collection.class.cast(invocation.getArguments()[0]));
                return null;
            }
        }).when(contagemDevolucaoService).gerarPDFChamadaEncalheFornecedor(Mockito.isA(Collection.class));
        
        contagemDevolucaoService.gerarImpressaoChamadaEncalheFornecedor(fornecedorDinap.getId(), 41, null);
        
        Collections.sort(chamadasDTO, new Comparator<ChamadasEncalheFornecedorDTO>() {
            @Override
            public int compare(ChamadasEncalheFornecedorDTO o1, ChamadasEncalheFornecedorDTO o2) {
               return o1.getIdentificacao().getNumeroCE().compareTo(o2.getIdentificacao().getNumeroCE());
            }
        });
        Assert.assertTrue(chamadasDTO.size() == 2);
        
        Iterator<ChamadasEncalheFornecedorDTO> iterator = chamadasDTO.iterator();
        
        ChamadasEncalheFornecedorDTO chamada1 = iterator.next();
        Assert.assertNotNull(chamada1);
        assertDistribuidor(chamada1);
        assertFornecedor(chamada1);
        assertIdentificacao(chamada1, 119747L);
        
        List<ChamadaEncalheFornecedorDTO> documentosChamada1 = chamada1.getDocumentos();
        Assert.assertTrue(documentosChamada1.size() == 1);
          
        ChamadaEncalheFornecedorDTO documento1Chamada1 = documentosChamada1.get(0);
        Assert.assertNotNull(documento1Chamada1);
        Assert.assertEquals(BigDecimal.valueOf(427.35), documento1Chamada1.getTotalBruto());
        Assert.assertEquals(BigDecimal.valueOf(148.30), documento1Chamada1.getTotalDesconto());
        Assert.assertEquals(BigDecimal.valueOf(279.05), documento1Chamada1.getTotalLiquido());
        Assert.assertEquals(BigDecimal.valueOf(9.7), documento1Chamada1.getMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(41.45).setScale(2), documento1Chamada1.getTotalMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(34.7), documento1Chamada1.getPorcentagemDesconto());
        
        ResumoChamadaEncalheFornecedorDTO resumoChamada1 = chamada1.getResumo();
        Assert.assertNotNull(resumoChamada1);
        Assert.assertEquals(BigDecimal.valueOf(279.05), resumoChamada1.getSubTotalVendas());
        Assert.assertEquals(BigDecimal.ZERO, resumoChamada1.getValorProjetosEspeciais());
        Assert.assertEquals(BigDecimal.valueOf(41.45), resumoChamada1.getTotalMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(279.05), resumoChamada1.getValorPagar());
        
        List<ItemResumoChamadaEncalheFornecedorDTO> itensResumoChamada1 = resumoChamada1.getItens();
        Assert.assertEquals(1, itensResumoChamada1.size());
        ItemResumoChamadaEncalheFornecedorDTO item1ResumoChamada1 = itensResumoChamada1.get(0);
        Assert.assertEquals(Fixture.criarData(11, Calendar.OCTOBER, 2012), item1ResumoChamada1.getDataVencimento());
        Assert.assertEquals(Integer.valueOf(1), item1ResumoChamada1.getLinha());
        Assert.assertEquals(Long.valueOf(119747), item1ResumoChamada1.getNumeroDocumento());
        Assert.assertEquals(BigDecimal.valueOf(279.05), item1ResumoChamada1.getValorDocumento());
        Assert.assertEquals(BigDecimal.valueOf(41.45), item1ResumoChamada1.getValorMargem());
        
        List<ItemChamadaEncalheFornecedorDTO> itensDoc1Cham1 = documento1Chamada1.getItens();
        Assert.assertEquals(3, itensDoc1Cham1.size());
       
        ItemChamadaEncalheFornecedorDTO it1Dc1Ch1 = itensDoc1Cham1.get(0); 
        Assert.assertNotNull(it1Dc1Ch1);
        Assert.assertEquals(produtoEdicaoSuper1.getProduto().getCodigo(), it1Dc1Ch1.getCodigo());
        Assert.assertEquals(produtoEdicaoSuper1.getProduto().getNomeComercial(), it1Dc1Ch1.getDescricao());
        Assert.assertEquals(produtoEdicaoSuper1.getProduto().getNome(), it1Dc1Ch1.getNome());
        Assert.assertNull(it1Dc1Ch1.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.PARCIAL.getCodigo(), it1Dc1Ch1.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoSuper1.getNumeroEdicao(), it1Dc1Ch1.getEdicao());
        Assert.assertEquals(produtoEdicaoSuper1.getProduto().getEditor().getCodigo(), it1Dc1Ch1.getEditor());
        Assert.assertEquals(Integer.valueOf(1), it1Dc1Ch1.getItem());
        Assert.assertNull(it1Dc1Ch1.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoSuper1.getPacotePadrao()), it1Dc1Ch1.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(9.9), it1Dc1Ch1.getPrecoCapa());
        Assert.assertNull(it1Dc1Ch1.getQtdeDevolvida());
        Assert.assertNull(it1Dc1Ch1.getQtdeEnviada());
        Assert.assertNull(it1Dc1Ch1.getQtdeVenda());
        Assert.assertNull(it1Dc1Ch1.getValorVenda());
        
        ItemChamadaEncalheFornecedorDTO it2Dc1Ch1 = itensDoc1Cham1.get(1); 
        Assert.assertNotNull(it2Dc1Ch1);
        Assert.assertEquals(produtoEdicaoInfoExame1.getProduto().getCodigo(), it2Dc1Ch1.getCodigo());
        Assert.assertEquals(produtoEdicaoInfoExame1.getProduto().getNomeComercial(), it2Dc1Ch1.getDescricao());
        Assert.assertEquals(produtoEdicaoInfoExame1.getProduto().getNome(), it2Dc1Ch1.getNome());
        Assert.assertNull(it2Dc1Ch1.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.PARCIAL.getCodigo(), it2Dc1Ch1.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoInfoExame1.getNumeroEdicao(), it2Dc1Ch1.getEdicao());
        Assert.assertEquals(produtoEdicaoInfoExame1.getProduto().getEditor().getCodigo(), it2Dc1Ch1.getEditor());
        Assert.assertEquals(Integer.valueOf(2), it2Dc1Ch1.getItem());
        Assert.assertNull(it2Dc1Ch1.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoInfoExame1.getPacotePadrao()), it2Dc1Ch1.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(29.9), it2Dc1Ch1.getPrecoCapa());
        Assert.assertNull(it2Dc1Ch1.getQtdeDevolvida());
        Assert.assertNull(it2Dc1Ch1.getQtdeEnviada());
        Assert.assertNull(it2Dc1Ch1.getQtdeVenda());
        Assert.assertNull(it2Dc1Ch1.getValorVenda());
        
        ItemChamadaEncalheFornecedorDTO it3Dc1Ch1 = itensDoc1Cham1.get(2); 
        Assert.assertNotNull(it3Dc1Ch1);
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getProduto().getCodigo(), it3Dc1Ch1.getCodigo());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getProduto().getNomeComercial(), it3Dc1Ch1.getDescricao());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getProduto().getNome(), it3Dc1Ch1.getNome());
        Assert.assertEquals(FormaDevolucao.INTEIRO.getDescricao(), it3Dc1Ch1.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.PARCIAL.getCodigo(), it3Dc1Ch1.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getNumeroEdicao(), it3Dc1Ch1.getEdicao());
        Assert.assertEquals(produtoEdicaoQuatroRodas1.getProduto().getEditor().getCodigo(), it3Dc1Ch1.getEditor());
        Assert.assertEquals(Integer.valueOf(3), it3Dc1Ch1.getItem());
        Assert.assertEquals(Long.valueOf(675699), it3Dc1Ch1.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoQuatroRodas1.getPacotePadrao()), it3Dc1Ch1.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(12.95), it3Dc1Ch1.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(67), it3Dc1Ch1.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(100), it3Dc1Ch1.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(33), it3Dc1Ch1.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(427.35), it3Dc1Ch1.getValorVenda());
        
        ChamadasEncalheFornecedorDTO chamada2 = iterator.next();
        Assert.assertNotNull(chamada2);
        assertDistribuidor(chamada2);
        assertFornecedor(chamada2);
        assertIdentificacao(chamada2, 119748L);
        
        
        List<ChamadaEncalheFornecedorDTO> documentosChamada2 = chamada2.getDocumentos();
        Assert.assertTrue(documentosChamada2.size() == 2);
        
        Collections.sort(documentosChamada2, COMPARATOR_CE_NUMERO_DOCUMENTO);
        
        ChamadaEncalheFornecedorDTO documento1Chamada2 = documentosChamada2.get(0);
        Assert.assertNotNull(documento1Chamada2);
        Assert.assertEquals(BigDecimal.valueOf(586.85), documento1Chamada2.getTotalBruto());
        Assert.assertEquals(BigDecimal.valueOf(203.64), documento1Chamada2.getTotalDesconto());
        Assert.assertEquals(BigDecimal.valueOf(383.21), documento1Chamada2.getTotalLiquido());
        Assert.assertEquals(BigDecimal.valueOf(9.7), documento1Chamada2.getMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(56.92).setScale(2), documento1Chamada2.getTotalMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(34.7), documento1Chamada2.getPorcentagemDesconto());

        ResumoChamadaEncalheFornecedorDTO resumoChamada2 = chamada2.getResumo();
        Assert.assertNotNull(resumoChamada2);
        
        Assert.assertEquals(BigDecimal.valueOf(1579.45), resumoChamada2.getSubTotalVendas());
        Assert.assertEquals(BigDecimal.ZERO, resumoChamada2.getValorProjetosEspeciais());
        Assert.assertEquals(BigDecimal.valueOf(234.62), resumoChamada2.getTotalMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(1579.45), resumoChamada2.getValorPagar());
        
        List<ItemResumoChamadaEncalheFornecedorDTO> itensResumoChamada2 = resumoChamada2.getItens();
        Assert.assertEquals(2, itensResumoChamada2.size());
        Collections.sort(itensResumoChamada2, COMPARATOR_ITEM_RESUMO_NUMERO_DOCUMENTO);
        
        ItemResumoChamadaEncalheFornecedorDTO item1ResumoChamada2 = itensResumoChamada2.get(0);
        Assert.assertEquals(Fixture.criarData(11, Calendar.OCTOBER, 2012), item1ResumoChamada2.getDataVencimento());
        Assert.assertEquals(Integer.valueOf(1), item1ResumoChamada2.getLinha());
        Assert.assertEquals(Long.valueOf(119748), item1ResumoChamada2.getNumeroDocumento());
        Assert.assertEquals(BigDecimal.valueOf(383.21), item1ResumoChamada2.getValorDocumento());
        Assert.assertEquals(BigDecimal.valueOf(56.92), item1ResumoChamada2.getValorMargem());
        
        ItemResumoChamadaEncalheFornecedorDTO item2ResumoChamada2 = itensResumoChamada2.get(1);
        Assert.assertEquals(Fixture.criarData(11, Calendar.OCTOBER, 2012), item2ResumoChamada2.getDataVencimento());
        Assert.assertEquals(Integer.valueOf(2), item2ResumoChamada2.getLinha());
        Assert.assertEquals(Long.valueOf(119749), item2ResumoChamada2.getNumeroDocumento());
        Assert.assertEquals(BigDecimal.valueOf(1196.24), item2ResumoChamada2.getValorDocumento());
        Assert.assertEquals(BigDecimal.valueOf(177.7).setScale(2), item2ResumoChamada2.getValorMargem());

        List<ItemChamadaEncalheFornecedorDTO> itensDoc1Cham2 = documento1Chamada2.getItens();
        Assert.assertEquals(3, itensDoc1Cham2.size());
        
        ItemChamadaEncalheFornecedorDTO it1Dc1Ch2 = itensDoc1Cham2.get(0); 
        Assert.assertNotNull(it1Dc1Ch2);
        Assert.assertEquals(produtoEdicaoBoaForma1.getProduto().getCodigo(), it1Dc1Ch2.getCodigo());
        Assert.assertEquals(produtoEdicaoBoaForma1.getProduto().getNomeComercial(), it1Dc1Ch2.getDescricao());
        Assert.assertEquals(produtoEdicaoBoaForma1.getProduto().getNome(), it1Dc1Ch2.getNome());
        Assert.assertEquals(FormaDevolucao.INTEIRO.getDescricao(), it1Dc1Ch2.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.NORMAL.getCodigo(), it1Dc1Ch2.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoBoaForma1.getNumeroEdicao(), it1Dc1Ch2.getEdicao());
        Assert.assertEquals(produtoEdicaoBoaForma1.getProduto().getEditor().getCodigo(), it1Dc1Ch2.getEditor());
        Assert.assertEquals(Integer.valueOf(1), it1Dc1Ch2.getItem());
        Assert.assertEquals(Long.valueOf(679182), it1Dc1Ch2.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoBoaForma1.getPacotePadrao()), it1Dc1Ch2.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(2.99), it1Dc1Ch2.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(85), it1Dc1Ch2.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(140), it1Dc1Ch2.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(55), it1Dc1Ch2.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(164.45), it1Dc1Ch2.getValorVenda());
        
        ItemChamadaEncalheFornecedorDTO it2Dc1Ch2 = itensDoc1Cham2.get(1); 
        Assert.assertNotNull(it2Dc1Ch2);
        Assert.assertEquals(produtoEdicaoBravo1.getProduto().getCodigo(), it2Dc1Ch2.getCodigo());
        Assert.assertEquals(produtoEdicaoBravo1.getProduto().getNomeComercial(), it2Dc1Ch2.getDescricao());
        Assert.assertEquals(produtoEdicaoBravo1.getProduto().getNome(), it2Dc1Ch2.getNome());
        Assert.assertEquals(FormaDevolucao.CAPA.getDescricao(), it2Dc1Ch2.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.FINAL.getCodigo(), it2Dc1Ch2.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoBravo1.getNumeroEdicao(), it2Dc1Ch2.getEdicao());
        Assert.assertEquals(produtoEdicaoBravo1.getProduto().getEditor().getCodigo(), it2Dc1Ch2.getEditor());
        Assert.assertEquals(Integer.valueOf(2), it2Dc1Ch2.getItem());
        Assert.assertEquals(Long.valueOf(583918), it2Dc1Ch2.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoBravo1.getPacotePadrao()), it2Dc1Ch2.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(19.99), it2Dc1Ch2.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(50), it2Dc1Ch2.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(60), it2Dc1Ch2.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(10), it2Dc1Ch2.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(199.9), it2Dc1Ch2.getValorVenda());
        
        ItemChamadaEncalheFornecedorDTO it3Dc1Ch2 = itensDoc1Cham2.get(2); 
        Assert.assertNotNull(it3Dc1Ch2);
        Assert.assertEquals(produtoEdicaoCaras1.getProduto().getCodigo(), it3Dc1Ch2.getCodigo());
        Assert.assertEquals(produtoEdicaoCaras1.getProduto().getNomeComercial(), it3Dc1Ch2.getDescricao());
        Assert.assertEquals(produtoEdicaoCaras1.getProduto().getNome(), it3Dc1Ch2.getNome());
        Assert.assertEquals(FormaDevolucao.INTEIRO.getDescricao(), it3Dc1Ch2.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.NORMAL.getCodigo(), it3Dc1Ch2.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoCaras1.getNumeroEdicao(), it3Dc1Ch2.getEdicao());
        Assert.assertEquals(produtoEdicaoCaras1.getProduto().getEditor().getCodigo(), it3Dc1Ch2.getEditor());
        Assert.assertEquals(Integer.valueOf(3), it3Dc1Ch2.getItem());
        Assert.assertEquals(Long.valueOf(688743), it3Dc1Ch2.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoCaras1.getPacotePadrao()), it3Dc1Ch2.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(8.9), it3Dc1Ch2.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(65), it3Dc1Ch2.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(90), it3Dc1Ch2.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(25), it3Dc1Ch2.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(222.5), it3Dc1Ch2.getValorVenda());
        
        ChamadaEncalheFornecedorDTO documento2Chamada2 = documentosChamada2.get(1);
        Assert.assertNotNull(documento2Chamada2);
        Assert.assertEquals(BigDecimal.valueOf(1831.91), documento2Chamada2.getTotalBruto());
        Assert.assertEquals(BigDecimal.valueOf(635.67), documento2Chamada2.getTotalDesconto());
        Assert.assertEquals(BigDecimal.valueOf(1196.24), documento2Chamada2.getTotalLiquido());
        Assert.assertEquals(BigDecimal.valueOf(9.7), documento2Chamada2.getMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(177.70).setScale(2), documento2Chamada2.getTotalMargemDistribuidor());
        Assert.assertEquals(BigDecimal.valueOf(34.7), documento2Chamada2.getPorcentagemDesconto());
        
        List<ItemChamadaEncalheFornecedorDTO> itensDoc2Cham2 = documento2Chamada2.getItens();
        Assert.assertEquals(3, itensDoc2Cham2.size());
        
        ItemChamadaEncalheFornecedorDTO it1Dc2Ch2 = itensDoc2Cham2.get(0); 
        Assert.assertNotNull(it1Dc2Ch2);
        Assert.assertEquals(produtoEdicaoCasaClaudia1.getProduto().getCodigo(), it1Dc2Ch2.getCodigo());
        Assert.assertEquals(produtoEdicaoCasaClaudia1.getProduto().getNomeComercial(), it1Dc2Ch2.getDescricao());
        Assert.assertEquals(produtoEdicaoCasaClaudia1.getProduto().getNome(), it1Dc2Ch2.getNome());
        Assert.assertEquals(FormaDevolucao.INTEIRO.getDescricao(), it1Dc2Ch2.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.NORMAL.getCodigo(), it1Dc2Ch2.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoCasaClaudia1.getNumeroEdicao(), it1Dc2Ch2.getEdicao());
        Assert.assertEquals(produtoEdicaoCasaClaudia1.getProduto().getEditor().getCodigo(), it1Dc2Ch2.getEditor());
        Assert.assertEquals(Integer.valueOf(1), it1Dc2Ch2.getItem());
        Assert.assertEquals(Long.valueOf(672613), it1Dc2Ch2.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoCasaClaudia1.getPacotePadrao()), it1Dc2Ch2.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(2.99), it1Dc2Ch2.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(108), it1Dc2Ch2.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(140), it1Dc2Ch2.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(32), it1Dc2Ch2.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(95.68), it1Dc2Ch2.getValorVenda());
        
        ItemChamadaEncalheFornecedorDTO it2Dc2Ch2 = itensDoc2Cham2.get(1); 
        Assert.assertNotNull(it2Dc2Ch2);
        Assert.assertEquals(produtoEdicaoContigo1.getProduto().getCodigo(), it2Dc2Ch2.getCodigo());
        Assert.assertEquals(produtoEdicaoContigo1.getProduto().getNomeComercial(), it2Dc2Ch2.getDescricao());
        Assert.assertEquals(produtoEdicaoContigo1.getProduto().getNome(), it2Dc2Ch2.getNome());
        Assert.assertEquals(FormaDevolucao.CAP_BR.getDescricao(), it2Dc2Ch2.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.NORMAL.getCodigo(), it2Dc2Ch2.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoContigo1.getNumeroEdicao(), it2Dc2Ch2.getEdicao());
        Assert.assertEquals(produtoEdicaoContigo1.getProduto().getEditor().getCodigo(), it2Dc2Ch2.getEditor());
        Assert.assertEquals(Integer.valueOf(2), it2Dc2Ch2.getItem());
        Assert.assertEquals(Long.valueOf(698621), it2Dc2Ch2.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoContigo1.getPacotePadrao()), it2Dc2Ch2.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(4.99), it2Dc2Ch2.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(245), it2Dc2Ch2.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(492), it2Dc2Ch2.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(247), it2Dc2Ch2.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(1232.53), it2Dc2Ch2.getValorVenda());
        
        ItemChamadaEncalheFornecedorDTO it3Dc2Ch2 = itensDoc2Cham2.get(2); 
        Assert.assertNotNull(it3Dc2Ch2);
        Assert.assertEquals(produtoEdicaoManequim1.getProduto().getCodigo(), it3Dc2Ch2.getCodigo());
        Assert.assertEquals(produtoEdicaoManequim1.getProduto().getNomeComercial(), it3Dc2Ch2.getDescricao());
        Assert.assertEquals(produtoEdicaoManequim1.getProduto().getNome(), it3Dc2Ch2.getNome());
        Assert.assertEquals(FormaDevolucao.INT_BR.getDescricao(), it3Dc2Ch2.getFormaDevolucao());
        Assert.assertEquals(RegimeRecolhimento.NORMAL.getCodigo(), it3Dc2Ch2.getTipoRecolhimento());
        Assert.assertEquals(produtoEdicaoManequim1.getNumeroEdicao(), it3Dc2Ch2.getEdicao());
        Assert.assertEquals(produtoEdicaoManequim1.getProduto().getEditor().getCodigo(), it3Dc2Ch2.getEditor());
        Assert.assertEquals(Integer.valueOf(3), it3Dc2Ch2.getItem());
        Assert.assertEquals(Long.valueOf(692359), it3Dc2Ch2.getNotaEnvio());
        Assert.assertEquals(Integer.valueOf(produtoEdicaoManequim1.getPacotePadrao()), it3Dc2Ch2.getPacotePadrao());
        Assert.assertEquals(BigDecimal.valueOf(21.9), it3Dc2Ch2.getPrecoCapa());
        Assert.assertEquals(Long.valueOf(7), it3Dc2Ch2.getQtdeDevolvida());
        Assert.assertEquals(Long.valueOf(30), it3Dc2Ch2.getQtdeEnviada());
        Assert.assertEquals(Long.valueOf(23), it3Dc2Ch2.getQtdeVenda());
        Assert.assertEquals(BigDecimal.valueOf(503.7), it3Dc2Ch2.getValorVenda());
        
        Mockito.verify(contagemDevolucaoService).gerarPDFChamadaEncalheFornecedor(Mockito.isA(Collection.class));
        Mockito.verify(chamadaEncalheFornecedorRepository).obterChamadasEncalheFornecedor(fornecedorDinap.getId(), 41, null);
    }
    

    private void assertFornecedor(ChamadasEncalheFornecedorDTO chamadaDTO1) {
       PessoaJuridicaChamadaEncalheFornecedorDTO fornecedorDTO = chamadaDTO1.getFornecedor();
       Assert.assertEquals(fornecedorDinap.getJuridica().getRazaoSocial(), fornecedorDTO.getRazaoSocial());
       Assert.assertEquals(fornecedorDinap.getJuridica().getCnpj(), fornecedorDTO.getCnpj());
       Assert.assertEquals(fornecedorDinap.getJuridica().getInscricaoEstadual(), fornecedorDTO.getInscricaoEstadual());
    }
    
    private void assertDistribuidor(ChamadasEncalheFornecedorDTO chamadaDTO1) {
        PessoaJuridicaChamadaEncalheFornecedorDTO distribuidorDTO = chamadaDTO1.getDistribuidor();
        Assert.assertEquals(distribuidor.getJuridica().getRazaoSocial(), distribuidorDTO.getRazaoSocial());
        Assert.assertEquals(distribuidor.getJuridica().getCnpj(), distribuidorDTO.getCnpj());
        Assert.assertEquals(distribuidor.getJuridica().getInscricaoEstadual(), distribuidorDTO.getInscricaoEstadual());
     }

    private void assertIdentificacao(ChamadasEncalheFornecedorDTO chamadaDTO1, Long numeroCE) {
        IdentificacaoChamadaEncalheFornecedorDTO identificacaoDTO = chamadaDTO1.getIdentificacao();
        Assert.assertEquals(Long.valueOf(2678001), identificacaoDTO.getCodigo());
        Assert.assertEquals("", identificacaoDTO.getCodigoNaturezaOperacao());
        Assert.assertEquals("", identificacaoDTO.getDescricaoNaturezaOperacao());
        Assert.assertEquals(Long.valueOf(23773628), identificacaoDTO.getControle());
        Assert.assertEquals(Fixture.criarData(27, Calendar.SEPTEMBER, 2012), identificacaoDTO.getDataEmissao());
        Assert.assertEquals(Fixture.criarData(17, Calendar.OCTOBER, 2012), identificacaoDTO.getDataLimiteChegada());
        Assert.assertEquals(Fixture.criarData(11, Calendar.OCTOBER, 2012), identificacaoDTO.getDataVencimento());
        Assert.assertEquals(numeroCE, identificacaoDTO.getNumeroCE());
        Assert.assertEquals(Integer.valueOf(41), identificacaoDTO.getNumeroSemana());
        Assert.assertEquals(Integer.valueOf(11), identificacaoDTO.getTipoCE());
    }

    private List<ChamadaEncalheFornecedor> criarChamadasEncalheFornecedor() {
        List<ChamadaEncalheFornecedor> chamadas = new ArrayList<>();
        
        ChamadaEncalheFornecedor cef1 = Fixture.newChamadaEncalheFornecedor(
                2678001L,
                fornecedorDinap,
                119747L, 23773628L, 11, 2012, 41,
                Fixture.criarData(17, Calendar.OCTOBER, 2012),
                Fixture.criarData(27, Calendar.SEPTEMBER, 2012),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(148.30),
                BigDecimal.valueOf(148.30), BigDecimal.valueOf(427.35),
                BigDecimal.valueOf(427.35), "F", "M", BigDecimal.ZERO, Fixture.criarData(11, Calendar.OCTOBER, 2012));
        chamadas.add(cef1);
        
        produtoEdicaoSuper1 = Fixture.produtoEdicao("36341001", 1L, 10, 14,
                new Long(100), BigDecimal.valueOf(7), BigDecimal.valueOf(9.9),
                "115", Fixture.produtoSuperInteressante(tipoProdutoRevista), null, false, "Super Int. 1");
        produtoEdicaoSuper1.getProduto().setEditor(abril);

        Fixture.newItemChamadaEncalheFornecedor(cef1, produtoEdicaoSuper1, 37, 119747L, 1,
                null, BigDecimal.valueOf(9.9), null,
                RegimeRecolhimento.PARCIAL, BigDecimal.ZERO, null, null, null,
                null, null, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        produtoEdicaoInfoExame1 = Fixture.produtoEdicao("29315013", 1L, 12, 30,
                new Long(250), new BigDecimal(25), BigDecimal.valueOf(29.9),
                "117", Fixture.produtoInfoExame(tipoProdutoRevista), null, false, "Info Exame 1");
        produtoEdicaoInfoExame1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef1, produtoEdicaoInfoExame1, 74, 119747L, 2,
                null, BigDecimal.valueOf(29.9), null,
                RegimeRecolhimento.PARCIAL, BigDecimal.ZERO, null, null, null,
                null, null, Fixture.criarData(5, Calendar.OCTOBER, 2012), "P", "P", null);
        
        produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao("5198", 1L, 7, 30,
                new Long(300), new BigDecimal(10), BigDecimal.valueOf(12.95),
                "118", Fixture.produtoQuatroRodas(tipoProdutoRevista), null, false, "Quatro Rodas 1");
        produtoEdicaoQuatroRodas1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef1, produtoEdicaoQuatroRodas1, 11, 119747L, 3,
                100L, BigDecimal.valueOf(12.95), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.PARCIAL, BigDecimal.valueOf(148.30), 675699L, 33L, BigDecimal.valueOf(427.35),
                BigDecimal.valueOf(427.35), 67L, Fixture.criarData(5, Calendar.OCTOBER, 2012), "P", "P", null);
        
        
        ChamadaEncalheFornecedor cef2 = Fixture.newChamadaEncalheFornecedor(
                2678001L,
                fornecedorDinap,
                119748L, 23773628L, 11, 2012, 41,
                Fixture.criarData(17, Calendar.OCTOBER, 2012),
                Fixture.criarData(27, Calendar.SEPTEMBER, 2012),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(839.31),
                BigDecimal.valueOf(839.31), BigDecimal.valueOf(2418.76),
                BigDecimal.valueOf(2418.76), "F", "M", BigDecimal.ZERO, Fixture.criarData(11, Calendar.OCTOBER, 2012));
        chamadas.add(cef2);
        
        produtoEdicaoBoaForma1 = Fixture.produtoEdicao("21171001", 1L, 10, 30,
                new Long(100), new BigDecimal(1.5), BigDecimal.valueOf(2.99),
                "119", Fixture.produtoBoaForma(tipoProdutoRevista), null, false, "Boa Forma 1");
        produtoEdicaoBoaForma1.getProduto().setEditor(abril);
        
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoBoaForma1, 85, 119748L, 1,
                140L, BigDecimal.valueOf(2.99), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(57.06), 679182L, 55L, BigDecimal.valueOf(164.45),
                BigDecimal.valueOf(164.45), 85L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        produtoEdicaoBravo1 = Fixture.produtoEdicao("COD_10", 1L, 10, 30,
                new Long(120), BigDecimal.valueOf(17), BigDecimal.valueOf(19.99),
                "119", Fixture.produtoBravo(tipoProdutoRevista), null, false, "Bravo 1");
        produtoEdicaoBravo1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoBravo1, 96, 119748L, 2,
                60L, BigDecimal.valueOf(19.99), FormaDevolucao.CAPA,
                RegimeRecolhimento.FINAL, BigDecimal.valueOf(69.37), 583918L, 10L, BigDecimal.valueOf(199.9),
                BigDecimal.valueOf(199.9), 50L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        produtoEdicaoCaras1 = Fixture.produtoEdicao("36168001", 1L, 15, 30,
                new Long(200), BigDecimal.valueOf(7), BigDecimal.valueOf(8.9),
                "120", Fixture.produtoCaras(tipoProdutoRevista), null, false, "Caras 1");
        produtoEdicaoCaras1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoCaras1, 18, 119748L, 3,
                90L, BigDecimal.valueOf(8.9), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(77.21), 688743L, 25L, BigDecimal.valueOf(222.5),
                BigDecimal.valueOf(222.5), 65L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
         produtoEdicaoCasaClaudia1 = Fixture.produtoEdicao("24664001", 1L, 10, 30,
                new Long(200),  BigDecimal.valueOf(2),  BigDecimal.valueOf(2.99),
                "121", Fixture.produtoCasaClaudia(tipoProdutoRevista), null, false, "Casa Claudia 1");
         produtoEdicaoCasaClaudia1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoCasaClaudia1, 66, 119749L, 1,
                140L, BigDecimal.valueOf(2.99), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(33.20), 672613L, 32L, BigDecimal.valueOf(95.68),
                BigDecimal.valueOf(95.68), 108L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        produtoEdicaoContigo1 = Fixture.produtoEdicao("25745001", 1L, 10, 30,
                new Long(100), BigDecimal.valueOf(3), BigDecimal.valueOf(4.99),
                "123", Fixture.produtoContigo(tipoProdutoRevista), null, false,"Contigo 1");
        produtoEdicaoContigo1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoContigo1, 29, 119749L, 2,
                492L, BigDecimal.valueOf(4.99), FormaDevolucao.CAP_BR,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(427.69), 698621L, 247L, BigDecimal.valueOf(1232.53),
                BigDecimal.valueOf(1232.53), 245L, Fixture.criarData(4, Calendar.OCTOBER, 2012), "P", "P", null);
        
        produtoEdicaoManequim1 = Fixture.produtoEdicao("111", 1L, 10, 30,
                new Long(100), BigDecimal.valueOf(15), BigDecimal.valueOf(21.9),
                "124", Fixture.produtoManequim(tipoProdutoRevista), null, false, "Manequim 1");
        produtoEdicaoManequim1.getProduto().setEditor(abril);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoManequim1, 40, 119749L, 3,
                30L, BigDecimal.valueOf(21.9), FormaDevolucao.INT_BR,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(174.78), 692359L, 23L, BigDecimal.valueOf(503.7),
                BigDecimal.valueOf(503.7), 7L, Fixture.criarData(4, Calendar.OCTOBER, 2012), "P", "P", null);
        
        return chamadas;
    }
 
}
