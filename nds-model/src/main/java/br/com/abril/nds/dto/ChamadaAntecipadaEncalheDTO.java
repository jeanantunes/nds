package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChamadaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private String codBox;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigDecimal qntExemplares;

	public ChamadaAntecipadaEncalheDTO() {}

	public ChamadaAntecipadaEncalheDTO(String codBox, Integer numeroCota,BigDecimal qntExemplares,String nomeCota ) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer numeroCota,BigDecimal qntExemplares) {
		
		this.numeroCota = numeroCota;
		this.qntExemplares = qntExemplares;
	}
	
	/**
	 * @return the codBox
	 */
	public String getCodBox() {
		return codBox;
	}

	/**
	 * @param codBox the codBox to set
	 */
	public void setCodBox(String codBox) {
		this.codBox = codBox;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the qntExemplares
	 */
	public BigDecimal getQntExemplares() {
		return qntExemplares;
	}

	/**
	 * @param qntExemplares the qntExemplares to set
	 */
	public void setQntExemplares(BigDecimal qntExemplares) {
		this.qntExemplares = qntExemplares;
	}
}
