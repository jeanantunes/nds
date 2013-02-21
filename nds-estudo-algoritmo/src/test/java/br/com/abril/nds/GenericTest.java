package br.com.abril.nds;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GenericTest {

    @Test
    public void test() {
	List<Integer> list = new ArrayList<>();
	System.out.println(list.size());
	
	list.add(1);
	System.out.println(list.size());
	
	list.get(list.size());
    }
}
