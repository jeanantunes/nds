package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;

public class CotaGarantiaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6486481011630273584L;
	
	
	private TipoGarantia tipo;
	
	private CotaGarantia cotaGarantia;
	
	
	public CotaGarantiaDTO() {
	}


	public CotaGarantiaDTO(TipoGarantia tipo, CotaGarantia cotaGarantia) {
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
	public CotaGarantia getCotaGarantia() {
		return cotaGarantia;
	}

	/**
	 * @param cotaGarantia the cotaGarantia to set
	 */
	public void setCotaGarantia(CotaGarantia cotaGarantia) {
		this.cotaGarantia = cotaGarantia;
	}

}
