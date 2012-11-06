package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.DividaRepository;

public class DividaServiceImplTest {
    
    private Distribuidor distribuidor;
    
    private DividaRepository dividaRepository;
    
    private DistribuidorService distribuidorService;
    
    private DividaServiceImpl dividaService;
    
    @Before
    public void setUp() {
        distribuidor = Fixture.distribuidor(1, Fixture.pessoaJuridica("Distribuidor Acme",
                "56003315000147", "110042490114", "distrib_acme@mail.com", "99.999-9"), new Date(), null);
        
        Banco hsbc = Fixture.banco(454L, true, 1, "1010",
                164L, "1", "6", "Instrucoes HSBC.", "HSBC","BANCO HSBC S/A", "399", BigDecimal.TEN, BigDecimal.ZERO);
        
        FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, hsbc,
                BigDecimal.ONE, BigDecimal.TEN, null);

        FormaCobranca formaCheque = Fixture.formaCobrancaCheque(true, BigDecimal.ONE, true, null,
                BigDecimal.ONE, BigDecimal.TEN, null);
        
        FormaCobranca formaDinheiro = Fixture.formaCobrancaDinheiro(true, BigDecimal.ONE, true, null,
                BigDecimal.ONE, BigDecimal.TEN, null);
        
        FormaCobranca formaDeposito = Fixture.formaCobrancaDeposito(true, BigDecimal.ONE, true, null,
                BigDecimal.ONE, BigDecimal.TEN, null);
        
        Set<PoliticaCobranca> politicas = new HashSet<>();
        PoliticaCobranca politicaCobrancaBoleto = Fixture
                .criarPoliticaCobranca(distribuidor, formaBoleto, true, false,
                        false, 1, null, null, true, FormaEmissao.EM_MASSA);
        politicas.add(politicaCobrancaBoleto);

        PoliticaCobranca politicaCobrancaCheque = Fixture
                .criarPoliticaCobranca(distribuidor, formaCheque, true, false,
                        true, 1, null, null, false, FormaEmissao.PONTUAL);
        politicas.add(politicaCobrancaCheque);

        PoliticaCobranca politicaCobrancaDinheiro = Fixture
                .criarPoliticaCobranca(distribuidor, formaDinheiro, true,
                        false, true, 1, null, null, false, FormaEmissao.PONTUAL);
        politicas.add(politicaCobrancaDinheiro);
        
        PoliticaCobranca politicaCobrancaDeposito = Fixture
                .criarPoliticaCobranca(distribuidor, formaDeposito, true,
                        false, true, 1, null, null, false, FormaEmissao.PONTUAL);
        politicas.add(politicaCobrancaDeposito);
        
        distribuidor.setPoliticasCobranca(politicas);
        
        dividaRepository = Mockito.mock(DividaRepository.class);
        distribuidorService = Mockito.mock(DistribuidorService.class);
        Mockito.when(distribuidorService.obter()).thenReturn(distribuidor);
        
        
        dividaService = new DividaServiceImpl();
        Whitebox.setInternalState(dividaService, "dividaRepository", dividaRepository);
        Whitebox.setInternalState(dividaService, "distribuidorService", distribuidorService);
        
    }
    
    @Test
    public void testSumarizacaoDividasReceberEm() {
        Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao = new HashMap<>();
        
        SumarizacaoDividasDTO sumarizacaoBoleto = new SumarizacaoDividasDTO(
                distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_RECEBER,
                TipoCobranca.BOLETO, BigDecimal.valueOf(1000),
                BigDecimal.valueOf(900), BigDecimal.valueOf(100));
        sumarizacao.put(TipoCobranca.BOLETO, sumarizacaoBoleto);

        SumarizacaoDividasDTO sumarizacaoCheque = new SumarizacaoDividasDTO(
                distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_RECEBER,
                TipoCobranca.CHEQUE, BigDecimal.valueOf(500),
                BigDecimal.valueOf(200), BigDecimal.valueOf(300));
        sumarizacao.put(TipoCobranca.CHEQUE, sumarizacaoCheque);

        SumarizacaoDividasDTO sumarizacaoDinheiro = new SumarizacaoDividasDTO(
                distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_RECEBER,
                TipoCobranca.DINHEIRO, BigDecimal.valueOf(2000),
                BigDecimal.valueOf(1500), BigDecimal.valueOf(500));
        sumarizacao.put(TipoCobranca.DINHEIRO, sumarizacaoDinheiro);

        SumarizacaoDividasDTO sumarizacaoTransferencia = new SumarizacaoDividasDTO(
                distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_RECEBER,
                TipoCobranca.TRANSFERENCIA_BANCARIA, BigDecimal.valueOf(200),
                BigDecimal.valueOf(200), BigDecimal.ZERO);
        sumarizacao.put(TipoCobranca.TRANSFERENCIA_BANCARIA,
                sumarizacaoTransferencia);
        
        Mockito.when(dividaRepository.sumarizacaoDividasReceberEm(distribuidor.getDataOperacao())).thenReturn(sumarizacao);
        
        List<SumarizacaoDividasDTO> retorno = dividaService.sumarizacaoDividasReceberEm(distribuidor.getDataOperacao());
        Assert.assertEquals(5, retorno.size());
        
        assertSumarizacao(sumarizacaoBoleto, retorno.get(0));
        assertSumarizacao(sumarizacaoCheque, retorno.get(1));
        assertSumarizacao(new SumarizacaoDividasDTO(distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_RECEBER, TipoCobranca.DEPOSITO), retorno.get(2));
        assertSumarizacao(sumarizacaoTransferencia, retorno.get(3));
        assertSumarizacao(sumarizacaoDinheiro, retorno.get(4));
        
        Mockito.verify(distribuidorService).obter();
        Mockito.verify(dividaRepository).sumarizacaoDividasReceberEm(distribuidor.getDataOperacao());
    }
    
    
    @Test
    public void testSumarizacaoDividasVencerApos() {
        Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao = new HashMap<>();
        
        SumarizacaoDividasDTO sumarizacaoBoleto = new SumarizacaoDividasDTO(
                distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_VENCER,
                TipoCobranca.BOLETO, BigDecimal.valueOf(1000),
                BigDecimal.valueOf(900), BigDecimal.valueOf(100));
        sumarizacao.put(TipoCobranca.BOLETO, sumarizacaoBoleto);

        SumarizacaoDividasDTO sumarizacaoDinheiro = new SumarizacaoDividasDTO(
                distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_VENCER,
                TipoCobranca.DINHEIRO, BigDecimal.valueOf(2000),
                BigDecimal.valueOf(1500), BigDecimal.valueOf(500));
        sumarizacao.put(TipoCobranca.DINHEIRO, sumarizacaoDinheiro);

        Mockito.when(dividaRepository.sumarizacaoDividasVencerApos(distribuidor.getDataOperacao())).thenReturn(sumarizacao);
        
        List<SumarizacaoDividasDTO> retorno = dividaService.sumarizacaoDividasVencerApos(distribuidor.getDataOperacao());
        Assert.assertEquals(4, retorno.size());
        
        assertSumarizacao(sumarizacaoBoleto, retorno.get(0));
        assertSumarizacao(new SumarizacaoDividasDTO(distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_VENCER, TipoCobranca.CHEQUE), retorno.get(1));
        assertSumarizacao(new SumarizacaoDividasDTO(distribuidor.getDataOperacao(), TipoDivida.DIVIDA_A_VENCER, TipoCobranca.DEPOSITO), retorno.get(2));
        assertSumarizacao(sumarizacaoDinheiro, retorno.get(3));
        
        Mockito.verify(distribuidorService).obter();
        Mockito.verify(dividaRepository).sumarizacaoDividasVencerApos(distribuidor.getDataOperacao());
    }
    
    private void assertSumarizacao(SumarizacaoDividasDTO expected, SumarizacaoDividasDTO actual) {
        Assert.assertEquals(expected.getDescricaoTipoCobranca(), actual.getDescricaoTipoCobranca());
        Assert.assertEquals(expected.getData(), actual.getData());
        Assert.assertEquals(expected.getInadimplencia(), actual.getInadimplencia());
        Assert.assertEquals(expected.getInadimplenciaFormatado(), actual.getInadimplenciaFormatado());
        Assert.assertEquals(expected.getTipoCobranca(), actual.getTipoCobranca());
        Assert.assertEquals(expected.getTipoSumarizacao(), actual.getTipoSumarizacao());
        Assert.assertEquals(expected.getTotal(), actual.getTotal());
        Assert.assertEquals(expected.getTotalFormatado(), actual.getTotalFormatado());
        Assert.assertEquals(expected.getValorPago(), actual.getValorPago());
        Assert.assertEquals(expected.getValorPagoFormatado(), actual.getValorPagoFormatado());
    }

}
