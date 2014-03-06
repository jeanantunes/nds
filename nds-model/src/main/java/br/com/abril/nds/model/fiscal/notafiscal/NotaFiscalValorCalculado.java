package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Entity
@Table(name="NOTA_FISCAL_VALOR_CALCULADO")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscalValorCalculado implements Serializable {
	
	private static final long serialVersionUID = -5476612050904249652L;
	
	@Id
	@GeneratedValue()
	@Column(name="ID")
	private Long id;
	
	@Column(name = "VALOR_BASE_ICMS")
	private	BigDecimal valorBaseICMS;
	
	@Column(name = "VALOR_ICMS")
	private	BigDecimal valorICMS;
	
	@Column(name = "VALOR_BASE_ICMS_SUBSTITUTO")
	private	BigDecimal valorBaseICMSSubstituto;
	
	@Column(name = "VALOR_ICMS_SUBSTITUTO")
	private	BigDecimal valorICMSSubstituto;
	
	@Column(name = "VALOR_PRODUTOS")
	private	BigDecimal valorProdutos;
	
	@Column(name = "VALOR_FRETE")
	private	BigDecimal valorFrete;
	
	@Column(name = "VALOR_SEGURO")
	private	BigDecimal valorSeguro;
	
	@Column(name = "VALOR_DESCONTO", nullable = false, precision=18, scale=4)
	protected BigDecimal valorDesconto;	
	
	@Column(name = "VALOR_OUTRO")
	private	BigDecimal valorOutro;
	
	@Column(name = "VALOR_IPI")
	private	BigDecimal valorIPI;
	
	@Column(name = "VALOR_NF")
	private	BigDecimal valorNF;

	@Column(name = "ISSQN_TOTAL")
	private	BigDecimal ISSQNTotal;
	
	@Column(name = "ISSQN_BASE")
	private	BigDecimal ISSQNBase;
	
	@Column(name = "ISSQN_VALOR")
	private	BigDecimal ISSQNValor;
	
	@Column(name="VALOR_PIS")
	private BigDecimal valorPIS;
	
	@Column(name="VALOR_COFINS", nullable=false, precision=18, scale=4)
	private BigDecimal valorCOFINS;
	
	public BigDecimal getISSQNTotal() {
		return ISSQNTotal;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ValoresCalculados [id=" + id + ", valorBaseICMS="
				+ valorBaseICMS + ", valorICMS=" + valorICMS
				+ ", valorBaseICMSSubstituto=" + valorBaseICMSSubstituto
				+ ", valorICMSSubstituto=" + valorICMSSubstituto
				+ ", valorProdutos=" + valorProdutos + ", valorFrete="
				+ valorFrete + ", valorSeguro=" + valorSeguro
				+ ", valorDesconto=" + valorDesconto + ", valorOutro="
				+ valorOutro + ", valorIPI=" + valorIPI + ", valorNF="
				+ valorNF + ", ISSQNTotal=" + ISSQNTotal + ", ISSQNBase="
				+ ISSQNBase + ", ISSQNValor=" + ISSQNValor + "]";
	}
}
