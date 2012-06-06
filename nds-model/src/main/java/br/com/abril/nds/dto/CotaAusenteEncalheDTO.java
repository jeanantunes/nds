package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaAusenteEncalheDTO implements Serializable {

	private static final long serialVersionUID = -5167121794665878284L;
	
	private Long idCota;

	public CotaAusenteEncalheDTO() {
		
	}
	
	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	
}
