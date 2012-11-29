package br.com.abril.nds.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdenacaoUtil  {

	public static <T extends Ordenavel> void incluirItemOrdenado(T itemIncluido, List<T> listaItensExistentes) {

		List<T> listaItensNovos = new ArrayList<T>();
		listaItensNovos.add(itemIncluido);
		
		incluirListaOrdenada(listaItensNovos, listaItensExistentes);
	}
	
	public static <T extends Ordenavel> void incluirListaOrdenada(List<T> listaItensNovos, List<T> listaItensExistentes) {
		
		Integer ordemItemAnterior = 0;
		
		OrdenacaoUtil.sortList(listaItensExistentes);
		OrdenacaoUtil.sortList(listaItensNovos);
		
		for (Ordenavel itemExistente : listaItensExistentes) {
			
			Integer ordem = itemExistente.getOrdem();
			
			if (ordem <= ordemItemAnterior) {
				ordem++;
				itemExistente.setOrdem(ordem);
			}
			
			for (Ordenavel itemNovo : listaItensNovos) {
			
				if (ordem == itemNovo.getOrdem()) {
					
					ordem++;
					itemExistente.setOrdem(ordem);
				}
			}
			
			if (ordem == ordemItemAnterior) {
				ordem++;
				itemExistente.setOrdem(ordem);
			}
			
			ordemItemAnterior = itemExistente.getOrdem();
		}
	}
	
	public static <T extends Ordenavel> void sortList(List<T> lista) {
		
		Collections.sort(lista, new Comparator<Ordenavel>() {
			
			@Override
			public int compare(Ordenavel obj1, Ordenavel obj2) {
				
				if (obj1.getOrdem() == null) 
					return -1;
				
				return obj1.getOrdem().compareTo(obj2.getOrdem()); 
			}
		});
	}

}
