package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class FechamentoFisicoLogicoDTO {

	private String codigo;
	private String produto;
	private Long edicao;
	private BigDecimal precoCapa;
	private BigDecimal exemplaresDevolucao;
	private BigDecimal total;
	private Long fisico;
	private Long diferenca;
	
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
	}
	public BigDecimal getExemplaresDevolucao() {
		return exemplaresDevolucao;
	}
	public void setExemplaresDevolucao(BigDecimal exemplaresDevolucao) {
		this.exemplaresDevolucao = exemplaresDevolucao;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
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
