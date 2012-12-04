package br.com.abril.nds.model.estoque;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class TipoDiferencaTest {
    
    @Test
    public void testIsDiferencaDeSobraDe() {
        Assert.assertTrue(TipoDiferenca.SOBRA_DE.isDiferencaDe());
    }
    
    @Test
    public void testIsDiferencaDeSobraEm() {
        Assert.assertFalse(TipoDiferenca.SOBRA_EM.isDiferencaDe());
    }
    
    @Test
    public void testIsDiferencaDeFaltaDe() {
        Assert.assertTrue(TipoDiferenca.FALTA_DE.isDiferencaDe());
    }
    
    @Test
    public void testIsDiferencaDeFaltaEm() {
        Assert.assertFalse(TipoDiferenca.FALTA_EM.isDiferencaDe());
    }
    
    @Test
    public void testIsFaltaSobraDe() {
        Assert.assertFalse(TipoDiferenca.SOBRA_DE.isFalta());
    }
    
    @Test
    public void testIsFaltaSobraEm() {
        Assert.assertFalse(TipoDiferenca.SOBRA_EM.isFalta());
    }

    @Test
    public void testIsFaltaFaltaDe() {
        Assert.assertTrue(TipoDiferenca.FALTA_DE.isFalta());
    }
    
    @Test
    public void testIsFaltaFaltaEm() {
        Assert.assertTrue(TipoDiferenca.FALTA_EM.isFalta());
    }
    
    @Test
    public void testIsSobraSobraDe() {
        Assert.assertTrue(TipoDiferenca.SOBRA_DE.isSobra());
    }
    
    @Test
    public void testIsSobraSobraEm() {
        Assert.assertTrue(TipoDiferenca.SOBRA_EM.isSobra());
    }

    @Test
    public void testIsSobraFaltaDe() {
        Assert.assertFalse(TipoDiferenca.FALTA_DE.isSobra());
    }
    
    @Test
    public void testIsSobraFaltaEm() {
        Assert.assertFalse(TipoDiferenca.FALTA_EM.isSobra());
    }
    
    @Test
    public void testGetTiposDiferencaDe() {
        Collection<TipoDiferenca> diferencasDe = TipoDiferenca.getTiposDiferencaDe();
        Assert.assertNotNull(diferencasDe);
        Assert.assertEquals(2, diferencasDe.size());
        Assert.assertTrue(diferencasDe.contains(TipoDiferenca.SOBRA_DE));
        Assert.assertTrue(diferencasDe.contains(TipoDiferenca.FALTA_DE));
    }
    
    @Test
    public void testGetTiposDiferencaEm() {
        Collection<TipoDiferenca> diferencasEm = TipoDiferenca.getTiposDiferencaEm();
        Assert.assertNotNull(diferencasEm);
        Assert.assertEquals(2, diferencasEm.size());
        Assert.assertTrue(diferencasEm.contains(TipoDiferenca.SOBRA_EM));
        Assert.assertTrue(diferencasEm.contains(TipoDiferenca.FALTA_EM));
    }
}
