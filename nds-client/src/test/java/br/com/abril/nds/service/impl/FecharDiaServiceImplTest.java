package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import br.com.abril.nds.dto.fechamentodiario.DiferencaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.fechar.dia.FechamentoDiario;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDiferenca;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;

public class FecharDiaServiceImplTest {
    
    private FecharDiaServiceImpl fecharDiaService;
    
    private DiferencaEstoqueRepository diferencaRepository;
    
    private Diferenca diferencaVeja;
    
    private Diferenca diferencaQuatroRodas;
    
    @Before
    public void setUp() {
       fecharDiaService = new FecharDiaServiceImpl();
       
       diferencaRepository = Mockito.mock(DiferencaEstoqueRepository.class);
       
       Whitebox.setInternalState(fecharDiaService, "diferencaRepository", diferencaRepository);
    }
    
    @Test
    public void testIncluirFaltasSobras() {
        Date dataFechamento = new Date();
        criarDiferencas(dataFechamento);

        List<Diferenca> diferencas = new ArrayList<>();
        diferencas.add(diferencaVeja);
        diferencas.add(diferencaQuatroRodas);
        
        Mockito.when(diferencaRepository.obterDiferencas(dataFechamento)).thenReturn(diferencas);
        
        FechamentoDiario fechamentoDiario = new FechamentoDiario();
        fechamentoDiario.setDataFechamento(dataFechamento);
        
        List<DiferencaDTO> dtos = fecharDiaService.incluirFaltasSobras(fechamentoDiario);
        Assert.assertNotNull(dtos);
        Assert.assertEquals(2, dtos.size());
        for (int i = 0; i < dtos.size(); i++) {
            assertValues(diferencas.get(i), dtos.get(i));
        }
        
        List<FechamentoDiarioDiferenca> fechamentoDiferencas = fechamentoDiario.getDiferencas();
        Assert.assertNotNull(fechamentoDiferencas);
        Assert.assertEquals(2, fechamentoDiferencas.size());
        
        for (int i = 0; i < fechamentoDiferencas.size(); i++) {
            assertValues(diferencas.get(i), fechamentoDiferencas.get(i));
        }
        
        Mockito.verify(diferencaRepository).obterDiferencas(dataFechamento);
    }
    
    private void criarDiferencas(Date data) {
        
        Usuario joao = Fixture.usuarioJoao();
        
        NCM ncmRevista = Fixture.ncm(49029000l, "REVISTAS", "KG");
        
        TipoProduto revista = Fixture.tipoRevista(ncmRevista);
        
        Produto produtoVeja = Fixture.produtoVeja(revista);
        
        Produto produtoQuatroRodas = Fixture.produtoQuatroRodas(revista);
        
        ProdutoEdicao veja1 = Fixture.produtoEdicao(1L, 10, 14,
                Long.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(15), "111", produtoVeja, null, false);
        
        ProdutoEdicao quatroRodas1 =  Fixture.produtoEdicao(1L, 7, 30,
                Long.valueOf(300), BigDecimal.valueOf(12.5), BigDecimal.valueOf(16.5), "118", produtoQuatroRodas, null, false);
    
        diferencaVeja = Fixture.diferenca(BigInteger.valueOf(2), joao, veja1,
                TipoDiferenca.FALTA_EM, StatusConfirmacao.PENDENTE, null,
                false, null, TipoDirecionamentoDiferenca.COTA, data);
        
        diferencaQuatroRodas = Fixture.diferenca(BigInteger.valueOf(1), joao, quatroRodas1,
                TipoDiferenca.SOBRA_DE, StatusConfirmacao.PENDENTE, null,
                false, null, TipoDirecionamentoDiferenca.ESTOQUE, data);
    }
    
    private void assertValues(Diferenca diferenca, DiferencaDTO diferencaDTO) {
        Assert.assertEquals(diferenca.getQtdeExemplares(), BigInteger.valueOf(diferencaDTO.getQtdeExemplar()));
        
        ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
        Produto produto = produtoEdicao.getProduto();
        Assert.assertEquals(produto.getCodigo(), diferencaDTO.getCodigoProduto());
        Assert.assertEquals(produto.getNome(), diferencaDTO.getNomeProduto());
        Assert.assertEquals(produtoEdicao.getNumeroEdicao(), diferencaDTO.getNumeroEdicao());
        Assert.assertEquals(diferenca.getTipoDiferenca(), diferencaDTO.getTipoDiferenca());
        Assert.assertEquals(diferenca.getValorTotalReal(), diferencaDTO.getTotal());
        LancamentoDiferenca lancamento = diferenca.getLancamentoDiferenca();
        if (lancamento != null) {
            Assert.assertEquals(lancamento.getStatus(), diferencaDTO.getStatusAprovacao());
            Assert.assertEquals(lancamento.getMotivo(), diferencaDTO.getMotivo());
        } else {
            Assert.assertNull(diferencaDTO.getStatusAprovacao());
            Assert.assertNull(diferencaDTO.getMotivo());
        }
        
    }
    
    private void assertValues(Diferenca diferenca, FechamentoDiarioDiferenca fechamentoDiferenca) {
        Assert.assertEquals(diferenca.getQtdeExemplares(), fechamentoDiferenca.getQtdeExemplares());
        ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
        Produto produto = produtoEdicao.getProduto();
        Assert.assertEquals(produto.getCodigo(), fechamentoDiferenca.getCodigoProduto());
        Assert.assertEquals(produto.getNome(), fechamentoDiferenca.getNomeProduto());
        Assert.assertEquals(produtoEdicao.getNumeroEdicao(), fechamentoDiferenca.getNumeroEdicao());
        Assert.assertEquals(produtoEdicao.getId(), fechamentoDiferenca.getIdProdutoEdicao());
        Assert.assertEquals(diferenca.getTipoDiferenca(), fechamentoDiferenca.getTipoDiferenca());
        Assert.assertEquals(diferenca.getValorTotal(), fechamentoDiferenca.getTotal());
        Assert.assertEquals(diferenca.getDataMovimento(), fechamentoDiferenca.getDataLancamento());
        LancamentoDiferenca lancamento = diferenca.getLancamentoDiferenca();
        if (lancamento != null) {
            Assert.assertEquals(lancamento.getStatus(), fechamentoDiferenca.getStatusAprovacao());
            Assert.assertEquals(lancamento.getMotivo(), fechamentoDiferenca.getMotivoAprovacao());
        } else {
            Assert.assertNull(fechamentoDiferenca.getStatusAprovacao());
            Assert.assertNull(fechamentoDiferenca.getMotivoAprovacao());
        }
    }
	
}
