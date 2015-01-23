package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class ItemImpressaoNfe{
	
	private String codigoProduto;

	private String descricaoProduto;
	
	private Long produtoEdicao; 

	private String NCMProduto;

	private String CFOPProduto;

	private String unidadeProduto;

	private BigDecimal quantidadeProduto;

	private BigDecimal valorUnitarioProduto;

	private BigDecimal valorTotalProduto;
	
	private BigDecimal valorDescontoProduto;
	
	private BigDecimal percentualDesconto;

	private String CSTProduto;

	private String CSOSNProduto;

	private BigDecimal baseCalculoProduto;

	private BigDecimal aliquotaICMSProduto;

	private BigDecimal valorICMSProduto;

	private BigDecimal aliquotaIPIProduto;

	private BigDecimal valorIPIProduto;
	
	private Integer sequencia;
	
	private String codigoBarra;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public Long getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(Long produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public String getNCMProduto() {
		return NCMProduto;
	}

	public void setNCMProduto(String nCMProduto) {
		NCMProduto = nCMProduto;
	}

	public String getCFOPProduto() {
		return CFOPProduto;
	}

	public void setCFOPProduto(String cFOPProduto) {
		CFOPProduto = cFOPProduto;
	}

	public String getUnidadeProduto() {
		return unidadeProduto;
	}

	public void setUnidadeProduto(String unidadeProduto) {
		this.unidadeProduto = unidadeProduto;
	}
	
	public BigDecimal getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(BigDecimal quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public BigDecimal getValorUnitarioProduto() {
		return valorUnitarioProduto;
	}

	public void setValorUnitarioProduto(BigDecimal valorUnitarioProduto) {
		this.valorUnitarioProduto = valorUnitarioProduto;
	}

	public BigDecimal getValorTotalProduto() {
		return valorTotalProduto;
	}

	public void setValorTotalProduto(BigDecimal valorTotalProduto) {
		this.valorTotalProduto = valorTotalProduto;
	}

	public BigDecimal getValorDescontoProduto() {
		return valorDescontoProduto;
	}

	public void setValorDescontoProduto(BigDecimal valorDescontoProduto) {
		this.valorDescontoProduto = valorDescontoProduto;
	}

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	public String getCSTProduto() {
		return CSTProduto;
	}

	public void setCSTProduto(String cSTProduto) {
		CSTProduto = cSTProduto;
	}

	public String getCSOSNProduto() {
		return CSOSNProduto;
	}

	public void setCSOSNProduto(String cSOSNProduto) {
		CSOSNProduto = cSOSNProduto;
	}

	public BigDecimal getBaseCalculoProduto() {
		return baseCalculoProduto;
	}

	public void setBaseCalculoProduto(BigDecimal baseCalculoProduto) {
		this.baseCalculoProduto = baseCalculoProduto;
	}

	public BigDecimal getAliquotaICMSProduto() {
		return aliquotaICMSProduto;
	}

	public void setAliquotaICMSProduto(BigDecimal aliquotaICMSProduto) {
		this.aliquotaICMSProduto = aliquotaICMSProduto;
	}

	public BigDecimal getValorICMSProduto() {
		return valorICMSProduto;
	}

	public void setValorICMSProduto(BigDecimal valorICMSProduto) {
		this.valorICMSProduto = valorICMSProduto;
	}

	public BigDecimal getAliquotaIPIProduto() {
		return aliquotaIPIProduto;
	}

	public void setAliquotaIPIProduto(BigDecimal aliquotaIPIProduto) {
		this.aliquotaIPIProduto = aliquotaIPIProduto;
	}

	public BigDecimal getValorIPIProduto() {
		return valorIPIProduto;
	}

	public void setValorIPIProduto(BigDecimal valorIPIProduto) {
		this.valorIPIProduto = valorIPIProduto;
	}

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	/**
	 * @return the codigoBarra
	 */
	public String getCodigoBarra() {
		return codigoBarra;
	}

	/**
	 * @param codigoBarra the codigoBarra to set
	 */
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}	
	
}