package br.com.abril.nds.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdenacaoUtil  {

	/**
	 * Reordena a lista a partir da ordem do novo item.
	 * 
	 * Ex: novoItem       = 5
	 * 	   listaOrdenavel = [1,2,4,5,6,8]
	 * 	
	 * 	   reordenar(novoItem, listaOrdenavel)
	 * 	   
	 * 	   result { novoItem = 5, listaOrdenavel = [1,2,4,6,7,8] }
	 * 
	 * @param novoItemOrdemFixa 
	 * @param listaItensReordenados
	 */
	public static <T extends Ordenavel> void reordenarLista(T novoItemOrdemFixa, List<T> listaItensReordenados) {

		List<T> listaItensNovos = new ArrayList<T>();
		listaItensNovos.add(novoItemOrdemFixa);
		
		reordenarListas(listaItensNovos, listaItensReordenados);
	}
	
	/**
	 * Reordena a lista a partir da ordem de outra lista.
	 * 
	 * Ex: listaFixa      = [1,3,5,7]
	 * 	   listaOrdenavel = [1,2,4,5,6,8]
	 * 	
	 * 	   reordenar(listaFixa, listaOrdenavel)
	 * 	   
	 * 	   result { listaFixa = [1,3,5,7], listaOrdenavel = [2,4,6,8,9,10] }
	 * 
	 * 
	 * @param listaItensOrdemFixa
	 * @param listaItensReordenados
	 */
	public static <T extends Ordenavel> void reordenarListas(List<T> listaItensOrdemFixa, List<T> listaItensReordenados) {
		
		if (listaItensOrdemFixa == null || listaItensReordenados == null)
			return;
		
		Integer ordemItemAnterior = 0;
		
		OrdenacaoUtil.sortList(listaItensReordenados);
		OrdenacaoUtil.sortList(listaItensOrdemFixa);
		
		for (Ordenavel itemExistente : listaItensReordenados) {
			
			Integer ordem = itemExistente.getOrdem();
			
			if (ordem <= ordemItemAnterior) {
				ordem++;
				itemExistente.setOrdem(ordem);
			}
			
			for (Ordenavel itemNovo : listaItensOrdemFixa) {
			
				if (ordem.equals(itemNovo.getOrdem())) {
					
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
