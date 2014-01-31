package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.AcumuloDividasRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCAcumuloDividaRepository extends AbstractRepositoryTest {
    
    @Autowired
    private AcumuloDividasRepository acumuloDividaRepository;
    
    @Test
    public void testQCObterAcumuloDividaPorMovimentoFinanceiroPendente() {
        acumuloDividaRepository.obterAcumuloDividaPorMovimentoFinanceiroPendente(1L);
    }
    

    @Test
    public void testQCObterAcumuloDividaPorDivida() {
        acumuloDividaRepository.obterAcumuloDividaPorDivida(1L);
    }
    
    @Test
    public void testQCObterNumeroMaximoAcumuloCota() {
        acumuloDividaRepository.obterNumeroMaximoAcumuloCota(1L);
    }
    
    @Test
    public void testQCObterNumeroDeAcumulosDivida() {
        acumuloDividaRepository.obterNumeroDeAcumulosDivida(1L);
    }
    
}
