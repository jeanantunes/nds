package br.com.abril.nds.client.util.comparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.persistence.metamodel.MappedSuperclassType;

import br.com.abril.nds.model.seguranca.Permissao;

/**
 * Classe que compara diferentes permissões, para organização de acordo com a ordem da Permissao
 * @author InfoA2
 */
public class PermissaoComparator implements SortedMap {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	/*@Override
	public int compare(Object o1, Object o2) {
		Integer ordem1 = ((Permissao) o1).getOrdem();
		Integer ordem2 = ((Permissao) o2).getOrdem();
		return ordem1.compareTo(ordem2);
	}*/

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object put(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Comparator comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap subMap(Object fromKey, Object toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap headMap(Object toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap tailMap(Object fromKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
