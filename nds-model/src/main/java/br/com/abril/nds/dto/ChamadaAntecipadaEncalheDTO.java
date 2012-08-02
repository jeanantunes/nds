package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChamadaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer codBox;
	
	private String nomeBox;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigDecimal qntExemplares;
	
	private Long codigoChamadaEncalhe;
	
	private Long idLancamento;

	public ChamadaAntecipadaEncalheDTO() {}

	public ChamadaAntecipadaEncalheDTO(Integer codBox,String nomeBox ,Integer numeroCota,BigDecimal qntExemplares,Long idLancamento,String nomeCota) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
		this.nomeBox = nomeBox;
		this.idLancamento = idLancamento;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer codBox,String nomeBox, Integer numeroCota,BigDecimal qntExemplares,String nomeCota, Long codigoChamadaEncalhe) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
		this.nomeBox = nomeBox;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer numeroCota,BigDecimal qntExemplares,Long codigoChamadaEncalhe, Long idLancamento) {
		
		this.numeroCota = numeroCota;
		this.qntExemplares = qntExemplares;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
		this.idLancamento = idLancamento;
	}
	
	
	
	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
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
