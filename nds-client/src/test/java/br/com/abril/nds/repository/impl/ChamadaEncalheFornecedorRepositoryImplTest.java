package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.FormaDevolucao;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.util.Intervalo;

public class ChamadaEncalheFornecedorRepositoryImplTest extends
        AbstractRepositoryImplTest {

    @Autowired
    private ChamadaEncalheFornecedorRepository repository;

    private Fornecedor fornecedorDinap;
    
    private Fornecedor fornecedorFC;

    private ChamadaEncalheFornecedor cef1;

    private ChamadaEncalheFornecedor cef2;
   
   
    @Before
    public void setUp() {
        Editor abril = Fixture.editoraAbril();
        save(abril);
        
        TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
        save(tipoFornecedorPublicacao);
        
        fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
        fornecedorDinap.setMargemDistribuidor(BigDecimal.valueOf(9.7));
        save(fornecedorDinap);
        
        fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
        fornecedorFC.setMargemDistribuidor(BigDecimal.valueOf(9.7));
        save(fornecedorFC);
        
        PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("Distribuidor Acme",
                "56003315000147", "110042490114", "distrib_acme@mail.com", "99.999-9");
        save(pessoaJuridica);
        
        Distribuidor distribuidor = Fixture.distribuidor(1, pessoaJuridica, new Date(), null);
        save(distribuidor);
        
        NCM ncm = Fixture.ncm(49029000l,"REVISTAS","KG");
        save(ncm);
        
        TipoProduto tipoProdutoRevista = Fixture.tipoProduto("Revistas", GrupoProduto.REVISTA, ncm, "4902.90.00", 001L);
        save(tipoProdutoRevista);
        
        cef1 = Fixture.newChamadaEncalheFornecedor(
                2678001L,
                fornecedorFC,
                119747L, 23773628L, 11, 2012, 41,
                Fixture.criarData(17, Calendar.OCTOBER, 2012),
                Fixture.criarData(27, Calendar.SEPTEMBER, 2012),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(148.30),
                BigDecimal.valueOf(148.30), BigDecimal.valueOf(427.35),
                BigDecimal.valueOf(427.35), "F", "M", BigDecimal.ZERO, Fixture.criarData(11, Calendar.OCTOBER, 2012));

        
        Produto produtoSuperInteressante = Fixture.produtoSuperInteressante(tipoProdutoRevista);
        produtoSuperInteressante.setEditor(abril);
        produtoSuperInteressante.addFornecedor(fornecedorFC);
        save(produtoSuperInteressante);
        
        ProdutoEdicao produtoEdicaoSuper1 = Fixture.produtoEdicao("36341001", 1L, 10, 14, new Long(100), BigDecimal.valueOf(7), BigDecimal.valueOf(9.9),
                "115", produtoSuperInteressante, null, false, "Super Int. 1");
        save(produtoEdicaoSuper1);

        Fixture.newItemChamadaEncalheFornecedor(cef1, produtoEdicaoSuper1, 37, 119747L, 1,
                0L, BigDecimal.valueOf(9.9), null,
                RegimeRecolhimento.PARCIAL, BigDecimal.ZERO, 0L, null, null,
                null, null, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoInfoExame = Fixture.produtoInfoExame(tipoProdutoRevista);
        produtoInfoExame.setEditor(abril);
        produtoInfoExame.addFornecedor(fornecedorFC);
        save(produtoInfoExame);
        
        ProdutoEdicao produtoEdicaoInfoExame1 = Fixture.produtoEdicao("29315013", 1L, 12, 30,
                new Long(250), new BigDecimal(25), BigDecimal.valueOf(29.9),
                "117", produtoInfoExame, null, false, "Info Exame 1");
        save(produtoEdicaoInfoExame1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef1, produtoEdicaoInfoExame1, 74, 119747L, 2,
                0L, BigDecimal.valueOf(29.9), null,
                RegimeRecolhimento.PARCIAL, BigDecimal.ZERO, 0L, null, null,
                null, null, Fixture.criarData(5, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
        produtoQuatroRodas.setEditor(abril);
        produtoQuatroRodas.addFornecedor(fornecedorFC);
        save(produtoQuatroRodas);
        
        ProdutoEdicao produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao("5198", 1L, 7, 30,
                new Long(300), new BigDecimal(10), BigDecimal.valueOf(12.95),
                "118", produtoQuatroRodas, null, false, "Quatro Rodas 1");
        save(produtoEdicaoQuatroRodas1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef1, produtoEdicaoQuatroRodas1, 11, 119747L, 3,
                100L, BigDecimal.valueOf(12.95), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.PARCIAL, BigDecimal.valueOf(148.30), 675699L, 33L, BigDecimal.valueOf(427.35),
                BigDecimal.valueOf(427.35), 67L, Fixture.criarData(5, Calendar.OCTOBER, 2012), "P", "P", null);
        
        
        cef2 = Fixture.newChamadaEncalheFornecedor(
                2678001L,
                fornecedorDinap,
                119748L, 23773628L, 11, 2012, 41,
                Fixture.criarData(17, Calendar.OCTOBER, 2012),
                Fixture.criarData(27, Calendar.SEPTEMBER, 2012),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(839.31),
                BigDecimal.valueOf(839.31), BigDecimal.valueOf(2418.76),
                BigDecimal.valueOf(2418.76), "F", "M", BigDecimal.ZERO, Fixture.criarData(11, Calendar.OCTOBER, 2012));

        
        Produto produtoBoaForma = Fixture.produtoBoaForma(tipoProdutoRevista);
        produtoBoaForma.setEditor(abril);
        produtoBoaForma.addFornecedor(fornecedorDinap);
        save(produtoBoaForma);
        
        ProdutoEdicao produtoEdicaoBoaForma1 = Fixture.produtoEdicao("21171001", 1L, 10, 30,
                new Long(100), new BigDecimal(1.5), BigDecimal.valueOf(2.99),
                "119", produtoBoaForma, null, false, "Boa Forma 1");
        save(produtoEdicaoBoaForma1);
        
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoBoaForma1, 85, 119748L, 1,
                140L, BigDecimal.valueOf(2.99), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(57.06), 679182L, 55L, BigDecimal.valueOf(164.45),
                BigDecimal.valueOf(164.45), 85L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoBravo = Fixture.produtoBravo(tipoProdutoRevista);
        produtoBravo.setEditor(abril);
        produtoBravo.addFornecedor(fornecedorDinap);
        save(produtoBravo);
        
        ProdutoEdicao produtoEdicaoBravo1 = Fixture.produtoEdicao("COD_10", 1L, 10, 30,
                new Long(120), BigDecimal.valueOf(17), BigDecimal.valueOf(19.99),
                "119", produtoBravo, null, false, "Bravo 1");
        save(produtoEdicaoBravo1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoBravo1, 96, 119748L, 2,
                60L, BigDecimal.valueOf(19.99), FormaDevolucao.CAPA,
                RegimeRecolhimento.FINAL, BigDecimal.valueOf(69.37), 583918L, 10L, BigDecimal.valueOf(199.9),
                BigDecimal.valueOf(199.9), 50L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
        produtoCaras.setEditor(abril);
        produtoCaras.addFornecedor(fornecedorDinap);
        save(produtoCaras);
        
        ProdutoEdicao produtoEdicaoCaras1 = Fixture.produtoEdicao("36168001", 1L, 15, 30,
                new Long(200), BigDecimal.valueOf(7), BigDecimal.valueOf(8.9),
                "120", produtoCaras, null, false, "Caras 1");
        save(produtoEdicaoCaras1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoCaras1, 18, 119748L, 3,
                90L, BigDecimal.valueOf(8.9), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(77.21), 688743L, 25L, BigDecimal.valueOf(222.5),
                BigDecimal.valueOf(222.5), 65L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoCasaClaudia = Fixture.produtoCasaClaudia(tipoProdutoRevista);
        produtoCasaClaudia.setEditor(abril);
        produtoCasaClaudia.addFornecedor(fornecedorDinap);
        save(produtoCasaClaudia);
        
        ProdutoEdicao produtoEdicaoCasaClaudia1 = Fixture.produtoEdicao("24664001", 1L, 10, 30,
                new Long(200),  BigDecimal.valueOf(2),  BigDecimal.valueOf(2.99),
                "121", produtoCasaClaudia, null, false, "Casa Claudia 1");
        save(produtoEdicaoCasaClaudia1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoCasaClaudia1, 66, 119749L, 1,
                140L, BigDecimal.valueOf(2.99), FormaDevolucao.INTEIRO,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(33.20), 672613L, 32L, BigDecimal.valueOf(95.68),
                BigDecimal.valueOf(95.68), 108L, Fixture.criarData(3, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoContigo = Fixture.produtoContigo(tipoProdutoRevista);
        produtoContigo.setEditor(abril);
        produtoContigo.addFornecedor(fornecedorDinap);
        save(produtoContigo);
        
        ProdutoEdicao produtoEdicaoContigo1 = Fixture.produtoEdicao("25745001", 1L, 10, 30,
                new Long(100), BigDecimal.valueOf(3), BigDecimal.valueOf(4.99),
                "123", produtoContigo, null, false,"Contigo 1");
        save(produtoEdicaoContigo1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoContigo1, 29, 119749L, 2,
                492L, BigDecimal.valueOf(4.99), FormaDevolucao.CAP_BR,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(427.69), 698621L, 247L, BigDecimal.valueOf(1232.53),
                BigDecimal.valueOf(1232.53), 245L, Fixture.criarData(4, Calendar.OCTOBER, 2012), "P", "P", null);
        
        Produto produtoManequim = Fixture.produtoManequim(tipoProdutoRevista);
        produtoManequim.setEditor(abril);
        produtoManequim.addFornecedor(fornecedorDinap);
        save(produtoManequim);
        
        ProdutoEdicao produtoEdicaoManequim1 = Fixture.produtoEdicao("111", 1L, 10, 30,
                new Long(100), BigDecimal.valueOf(15), BigDecimal.valueOf(21.9),
                "124", produtoManequim, null, false, "Manequim 1");
        save(produtoEdicaoManequim1);
        
        Fixture.newItemChamadaEncalheFornecedor(cef2, produtoEdicaoManequim1, 40, 119749L, 3,
                30L, BigDecimal.valueOf(21.9), FormaDevolucao.INT_BR,
                RegimeRecolhimento.NORMAL, BigDecimal.valueOf(174.78), 692359L, 23L, BigDecimal.valueOf(503.7),
                BigDecimal.valueOf(503.7), 7L, Fixture.criarData(4, Calendar.OCTOBER, 2012), "P", "P", null);
        
        save(cef1, cef2);
        flushClear();
    }
    

    @Test
    public void testObterChamadasEncalheFornecedorPorNumeroSemana() {
        List<ChamadaEncalheFornecedor> chamadas = repository.obterChamadasEncalheFornecedor(null, 41, null); 
        Assert.assertEquals(2, chamadas.size());
        Assert.assertTrue(chamadas.contains(cef1));
        Assert.assertTrue(chamadas.contains(cef2));
    }
    

    @Test
    public void testObterChamadasEncalheFornecedorPorNumeroSemanaVazio() {
        List<ChamadaEncalheFornecedor> chamadas = repository.obterChamadasEncalheFornecedor(null, 50, null); 
        Assert.assertTrue(chamadas.isEmpty());
    }
    
    @Test
    public void testObterChamadasEncalheFornecedorPorPeriodo() {
        List<ChamadaEncalheFornecedor> chamadas = repository
                .obterChamadasEncalheFornecedor(
                        null,
                        null,
                        new Intervalo<Date>(Fixture.criarData(1, Calendar.OCTOBER, 2012), 
                                Fixture.criarData(6, Calendar.OCTOBER, 2012))); 
        Assert.assertEquals(2, chamadas.size());
        Assert.assertTrue(chamadas.contains(cef1));
        Assert.assertTrue(chamadas.contains(cef2));
    }
    
    @Test
    public void testObterChamadasEncalheFornecedorPorPeriodoVazio() {
        List<ChamadaEncalheFornecedor> chamadas = repository
                .obterChamadasEncalheFornecedor(
                        null,
                        null,
                        new Intervalo<Date>(Fixture.criarData(7, Calendar.OCTOBER, 2012), 
                                Fixture.criarData(10, Calendar.OCTOBER, 2012))); 
        Assert.assertTrue(chamadas.isEmpty());
    }
    
    @Test
    public void testObterChamadasEncalheFornecedorPorFornecedor() {
        List<ChamadaEncalheFornecedor> chamadas = repository.obterChamadasEncalheFornecedor(fornecedorDinap.getId(), 41, null); 
        Assert.assertEquals(1, chamadas.size());
        Assert.assertTrue(chamadas.contains(cef2));
    }

}
