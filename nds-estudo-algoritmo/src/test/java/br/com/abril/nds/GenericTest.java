package br.com.abril.nds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import br.com.abril.nds.model.ProdutoEdicaoBase;

public class GenericTest {

    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	ProdutoEdicaoBase edicao = new ProdutoEdicaoBase();
	edicao.setCodigoProduto(123L);
	edicao.setColecao(false);
	
	Class<? extends ProdutoEdicaoBase> clazz = edicao.getClass();
	Method[] methods = clazz.getMethods();
	
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("g") || name.startsWith("i")) && !name.equalsIgnoreCase("getClass")) {
		System.out.println(name + ": " + method.invoke(edicao));
	    }
	}
	
	System.out.println("\r\nGenerics:\r\n");
	fazTabela(edicao);
	
	List<ProdutoEdicaoBase> list = new ArrayList<>();
	list.add(edicao);
	list.add(edicao);
	
	fazTabela(list);
    }
    
    private <T> void fazTabela(T type) {
	
	
	
	if(type instanceof List) {
	    //print cabecalho
	    for (Object t : (List<?>) type) {
		fazTabela(t);
	    }
	}
	
	Class<? extends Object> clazz = type.getClass();
	
	Method[] methods = clazz.getMethods();
	
	Map<String, Method> sortedMethods = new TreeMap<String, Method>();
	
	for (Method method : methods) {
	    String name = method.getName();
	    if((name.startsWith("get") || name.startsWith("is")) && !name.equalsIgnoreCase("getClass")) {
		sortedMethods.put(name.replaceAll("is|get", ""), method);
	    }
	}
	
	Map<String, Method> finalOrderMethods = new LinkedHashMap<String, Method>();
	if(sortedMethods.containsKey("Id")) {
	    finalOrderMethods.put("Id", sortedMethods.remove("Id"));
	}
	if(sortedMethods.containsKey("Numero")) {
	    finalOrderMethods.put("Numero", sortedMethods.remove("Numero"));
	}
	if(sortedMethods.containsKey("NumeroEdicao")) {
	    finalOrderMethods.put("NumeroEdicao", sortedMethods.remove("NumeroEdicao"));
	}
	finalOrderMethods.putAll(sortedMethods);
	
	Set<Entry<String, Method>> entrySet = finalOrderMethods.entrySet();
	for (Entry<String, Method> entry : entrySet) {
	    System.out.println(entry.getKey());
	}
    }
}
