package br.com.abril.nds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import br.com.abril.nds.model.ProdutoEdicaoBase;

public class GenericTest {

    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	ProdutoEdicaoBase edicao = new ProdutoEdicaoBase();
	edicao.setCodigoProduto(123L);
	edicao.setColecao(false);
	
	Class<? extends ProdutoEdicaoBase> clazz = edicao.getClass();
	
	Method[] declaredMethods = clazz.getMethods();
	
	for (Method method : declaredMethods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		System.out.println(name + ": " + method.invoke(edicao));
	    }
	}
    }
}
