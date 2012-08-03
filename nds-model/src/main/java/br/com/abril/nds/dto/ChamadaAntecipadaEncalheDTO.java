package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ChamadaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer codBox;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigInteger qntExemplares;
	
	private Long codigoChamadaEncalhe;

	public ChamadaAntecipadaEncalheDTO() {}

	public ChamadaAntecipadaEncalheDTO(Integer codBox, Integer numeroCota,BigInteger qntExemplares,String nomeCota) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer codBox, Integer numeroCota,BigInteger qntExemplares,String nomeCota, Long codigoChamadaEncalhe) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer numeroCota,BigInteger qntExemplares,Long codigoChamadaEncalhe) {
		
		this.numeroCota = numeroCota;
		this.qntExemplares = qntExemplares;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
	}
	
	/**
	 * @return the codigoChamadaEncalhe
	 */
	public Long getCodigoChamadaEncalhe() {
		return codigoChamadaEncalhe;
	}

	/**
	 * @param codigoChamadaEncalhe the codigoChamadaEncalhe to set
	 */
	public void setCodigoChamadaEncalhe(Long codigoChamadaEncalhe) {
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
	}

	/**
	 * @return the codBox
	 */
	public Integer getCodBox() {
		return codBox;
	}

	/**
	 * @param codBox the codBox to set
	 */
	public void setCodBox(Integer codBox) {
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
	public BigInteger getQntExemplares() {
		return qntExemplares;
	}

	/**
	 * @param qntExemplares the qntExemplares to set
	 */
	public void setQntExemplares(BigInteger qntExemplares) {
		this.qntExemplares = qntExemplares;
	}
}
