package br.com.abril.nds.model.integracao.icd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.abril.nds.model.integracao.icd.pks.CEPK;

@Entity
@Table(name = "CHAMADA_ENCALHE")
public class IcdChamadaEncalhe {
	
	@EmbeddedId
	private CEPK cePK;
	
	@Transient
	private String tipoDocumento;
	
	@Transient
	private String baseDeDados;
	
	@Transient
	private String usuarioBaseDeDados;
	
	@Column(name = "COD_DISTRIBUIDOR", nullable = false)
	private Long codigoDistribuidor;
	
	@Column(name = "DATA_EMISSAO_CHEN")
	private Date dataEmissao;
	
	@Column(name = "DATA_ANO_REFERENCIA_CHEN")
	private Long dataAnoReferencia;
	
	@Column(name = "TIPO_STATUS_CHEN")
	private String tipoStatus;
	
	@Column(name = "COD_TIPO_CHAMADA_ENC_TPCE")
	private Long codigoTipoChamadaEncalhe;
	
	@Column(name = "COD_NATUREZA_OPERACAO_NTOP")
	private Long codigoNaturezaOperacao;
	
	@Column(name = "NUM_SEMANA_REFERENCIA_CHEN")
	private Long numeroSemanaReferencia;
	
	@Column(name = "NUM_CONTROLE_CHEN")
	private Long numeroControle;
	
	@Column(name = "DATA_LIMITE_RECEBIMENTO_CHEN")
	private Date dataLimiteRecebimento;
	
	@Column(name = "VLR_TOTAL_VENDA_APURADA_CHEN")
	private BigDecimal valorTotalVendaApurada;	
	
	@Column(name = "VLR_TOTAL_CREDITO_APURADO_CHEN")
	private BigDecimal valorTotalCreditoApurado;
	
	@Column(name = "COD_PREENCHIMENTO_CHEN")
	private String codigoPreenchimento;
	
	@Column(name = "VLR_TOTAL_VENDA_INFORMADA_CHEN")
	private BigDecimal valorTotalVendaInformada;
	
	@Column(name = "VLR_TOTAL_CRED_INFORMADO_CHEN")
	private BigDecimal valorTotalCreditoInformado;
	
	@Column(name = "VLR_TOT_MARGEM_INFORMADO_CHEN")
	private BigDecimal valorTotalMargemInformado;
	
	@Column(name = "VLR_TOT_MARGEM_APURADO_CHEN")
	private BigDecimal valorTotalMargemApurado;
	
	@Column(name = "VLR_NOTA_VALORES_DIVERSOS_CHEN")
	private BigDecimal valorNotaValoresDiversos;
	
	@Column(name = "COD_STATUS_TRANSMISSAO_CHEN")
	private String codigoStatusTransmissao;
	
	@Column(name = "IND_FECHTO_PARCIAL_CHEN")
	private String indiceFechamentoParcial;
	
	@Column(name = "IND_CE_PROVISORIA_CHEN")
	private String indiceCEProvisoria;
	
	@OneToMany(mappedBy="ceItemPK.numeroChamadaEncalhe")
	List<IcdChamadaEncalheItem> chamadaEncalheItens;

	/**
	 * Getters e Setters 
	 */
	public CEPK getCePK() {
		return cePK;
	}

	public void setCePK(CEPK cePK) {
		this.cePK = cePK;
	}
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getBaseDeDados() {
		return baseDeDados;
	}

	public void setBaseDeDados(String baseDeDados) {
		this.baseDeDados = baseDeDados;
	}

	public String getUsuarioBaseDeDados() {
		return usuarioBaseDeDados;
	}

	public void setUsuarioBaseDeDados(String usuarioBaseDeDados) {
		this.usuarioBaseDeDados = usuarioBaseDeDados;
	}

	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Long getDataAnoReferencia() {
		return dataAnoReferencia;
	}

	public void setDataAnoReferencia(Long dataAnoReferencia) {
		this.dataAnoReferencia = dataAnoReferencia;
	}
	
	public String getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(String tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public Long getCodigoTipoChamadaEncalhe() {
		return codigoTipoChamadaEncalhe;
	}

	public void setCodigoTipoChamadaEncalhe(Long codigoTipoChamadaEncalhe) {
		this.codigoTipoChamadaEncalhe = codigoTipoChamadaEncalhe;
	}

	public Long getCodigoNaturezaOperacao() {
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(Long codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public Long getNumeroSemanaReferencia() {
		return numeroSemanaReferencia;
	}

	public void setNumeroSemanaReferencia(Long numeroSemanaReferencia) {
		this.numeroSemanaReferencia = numeroSemanaReferencia;
	}

	public Long getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Long numeroControle) {
		this.numeroControle = numeroControle;
	}

	public Date getDataLimiteRecebimento() {
		return dataLimiteRecebimento;
	}

	public void setDataLimiteRecebimento(Date dataLimiteRecebimento) {
		this.dataLimiteRecebimento = dataLimiteRecebimento;
	}

	public BigDecimal getValorTotalVendaApurada() {
		return valorTotalVendaApurada;
	}

	public void setValorTotalVendaApurada(BigDecimal valorTotalVendaApurada) {
		this.valorTotalVendaApurada = valorTotalVendaApurada;
	}

	public BigDecimal getValorTotalCreditoApurado() {
		return valorTotalCreditoApurado;
	}

	public void setValorTotalCreditoApurado(BigDecimal valorTotalCreditoApurado) {
		this.valorTotalCreditoApurado = valorTotalCreditoApurado;
	}

	public String getCodigoPreenchimento() {
		return codigoPreenchimento;
	}

	public void setCodigoPreenchimento(String codigoPreenchimento) {
		this.codigoPreenchimento = codigoPreenchimento;
	}

	public BigDecimal getValorTotalVendaInformada() {
		return valorTotalVendaInformada;
	}

	public void setValorTotalVendaInformada(BigDecimal valorTotalVendaInformada) {
		this.valorTotalVendaInformada = valorTotalVendaInformada;
	}

	public BigDecimal getValorTotalCreditoInformado() {
		return valorTotalCreditoInformado;
	}

	public void setValorTotalCreditoInformado(BigDecimal valorTotalCreditoInformado) {
		this.valorTotalCreditoInformado = valorTotalCreditoInformado;
	}

	public BigDecimal getValorTotalMargemInformado() {
		return valorTotalMargemInformado;
	}

	public void setValorTotalMargemInformado(BigDecimal valorTotalMargemInformado) {
		this.valorTotalMargemInformado = valorTotalMargemInformado;
	}

	public BigDecimal getValorTotalMargemApurado() {
		return valorTotalMargemApurado;
	}

	public void setValorTotalMargemApurado(BigDecimal valorTotalMargemApurado) {
		this.valorTotalMargemApurado = valorTotalMargemApurado;
	}

	public BigDecimal getValorNotaValoresDiversos() {
		return valorNotaValoresDiversos;
	}

	public void setValorNotaValoresDiversos(BigDecimal valorNotaValoresDiversos) {
		this.valorNotaValoresDiversos = valorNotaValoresDiversos;
	}

	public String getCodigoStatusTransmissao() {
		return codigoStatusTransmissao;
	}

	public void setCodigoStatusTransmissao(String codigoStatusTransmissao) {
		this.codigoStatusTransmissao = codigoStatusTransmissao;
	}

	public String getIndiceFechamentoParcial() {
		return indiceFechamentoParcial;
	}

	public void setIndiceFechamentoParcial(String indiceFechamentoParcial) {
		this.indiceFechamentoParcial = indiceFechamentoParcial;
	}

	public String getIndiceCEProvisoria() {
		return indiceCEProvisoria;
	}

	public void setIndiceCEProvisoria(String indiceCEProvisoria) {
		this.indiceCEProvisoria = indiceCEProvisoria;
	}

	public List<IcdChamadaEncalheItem> getChamadaEncalheItens() {
		return chamadaEncalheItens;
	}

	public void setChamadaEncalheItens(List<IcdChamadaEncalheItem> chamadaEncalheItens) {
		this.chamadaEncalheItens = chamadaEncalheItens;
	}
	
}
