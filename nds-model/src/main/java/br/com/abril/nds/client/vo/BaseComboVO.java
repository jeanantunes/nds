package br.com.abril.nds.client.vo;

import java.io.Serializable;

/**
 * 
 * @author mateus.medice
 *
 */
public class BaseComboVO implements Serializable {

	private static final long serialVersionUID = 3683639608949150611L;

	private Long value;
	
	private String label;
	
	/**
	 * 
	 */
	public BaseComboVO() {
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public BaseComboVO(Long value, String label) {
		this.value = value;
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Long value) {
		this.value = value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
}
