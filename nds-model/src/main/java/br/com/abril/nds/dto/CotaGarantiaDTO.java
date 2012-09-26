package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoGarantia;

public class CotaGarantiaDTO<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6486481011630273584L;
	
	
	private TipoGarantia tipo;
	
	private T cotaGarantia;
	
	
	public CotaGarantiaDTO() {
	}


	public CotaGarantiaDTO(TipoGarantia tipo, T cotaGarantia) {
		super();
		this.tipo = tipo;
		this.cotaGarantia = cotaGarantia;
	}


	/**
	 * @return the tipo
	 */
	public TipoGarantia getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoGarantia tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the cotaGarantia
	 */
	public T getCotaGarantia() {
		return cotaGarantia;
	}

	/**
	 * @param cotaGarantia the cotaGarantia to set
	 */
	public void setCotaGarantia(T cotaGarantia) {
		this.cotaGarantia = cotaGarantia;
	}

}
