package br.com.abril.nds.util;

import java.io.Serializable;

/**
 * 
 * @author Diego Fernandes
 *
 * @param <T>
 */
public class Intervalo<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7499592002561128139L;

	private T de;
	
	private T ate;
	
	public Intervalo() {
	}
		public Intervalo(T de, T ate) {
		super();
		this.de = de;
		this.ate = ate;
	}



	/**
	 * @return the de
	 */
	public T getDe() {
		return de;
	}

	/**
	 * @param de the de to set
	 */
	public void setDe(T de) {
		this.de = de;
	}

	/**
	 * @return the ate
	 */
	public T getAte() {
		return ate;
	}

	/**
	 * @param ate the ate to set
	 */
	public void setAte(T ate) {
		this.ate = ate;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ate == null) ? 0 : ate.hashCode());
		result = prime * result + ((de == null) ? 0 : de.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intervalo other = (Intervalo) obj;
		if (ate == null) {
			if (other.ate != null)
				return false;
		} else if (!ate.equals(other.ate))
			return false;
		if (de == null) {
			if (other.de != null)
				return false;
		} else if (!de.equals(other.de))
			return false;
		return true;
	}
	
}
