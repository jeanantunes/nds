package br.com.abril.nds.dto;

import java.util.Date;

public class ControleConferenciaEncalheCotaDTO {
	
	private Integer numeroCota;
	
	private Long idCota;

	private Date dataOperacao;
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
}