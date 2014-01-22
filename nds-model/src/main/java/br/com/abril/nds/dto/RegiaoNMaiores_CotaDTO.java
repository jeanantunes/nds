package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.dto.filtro.FiltroDTO;

public class RegiaoNMaiores_CotaDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -550941856604957015L;
	
	private Integer numeroCota;
	private String nomePessoa;
	private String status;
	private BigDecimal vdMedia;
	private BigInteger cotaId;

	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomePessoa() {
		return nomePessoa;
	}
	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getVdMedia() {
		return vdMedia;
	}
	public void setVdMedia(BigDecimal vdMedia) {
		this.vdMedia = vdMedia;
	}
	public BigInteger getCotaId() {
		return cotaId;
	}
	public void setCotaId(BigInteger cotaId) {
		this.cotaId = cotaId;
	}
}