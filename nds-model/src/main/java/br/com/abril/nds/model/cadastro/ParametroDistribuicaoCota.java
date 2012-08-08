package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ParametroDistribuicaoCota implements Serializable {

	private static final long serialVersionUID = 7170591493362816632L;
	
	@Column(name = "QTDE_PDV", nullable = true)
	private Integer qtdePDV;
	
	@Column(name = "ASSISTENTE_COMERCIAL", nullable = true)
	private String assistenteComercial;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "TIPO_ENTREGA_ID")
	private TipoEntrega tipoEntrega;
	
	@Column(name = "OBSERVACAO", nullable = true)
	private String observacao;
	
	@Column(name = "ARRENDATARIO", nullable = true)
	private Boolean arrendatario;
	
	@Column(name = "REPARTE_POR_PONTO_VENDA", nullable = true)
	private Boolean repartePorPontoVenda;
	
	@Column(name = "SOLICITA_NUM_ATRASADOS", nullable = true)
	private Boolean solicitaNumAtras;
	
	@Column(name = "RECEBE_RECOLHE_PARCIAIS", nullable = true)
	private Boolean recebeRecolheParcias;
	
	@Column(name = "NOTA_ENVIO_IMPRESSO", nullable = true)
	private Boolean notaEnvioImpresso;
	
	@Column(name = "NOTA_ENVIO_EMAIL", nullable = true)
	private Boolean notaEnvioEmail;
	
	@Column(name = "CHAMADA_ENCALHE_IMPRESSO", nullable = true)
	private Boolean chamadaEncalheImpresso;
	
	@Column(name = "CHAMADA_ENCALHE_EMAIL", nullable = true)
	private Boolean chamadaEncalheEmail;
	
	@Column(name = "SLIP_IMPRESSO", nullable = true)
	private Boolean slipImpresso;
	
	@Column(name = "SLIP_EMAIL", nullable = true)
	private Boolean slipEmail;
	
	@Column(name = "PROCURACAO_ASSINADA")
	private boolean procuracaoAssinada;

	
	public ParametroDistribuicaoCota(){
		
	}
	
	public ParametroDistribuicaoCota(Integer qtdePDV,
			String assistenteComercial, TipoEntrega tipoEntrega,
			String observacao, Boolean arrendatario,
			Boolean repartePorPontoVenda, Boolean solicitaNumAtras,
			Boolean recebeRecolheParcias, Boolean notaEnvioImpresso,
			Boolean notaEnvioEmail, Boolean chamadaEncalheImpresso,
			Boolean chamadaEncalheEmail, Boolean slipImpresso, Boolean slipEmail) {
		super();
		this.qtdePDV = qtdePDV;
		this.assistenteComercial = assistenteComercial;
		this.tipoEntrega = tipoEntrega;
		this.observacao = observacao;
		this.arrendatario = arrendatario;
		this.repartePorPontoVenda = repartePorPontoVenda;
		this.solicitaNumAtras = solicitaNumAtras;
		this.recebeRecolheParcias = recebeRecolheParcias;
		this.notaEnvioImpresso = notaEnvioImpresso;
		this.notaEnvioEmail = notaEnvioEmail;
		this.chamadaEncalheImpresso = chamadaEncalheImpresso;
		this.chamadaEncalheEmail = chamadaEncalheEmail;
		this.slipImpresso = slipImpresso;
		this.slipEmail = slipEmail;
	}

	/**
	 * @return the qtdePDV
	 */
	public Integer getQtdePDV() {
		return qtdePDV;
	}

	/**
	 * @param qtdePDV the qtdePDV to set
	 */
	public void setQtdePDV(Integer qtdePDV) {
		this.qtdePDV = qtdePDV;
	}

	/**
	 * @return the assistenteComercial
	 */
	public String getAssistenteComercial() {
		return assistenteComercial;
	}

	/**
	 * @param assistenteComercial the assistenteComercial to set
	 */
	public void setAssistenteComercial(String assistenteComercial) {
		this.assistenteComercial = assistenteComercial;
	}

	/**
	 * @return the tipoEntrega
	 */
	public TipoEntrega getTipoEntrega() {
		return tipoEntrega;
	}

	/**
	 * @param tipoEntrega the tipoEntrega to set
	 */
	public void setTipoEntrega(TipoEntrega tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}

	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the arrendatario
	 */
	public Boolean getArrendatario() {
		return arrendatario;
	}

	/**
	 * @param arrendatario the arrendatario to set
	 */
	public void setArrendatario(Boolean arrendatario) {
		this.arrendatario = arrendatario;
	}

	/**
	 * @return the repartePorPontoVenda
	 */
	public Boolean getRepartePorPontoVenda() {
		return repartePorPontoVenda;
	}

	/**
	 * @param repartePorPontoVenda the repartePorPontoVenda to set
	 */
	public void setRepartePorPontoVenda(Boolean repartePorPontoVenda) {
		this.repartePorPontoVenda = repartePorPontoVenda;
	}

	/**
	 * @return the solicitaNumAtras
	 */
	public Boolean getSolicitaNumAtras() {
		return solicitaNumAtras;
	}

	/**
	 * @param solicitaNumAtras the solicitaNumAtras to set
	 */
	public void setSolicitaNumAtras(Boolean solicitaNumAtras) {
		this.solicitaNumAtras = solicitaNumAtras;
	}

	/**
	 * @return the recebeRecolheParcias
	 */
	public Boolean getRecebeRecolheParcias() {
		return recebeRecolheParcias;
	}

	/**
	 * @param recebeRecolheParcias the recebeRecolheParcias to set
	 */
	public void setRecebeRecolheParcias(Boolean recebeRecolheParcias) {
		this.recebeRecolheParcias = recebeRecolheParcias;
	}

	/**
	 * @return the notaEnvioImpresso
	 */
	public Boolean getNotaEnvioImpresso() {
		return notaEnvioImpresso;
	}

	/**
	 * @param notaEnvioImpresso the notaEnvioImpresso to set
	 */
	public void setNotaEnvioImpresso(Boolean notaEnvioImpresso) {
		this.notaEnvioImpresso = notaEnvioImpresso;
	}

	/**
	 * @return the notaEnvioEmail
	 */
	public Boolean getNotaEnvioEmail() {
		return notaEnvioEmail;
	}

	/**
	 * @param notaEnvioEmail the notaEnvioEmail to set
	 */
	public void setNotaEnvioEmail(Boolean notaEnvioEmail) {
		this.notaEnvioEmail = notaEnvioEmail;
	}

	/**
	 * @return the chamadaEncalheImpresso
	 */
	public Boolean getChamadaEncalheImpresso() {
		return chamadaEncalheImpresso;
	}

	/**
	 * @param chamadaEncalheImpresso the chamadaEncalheImpresso to set
	 */
	public void setChamadaEncalheImpresso(Boolean chamadaEncalheImpresso) {
		this.chamadaEncalheImpresso = chamadaEncalheImpresso;
	}

	/**
	 * @return the chamadaEncalheEmail
	 */
	public Boolean getChamadaEncalheEmail() {
		return chamadaEncalheEmail;
	}

	/**
	 * @param chamadaEncalheEmail the chamadaEncalheEmail to set
	 */
	public void setChamadaEncalheEmail(Boolean chamadaEncalheEmail) {
		this.chamadaEncalheEmail = chamadaEncalheEmail;
	}

	/**
	 * @return the slipImpresso
	 */
	public Boolean getSlipImpresso() {
		return slipImpresso;
	}

	/**
	 * @param slipImpresso the slipImpresso to set
	 */
	public void setSlipImpresso(Boolean slipImpresso) {
		this.slipImpresso = slipImpresso;
	}

	/**
	 * @return the slipEmail
	 */
	public Boolean getSlipEmail() {
		return slipEmail;
	}

	/**
	 * @param slipEmail the slipEmail to set
	 */
	public void setSlipEmail(Boolean slipEmail) {
		this.slipEmail = slipEmail;
	}

	public boolean getProcuracaoAssinada() {
		return procuracaoAssinada;
	}

	public void setProcuracaoAssinada(boolean procuracaoAssinada) {
		this.procuracaoAssinada = procuracaoAssinada;
	}
}