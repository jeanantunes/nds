package br.com.abril.nds.process.definicaobases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.mocks.MockEdicoes;
import br.com.abril.nds.model.Estudo;

public class DefinicaoBasesTest {

    private DefinicaoBases bases;

    @Before
    public void setUp() throws Exception {
	bases = new DefinicaoBases(new Estudo());
	bases.setEdicoesRecebidasParaEstudoRaw(MockEdicoes.getEdicoesRandom());
    }

    @Test
    public void testExecutarProcesso() throws Exception {
	bases.executarProcesso();
	assertNotNull(bases.getEstudo().getEdicoesBase());
    }
}
