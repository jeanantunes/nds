package br.com.abril.nds.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Classe para comparação de valores de mapa.
 * 
 * @author Discover Technolohy
 *
 * @param <K> - tipo da chave
 * @param <V> - tipo do valor
 */
public class MapValueComparator <K, V extends Comparable<V>> implements Comparator<K> {

	private Map<K, V> map;
	
	private boolean inverseOrder;

	/**
	 * Construtor.
	 * 
	 * @param base - mapa base
	 * @param inverseOrder - flag para inversão de ordem
	 */
	public MapValueComparator(Map<K, V> map, boolean inverseOrder) {

		this.map = map;
		this.inverseOrder = inverseOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(K o1, K o2) {
		
		if (inverseOrder) {
			
			return map.get(o2).compareTo(map.get(o1));
		}
		
		return map.get(o1).compareTo(map.get(o2));
	}

}
