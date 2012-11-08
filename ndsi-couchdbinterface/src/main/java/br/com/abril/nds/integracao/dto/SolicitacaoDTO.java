package br.com.abril.nds.integracao.dto;

import java.util.Date;

import com.ancientprogramming.fixedformat4j.format.impl.StringFormatter;


public class SolicitacaoDTO {

	private Long codigoDistribuidor;
	private Date solicitacao;
	private String codigoAcerto;
	private Integer numeroSequencia;
	private String descricaoMotivo;
	private String codigoMotivo;
	private String codigoSituacao;
	
	/**
	 * @return the codigoDistribuidor
	 */
	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	/**
	 * @param codigoDistribuidor the codigoDistribuidor to set
	 */
	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	/**
	 * @return the solicitacao
	 */
	public Date getSolicitacao() {
		return solicitacao;
	}
	/**
	 * @param solicitacao the solicitacao to set
	 */
	public void setSolicitacao(Date solicitacao) {
		this.solicitacao = solicitacao;
	}
	/**
	 * @return the codigoAcerto
	 */
	public String getCodigoAcerto() {
		return codigoAcerto;
	}
	/**
	 * @param codigoAcerto the codigoAcerto to set
	 */
	public void setCodigoAcerto(String codigoAcerto) {
		this.codigoAcerto = codigoAcerto;
	}
	/**
	 * @return the numeroSequencia
	 */
	public Integer getNumeroSequencia() {
		return numeroSequencia;
	}
	/**
	 * @param numeroSequencia the numeroSequencia to set
	 */
	public void setNumeroSequencia(Integer numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}
	/**
	 * @return the descricaoMotivo
	 */
	public String getDescricaoMotivo() {
		return descricaoMotivo;
	}
	/**
	 * @param descricaoMotivo the descricaoMotivo to set
	 */
	public void setDescricaoMotivo(String descricaoMotivo) {
		this.descricaoMotivo = descricaoMotivo;
	}
	/**
	 * @return the codigoMotivo
	 */
	public String getCodigoMotivo() {
		return codigoMotivo;
	}
	/**
	 * @param codigoMotivo the codigoMotivo to set
	 */
	public void setCodigoMotivo(String codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}
		
	public String toString() {
		return String.format("codigoDistribuidor=%s, solicitacao=%s, codigoAcerto=%s, numeroSequencia=%s, descricaoMotivo=%s, codigoMotivo=%s",
				codigoDistribuidor.toString(), solicitacao.toString(), codigoAcerto, numeroSequencia.toString(), descricaoMotivo, codigoMotivo
				);
	}
	/**
	 * @return the codigoSituacao
	 */
	public String getCodigoSituacao() {
		return codigoSituacao;
	}
	/**
	 * @param codigoSituacao the codigoSituacao to set
	 */
	public void setCodigoSituacao(String codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}
}
