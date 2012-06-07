package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;

public class FechamentoFisicoLogicoDTO {

	private String codigo;
	private String produto;
	private Long edicao;
	private BigDecimal precoCapa;
	private BigDecimal exemplaresDevolucao;
	private BigDecimal total;
	private Long fisico = Long.valueOf(0);
	private Long diferenca = Long.valueOf(0);
	
	private String precoCapaFormatado;
	private String exemplaresDevolucaoFormatado;
	private String totalFormatado;
	private String replicar = "";
	
	
	public String getReplicar() {
		return replicar;
	}

	public String getPrecoCapaFormatado() {
		return this.precoCapaFormatado;
	}
	
	public String getExemplaresDevolucaoFormatado() {
		return this.exemplaresDevolucaoFormatado;
	}
	
	public String getTotalFormatado() {
		return this.totalFormatado;
	}
	
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		this.precoCapaFormatado = CurrencyUtil.formatarValor(this.precoCapa); 
	}
	public BigDecimal getExemplaresDevolucao() {
		return exemplaresDevolucao;
	}
	public void setExemplaresDevolucao(BigDecimal exemplaresDevolucao) {
		this.exemplaresDevolucao = exemplaresDevolucao;
		this.exemplaresDevolucaoFormatado = CurrencyUtil.formatarValorTruncado(this.exemplaresDevolucao).replaceAll("\\D", "");
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
		this.totalFormatado = CurrencyUtil.formatarValor(this.total); 
	}
	public Long getFisico() {
		return fisico;
	}
	public void setFisico(Long fisico) {
		this.fisico = fisico;
	}
	public Long getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(Long diferenca) {
		this.diferenca = diferenca;
	}
}
