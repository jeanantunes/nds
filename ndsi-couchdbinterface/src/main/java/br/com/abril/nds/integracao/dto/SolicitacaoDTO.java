package br.com.abril.nds.integracao.dto;

import java.util.Date;


public class SolicitacaoDTO {

	private Long codigoDistribuidor;
	private Date solicitacao;
	private Long codigoAcerto;
	private Integer numeroSequencia;
	private String motivo;
	private Long codigoMotivo;
	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	public Date getSolicitacao() {
		return solicitacao;
	}
	public void setSolicitacao(Date solicitacao) {
		this.solicitacao = solicitacao;
	}
	public Long getCodigoAcerto() {
		return codigoAcerto;
	}
	public void setCodigoAcerto(Long codigoAcerto) {
		this.codigoAcerto = codigoAcerto;
	}
	public Integer getNumeroSequencia() {
		return numeroSequencia;
	}
	public void setNumeroSequencia(Integer numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Long getCodigoMotivo() {
		return codigoMotivo;
	}
	public void setCodigoMotivo(Long codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}
	
    
}
