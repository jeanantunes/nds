package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * Classe DTO para itens em geral.
 * 
 * @author Discover Technology
 *
 * @param <K> - chave
 * @param <V> - valor
 */
public class ItemDTO<K extends Serializable, V extends Serializable> implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7013165086711837400L;

	private K key;
	
	private V value;
	
	/**
	 * Construtor padrão.
	 */
	public ItemDTO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param key - chave
	 * @param value - valor
	 */
	public ItemDTO(K key, V value) {
		
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public V getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(V value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemDTO other = (ItemDTO) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemDTO [key=" + key + ", value=" + value + "]";
	}
	
	public String toFormattedString(){
		return  key + "- " + value;
	}
	

}
