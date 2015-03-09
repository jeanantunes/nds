package br.com.abril.nds.model.fiscal.notafiscal;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class ValoresCalculadosWrapper {
	
	@Column(name = "VALOR_BASE_ICMS")
	@XmlElement(name="vBC")
	private	BigDecimal valorBaseICMS;
	
	@Column(name = "VALOR_ICMS")
	@XmlElement(name="vICMS")
	private	BigDecimal valorICMS;
	
	@Column(name = "VALOR_ICMS_DESON")
	@XmlElement(name="vICMSDeson")
	private	BigDecimal vICMSDeson;
	
	@Column(name = "VALOR_BASE_ICMS_SUBSTITUTO")
	@XmlElement(name="vBCST")
	private	BigDecimal valorBaseICMSSubstituto;
	
	@Column(name = "VALOR_ICMS_SUBSTITUTO")
	@XmlElement(name="vST")
	private	BigDecimal valorICMSSubstituto;
	
	@Column(name = "VALOR_PRODUTOS")
	@XmlElement(name="vProd")
	private	BigDecimal valorProdutos;
	
	@Column(name = "VALOR_FRETE")
	@XmlElement(name="vFrete")
	private	BigDecimal valorFrete;
	
	@Column(name = "VALOR_SEGURO")
	@XmlElement(name="vSeg")
	private	BigDecimal valorSeguro;
	
	@Column(name = "VALOR_DESCONTO", nullable = false, precision=18, scale=4)
	@XmlElement(name="vDesc")
	protected BigDecimal valorDesconto;	
	
	@Column(name = "VALOR_IMPOSTO_IMPORTACAO", nullable = false, precision=18, scale=4)
	@XmlElement(name="vII")
	protected BigDecimal valorImpostoImportacao;	
	
	@Column(name = "VALOR_IPI")
	@XmlElement(name="vIPI")
	private	BigDecimal valorIPI;
	
	@Column(name="VALOR_PIS")
	@XmlElement(name="vPIS")
	private BigDecimal valorPIS;
	
	@Column(name="VALOR_COFINS", nullable=false, precision=18, scale=4)
	@XmlElement(name="vCOFINS")
	private BigDecimal valorCOFINS;

	@Column(name = "VALOR_OUTRO")
	@XmlElement(name="vOutro")
	private	BigDecimal valorOutro;
	
	@Column(name = "VALOR_NF")
	@XmlElement(name="vNF")
	private	BigDecimal valorNF;
	
	@Column(name = "ISSQN_TOTAL")
	@XmlTransient
	private	BigDecimal ISSQNTotal;
	
	@Column(name = "ISSQN_BASE")
	@XmlTransient
	private	BigDecimal ISSQNBase;
	
	@Column(name = "ISSQN_VALOR")
	@XmlTransient
	private	BigDecimal ISSQNValor;
	
	public BigDecimal getISSQNTotal() {
		return ISSQNTotal;
	}
	
	public BigDecimal getValorBaseICMS() {
		return valorBaseICMS;
	}

	public void setValorBaseICMS(BigDecimal valorBaseICMS) {
		this.valorBaseICMS = valorBaseICMS;
	}

	public BigDecimal getValorICMS() {
		return valorICMS;
	}

	public void setValorICMS(BigDecimal valorICMS) {
		this.valorICMS = valorICMS;
	}
	
	public BigDecimal getvICMSDeson() {
		return vICMSDeson;
	}

	public void setvICMSDeson(BigDecimal vICMSDeson) {
		this.vICMSDeson = vICMSDeson;
	}
	
	public BigDecimal getValorBaseICMSSubstituto() {
		return valorBaseICMSSubstituto;
	}

	public void setValorBaseICMSSubstituto(BigDecimal valorBaseICMSSubstituto) {
		this.valorBaseICMSSubstituto = valorBaseICMSSubstituto;
	}

	public BigDecimal getValorICMSSubstituto() {
		return valorICMSSubstituto;
	}

	public void setValorICMSSubstituto(BigDecimal valorICMSSubstituto) {
		this.valorICMSSubstituto = valorICMSSubstituto;
	}

	public BigDecimal getValorProdutos() {
		return valorProdutos;
	}

	public void setValorProdutos(BigDecimal valorProdutos) {
		this.valorProdutos = valorProdutos;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorOutro() {
		return valorOutro;
	}

	public void setValorOutro(BigDecimal valorOutro) {
		this.valorOutro = valorOutro;
	}

	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	public BigDecimal getValorNF() {
		return valorNF;
	}

	public void setValorNF(BigDecimal valorNF) {
		this.valorNF = valorNF;
	}

	public void setISSQNTotal(BigDecimal iSSQNTotal) {
		ISSQNTotal = iSSQNTotal;
	}

	public BigDecimal getISSQNBase() {
		return ISSQNBase;
	}

	public void setISSQNBase(BigDecimal iSSQNBase) {
		ISSQNBase = iSSQNBase;
	}

	public BigDecimal getISSQNValor() {
		return ISSQNValor;
	}

	public void setISSQNValor(BigDecimal iSSQNValor) {
		ISSQNValor = iSSQNValor;
	}
	
	public BigDecimal getValorPIS() {
		return valorPIS;
	}

	public void setValorPIS(BigDecimal valorPIS) {
		this.valorPIS = valorPIS;
	}

	public BigDecimal getValorCOFINS() {
		return valorCOFINS;
	}

	public void setValorCOFINS(BigDecimal valorCOFINS) {
		this.valorCOFINS = valorCOFINS;
	}

	public BigDecimal getValorImpostoImportacao() {
		return valorImpostoImportacao;
	}

	public void setValorImpostoImportacao(BigDecimal valorImpostoImportacao) {
		this.valorImpostoImportacao = valorImpostoImportacao;
	}

	
	
}
