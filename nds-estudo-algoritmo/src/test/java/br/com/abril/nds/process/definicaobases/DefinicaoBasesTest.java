package br.com.abril.nds.process.definicaobases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefinicaoBasesTest {

    private DefinicaoBases bases;
    
    @Before
    public void setUp() throws Exception {
	bases = new DefinicaoBases();
    }

    @Test
    public void testExecutarProcesso() throws Exception {
	bases.executarProcesso();
	assertNotNull(bases.getEstudo().getCotas());
//	fail("Not yet implemented");
    }

}
