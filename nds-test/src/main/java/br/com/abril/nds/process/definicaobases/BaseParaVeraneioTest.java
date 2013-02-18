package br.com.abril.nds.process.definicaobases;

import static org.testng.AssertJUnit.assertNotNull;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public class BaseParaVeraneioTest {

    private BaseParaVeraneio baseParaVeraneio;
    
    @BeforeMethod
    public void setUp() throws Exception {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	edicoes.add(getEdicao());
	Estudo estudo = new Estudo();
	estudo.setPracaVeraneio(true);
	estudo.setEdicoesBase(edicoes);
	baseParaVeraneio = new BaseParaVeraneio(estudo);
    }

    @Test
    public void testExecutarProcesso() throws Exception {
	baseParaVeraneio.executarProcesso();
	assertNotNull(baseParaVeraneio.getEstudo().getEdicoesBase());
    }

    private ProdutoEdicao getEdicao() {
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();
	produtoEdicao.setId(134437L);
	produtoEdicao.setIdLancamento(92826L);
	produtoEdicao.setEdicaoAberta(true);
	produtoEdicao.setDataLancamento(LocalDate.parse("2013-02-11").toDate());
	produtoEdicao.setColecao(false);
	produtoEdicao.setParcial(false);
	
	return produtoEdicao;
    }
}
