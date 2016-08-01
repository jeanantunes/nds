package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class ParametroDistribuicaoCota implements Serializable {

	private static final long serialVersionUID = 7170591493362816632L;
	
	@Column(name = "QTDE_PDV", nullable = true)
	private Integer qtdePDV;
	
	@Column(name = "ASSISTENTE_COMERCIAL", nullable = true)
	private String assistenteComercial;
	
	@Column(name = "GERENTE_COMERCIAL", nullable = true)
	private String gerenteComercial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DESCRICAO_TIPO_ENTREGA", nullable = true)
	private DescricaoTipoEntrega descricaoTipoEntrega;
	
	@Column(name = "OBSERVACAO", nullable = true)
	private String observacao;
		
	@Column(name = "REPARTE_POR_PONTO_VENDA", nullable = true)
	private Boolean repartePorPontoVenda;
	
	@Column(name = "SOLICITA_NUM_ATRASADOS", nullable = true)
	private Boolean solicitaNumAtras;
	
	@Column(name = "RECEBE_RECOLHE_PARCIAIS", nullable = true)
	private Boolean recebeRecolheParciais;
	
	@Column(name = "RECEBE_COMPLEMENTAR",nullable = false, columnDefinition = "boolean default false")
	private Boolean recebeComplementar = true;
	
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
	
	@Column(name = "BOLETO_IMPRESSO", nullable = true)
	private Boolean boletoImpresso;
	
	@Column(name = "BOLETO_EMAIL", nullable = true)
	private Boolean boletoEmail;
	
	@Column(name = "BOLETO_SLIP_IMPRESSO", nullable = true)
	private Boolean boletoSlipImpresso;
	
	@Column(name = "BOLETO_SLIP_EMAIL", nullable = true)
	private Boolean boletoSlipEmail;
	
	@Column(name = "RECIBO_IMPRESSO", nullable = true)
	private Boolean reciboImpresso;
	
	@Column(name = "RECIBO_EMAIL", nullable = true)
	private Boolean reciboEmail;
	
	@Column(name = "UTILIZA_DOCS_PARAMETRO_DISTRIB", nullable = false, columnDefinition = "boolean default true")
	private Boolean utilizaDocsParametrosDistribuidor = true;

	@Column(name = "UTILIZA_TERMO_ADESAO")
    private Boolean utilizaTermoAdesao;
	
	@Column(name = "TERMO_ADESAO_RECEBIDO")
    private Boolean termoAdesaoRecebido;
	
	@Column(name = "UTILIZA_PROCURACAO")
    private Boolean utilizaProcuracao;
	
	@Column(name = "PROCURACAO_RECEBIDA")
    private Boolean procuracaoRecebida;
	
    @Column(name = "INICIO_PERIODO_CARENCIA")
    @Temporal(TemporalType.DATE)
    private Date inicioPeriodoCarencia;
    
    @Column(name = "FIM_PERIODO_CARENCIA")
    @Temporal(TemporalType.DATE)
    private Date fimPeriodoCarencia;

    @Column(name = "ENDERECO_LED")
    private Integer enderecoLED;
	
	public ParametroDistribuicaoCota(){
		
	}
	
	public ParametroDistribuicaoCota(Integer qtdePDV,
			String assistenteComercial, DescricaoTipoEntrega descricaoTipoEntrega,
			String observacao,
			Boolean repartePorPontoVenda, Boolean solicitaNumAtras,
			Boolean recebeRecolheParciais, Boolean notaEnvioImpresso,
			Boolean notaEnvioEmail, Boolean chamadaEncalheImpresso,
			Boolean chamadaEncalheEmail, Boolean slipImpresso, Boolean slipEmail) {
		super();
		this.qtdePDV = qtdePDV;
		this.assistenteComercial = assistenteComercial;
		this.descricaoTipoEntrega = descricaoTipoEntrega;
		this.observacao = observacao;
		this.repartePorPontoVenda = repartePorPontoVenda;
		this.solicitaNumAtras = solicitaNumAtras;
		this.recebeRecolheParciais = recebeRecolheParciais;
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

	public String getGerenteComercial() {
		return gerenteComercial;
	}

	public void setGerenteComercial(String gerenteComercial) {
		this.gerenteComercial = gerenteComercial;
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
	public Boolean getRecebeRecolheParciais() {
		return recebeRecolheParciais;
	}

	/**
	 * @param recebeRecolheParcias the recebeRecolheParcias to set
	 */
	public void setRecebeRecolheParciais(Boolean recebeRecolheParciais) {
		this.recebeRecolheParciais = recebeRecolheParciais;
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

	/**
	 * @return the utilizaTermoAdesao
	 */
	public Boolean getUtilizaTermoAdesao() {
		return utilizaTermoAdesao;
	}

	/**
	 * @param utilizaTermoAdesao the utilizaTermoAdesao to set
	 */
	public void setUtilizaTermoAdesao(Boolean utilizaTermoAdesao) {
		this.utilizaTermoAdesao = utilizaTermoAdesao;
	}

	/**
	 * @return the termoAdesaoRecebido
	 */
	public Boolean getTermoAdesaoRecebido() {
		return termoAdesaoRecebido;
	}

	/**
	 * @param termoAdesaoRecebido the termoAdesaoRecebido to set
	 */
	public void setTermoAdesaoRecebido(Boolean termoAdesaoRecebido) {
		this.termoAdesaoRecebido = termoAdesaoRecebido;
	}

	/**
	 * @return the utilizaProcuracao
	 */
	public Boolean getUtilizaProcuracao() {
		return utilizaProcuracao;
	}

	/**
	 * @param utilizaProcuracao the utilizaProcuracao to set
	 */
	public void setUtilizaProcuracao(Boolean utilizaProcuracao) {
		this.utilizaProcuracao = utilizaProcuracao;
	}

	/**
	 * @return the procuracaoRecebida
	 */
	public Boolean getProcuracaoRecebida() {
		return procuracaoRecebida;
	}

	/**
	 * @param procuracaoRecebida the procuracaoRecebida to set
	 */
	public void setProcuracaoRecebida(Boolean procuracaoRecebida) {
		this.procuracaoRecebida = procuracaoRecebida;
	}

	public Boolean getBoletoImpresso() {
		return boletoImpresso;
	}

	public void setBoletoImpresso(Boolean boletoImpresso) {
		this.boletoImpresso = boletoImpresso;
	}

	public Boolean getBoletoEmail() {
		return boletoEmail;
	}

	public void setBoletoEmail(Boolean boletoEmail) {
		this.boletoEmail = boletoEmail;
	}

	public Boolean getBoletoSlipImpresso() {
		return boletoSlipImpresso;
	}

	public void setBoletoSlipImpresso(Boolean boletoSlipImpresso) {
		this.boletoSlipImpresso = boletoSlipImpresso;
	}

	public Boolean getBoletoSlipEmail() {
		return boletoSlipEmail;
	}

	public void setBoletoSlipEmail(Boolean boletoSlipEmail) {
		this.boletoSlipEmail = boletoSlipEmail;
	}

	public Boolean getReciboImpresso() {
		return reciboImpresso;
	}

	public void setReciboImpresso(Boolean reciboImpresso) {
		this.reciboImpresso = reciboImpresso;
	}

	public Boolean getReciboEmail() {
		return reciboEmail;
	}

	public void setReciboEmail(Boolean reciboEmail) {
		this.reciboEmail = reciboEmail;
	}
	
	/**
	 * @return the inicioPeriodoCarencia
	 */
	public Date getInicioPeriodoCarencia() {
		return inicioPeriodoCarencia;
	}

	/**
	 * @param inicioPeriodoCarencia the inicioPeriodoCarencia to set
	 */
	public void setInicioPeriodoCarencia(Date inicioPeriodoCarencia) {
		this.inicioPeriodoCarencia = inicioPeriodoCarencia;
	}

	/**
	 * @return the fimPeriodoCarencia
	 */
	public Date getFimPeriodoCarencia() {
		return fimPeriodoCarencia;
	}

	/**
	 * @param fimPeriodoCarencia the fimPeriodoCarencia to set
	 */
	public void setFimPeriodoCarencia(Date fimPeriodoCarencia) {
		this.fimPeriodoCarencia = fimPeriodoCarencia;
	}

	/**
	 * Obtém descricaoTipoEntrega
	 *
	 * @return DescricaoTipoEntrega
	 */
	public DescricaoTipoEntrega getDescricaoTipoEntrega() {
		return descricaoTipoEntrega;
	}

	/**
	 * Atribuí descricaoTipoEntrega
	 * @param descricaoTipoEntrega 
	 */
	public void setDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		this.descricaoTipoEntrega = descricaoTipoEntrega;
	}
	
	/**
	 * Obtém recebeComplementar
	 * @return
	 */
	public Boolean getRecebeComplementar() {
		return recebeComplementar;
	}
	
	/**
	 * Atribuí recebeComplementar
	 * @param recebeComplementar
	 */
	public void setRecebeComplementar(Boolean recebeComplementar) {
		this.recebeComplementar = recebeComplementar;
	}

	public Boolean getUtilizaDocsParametrosDistribuidor() {
		return utilizaDocsParametrosDistribuidor;
	}

	public void setUtilizaDocsParametrosDistribuidor(Boolean utilizaDocsParametrosDistribuidor) {
		this.utilizaDocsParametrosDistribuidor = utilizaDocsParametrosDistribuidor;
	}

	public Integer getEnderecoLED() {
		return enderecoLED;
	}

	public void setEnderecoLED(Integer enderecoLED) {
		this.enderecoLED = enderecoLED;
	}
}