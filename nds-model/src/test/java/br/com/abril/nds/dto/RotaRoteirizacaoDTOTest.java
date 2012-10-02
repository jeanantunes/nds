package br.com.abril.nds.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.dto.PdvRoteirizacaoDTO.OrigemEndereco;

public class RotaRoteirizacaoDTOTest {
    
    private RotaRoteirizacaoDTO rota;
    private PdvRoteirizacaoDTO pdv1;
    private PdvRoteirizacaoDTO pdv2;
    private PdvRoteirizacaoDTO pdv3;
    private PdvRoteirizacaoDTO pdv4;
    
    @Before
    public void setUp() {
       rota = new RotaRoteirizacaoDTO(Long.valueOf(1), 1, "Rota 1");
       pdv1 = new PdvRoteirizacaoDTO(Long.valueOf(1), "PDV 1", OrigemEndereco.COTA, null, 123, "Cota 1", 1);
       rota.addPdv(pdv1);
       pdv2 = new PdvRoteirizacaoDTO(Long.valueOf(2), "PDV 2", OrigemEndereco.COTA, null, 456, "Cota 1", 2);
       rota.addPdv(pdv2);
       pdv3 = new PdvRoteirizacaoDTO(Long.valueOf(3), "PDV 3", OrigemEndereco.COTA, null, 789, "Cota 1", 3);
       rota.addPdv(pdv3);
       pdv4 = new PdvRoteirizacaoDTO(Long.valueOf(4), "PDV 4", OrigemEndereco.COTA, null, 321, "Cota 1", 4);
       rota.addPdv(pdv4);
    }
    
    
    @Test
    public void testAlterarOrdemSucesso() {
        boolean result = rota.alterarOrdemPdv(pdv1.getId(), 5);
        Assert.assertTrue(result);
        Assert.assertTrue(pdv1.getOrdem().equals(5));
        Assert.assertTrue(pdv2.getOrdem().equals(2));
        Assert.assertTrue(pdv3.getOrdem().equals(3));
        Assert.assertTrue(pdv4.getOrdem().equals(4));
        
    }
    
    @Test
    public void testAlterarOrdemMesmaOrdem() {
        boolean result = rota.alterarOrdemPdv(pdv1.getId(), 1);
        Assert.assertTrue(result);
        Assert.assertTrue(pdv1.getOrdem().equals(1));
        Assert.assertTrue(pdv2.getOrdem().equals(2));
        Assert.assertTrue(pdv3.getOrdem().equals(3));
        Assert.assertTrue(pdv4.getOrdem().equals(4));
    }
    
    @Test
    public void testAlterarOrdemDuplicada() {
        boolean result = rota.alterarOrdemPdv(pdv1.getId(), 2);
        Assert.assertFalse(result);
        Assert.assertTrue(pdv1.getOrdem().equals(1));
        Assert.assertTrue(pdv2.getOrdem().equals(2));
        Assert.assertTrue(pdv3.getOrdem().equals(3));
        Assert.assertTrue(pdv4.getOrdem().equals(4));
    }


}
