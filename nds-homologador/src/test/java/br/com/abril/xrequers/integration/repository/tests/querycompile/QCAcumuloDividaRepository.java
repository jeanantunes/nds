package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.AcumuloDividasRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCAcumuloDividaRepository extends AbstractRepositoryTest {
    
    @Autowired
    private AcumuloDividasRepository acumuloDividaRepository;
    
    @Test
    public void test_qc_obter_acumulo_divida_por_movimento_financeiro_pendente() {
        
        this.acumuloDividaRepository.obterAcumuloDividaPorMovimentoFinanceiroPendente(1L);
    }
    
    @Test
    public void testQCObterAcumuloDividaPorDivida() {
        this.acumuloDividaRepository.obterAcumuloDividaPorDivida(1L);
    }
    
    @Test
    public void testQCObterNumeroMaximoAcumuloCota() {
        this.acumuloDividaRepository.obterNumeroMaximoAcumuloCota(1L, 1L);
        this.acumuloDividaRepository.obterNumeroMaximoAcumuloCota(1L, 1L);
    }
    
    @Test
    public void testQCObterNumeroDeAcumulosDivida() {
        this.acumuloDividaRepository.obterNumeroDeAcumulosDivida(1L);
        this.acumuloDividaRepository.obterNumeroDeAcumulosDivida(1L);
    }
    
}
