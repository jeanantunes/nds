package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;
import java.util.Date;

public class FiltroBoletoAvulsoDTO {
	
	private Long idRegiao;
	
	private Integer idBox;
	
	private Long idRoteiro;
	
	private Long idRota;

	private Long idBanco;
	
	private Date dataVencimento;
	
	private BigDecimal valor;
	
	private String observacao;
	
	public Long getIdRegiao() {
		return idRegiao;
	}
	
	public void setIdRegiao(Long idRegiao) {
		this.idRegiao = idRegiao;
	}
	
	public Integer getIdBox() {
		return idBox;
	}
	
	public void setIdBox(Integer idBox) {
		this.idBox = idBox;
	}
	
	public Long getIdRoteiro() {
		return idRoteiro;
	}
	
	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}
	
	public Long getIdRota() {
		return idRota;
	}
	
	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}
	
	public Long getIdBanco() {
		return idBanco;
	}
	
	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}
	
	public Date getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}