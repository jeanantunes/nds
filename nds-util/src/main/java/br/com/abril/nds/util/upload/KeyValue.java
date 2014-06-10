package br.com.abril.nds.util.upload;

import java.io.Serializable;

public class KeyValue implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7711846618893343671L;

	private String key;
	private Object value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}