package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;
import br.com.abril.nds.repository.AnaliseEstudoRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCAnaliseEstudoRepository extends AbstractRepositoryTest {
    
    @Autowired
    private AnaliseEstudoRepository analiseEstudoRepository;
    
    private FiltroAnaliseEstudoDTO filtro;
    
    @Before
    public void setup() {

        this.filtro = new FiltroAnaliseEstudoDTO();
    }
    
    @Test
    public void testQCAnaliseEstudoRepository() {
        this.analiseEstudoRepository.buscarEstudos(this.filtro);
    }
    
}
