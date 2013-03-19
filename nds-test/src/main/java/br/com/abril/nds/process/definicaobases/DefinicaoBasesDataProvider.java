package br.com.abril.nds.process.definicaobases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.model.ProdutoEdicaoBase;

public abstract class DefinicaoBasesDataProvider {

    @DataProvider(name="getEdicoesInput")
    public static Iterator<Object[]> getEdicoesInput() {
	
	List<Object[]> returnList = new ArrayList<Object[]>();
	
	/*for (int i = 0; i < 3; i++) {
	    List<ProdutoEdicao> edicoesRandom = MockEdicoes.getEdicoesRandom();
	    returnList.add(new Object[] {edicoesRandom});
	}*/
	
	ProdutoEdicaoBase produtoEdicao = new ProdutoEdicaoBase();
	produtoEdicao.setCodigoProduto("61310001");
	
	List<ProdutoEdicaoBase> edicaos = new ArrayList<ProdutoEdicaoBase>();
	edicaos.add(produtoEdicao);
	returnList.add(new Object[] {Arrays.asList(produtoEdicao)});
	
	return returnList.iterator();
    }
}
