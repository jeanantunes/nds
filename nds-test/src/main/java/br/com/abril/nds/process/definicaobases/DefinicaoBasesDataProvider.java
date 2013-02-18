package br.com.abril.nds.process.definicaobases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.model.ProdutoEdicao;

public abstract class DefinicaoBasesDataProvider {

    @DataProvider(name="getEdicoesInput")
    public static Iterator<Object[]> getEdicoesInput() {
	
	List<Object[]> returnList = new ArrayList<Object[]>();
	
	/*for (int i = 0; i < 3; i++) {
	    List<ProdutoEdicao> edicoesRandom = MockEdicoes.getEdicoesRandom();
	    returnList.add(new Object[] {edicoesRandom});
	}*/
	
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();
	produtoEdicao.setCodigoProduto(61310001L);
	
	List<ProdutoEdicao> edicaos = new ArrayList<>();
	edicaos.add(produtoEdicao);
	returnList.add(new Object[] {Arrays.asList(produtoEdicao)});
	
	return returnList.iterator();
    }
}
