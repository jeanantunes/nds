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

}
