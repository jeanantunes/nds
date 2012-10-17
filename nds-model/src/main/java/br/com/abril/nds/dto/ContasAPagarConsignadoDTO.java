package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ContasAPagarConsignadoDTO implements Serializable {

	private static final long serialVersionUID = 796550216964502238L;
	
	private String codigo;
	private String produto;
	private Integer edicao;
	private BigDecimal precoCapa;
	private BigDecimal precoComDesconto;
	private Integer reparteSugerido;
	private Integer reparteFinal;
	private Integer diferenca;
	private String motivo;
	private String fornecedor;
	private BigDecimal valor;
	private BigDecimal valorComDesconto;
	private String nfe;
	
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
	public Integer getEdicao() {
		return edicao;
	}
	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}
	public Integer getReparteSugerido() {
		return reparteSugerido;
	}
	public void setReparteSugerido(Integer reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}
	public Integer getReparteFinal() {
		return reparteFinal;
	}
	public void setReparteFinal(Integer reparteFinal) {
		this.reparteFinal = reparteFinal;
	}
	public Integer getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(Integer diferenca) {
		this.diferenca = diferenca;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValorComDesconto() {
		return valorComDesconto;
	}
	public void setValorComDesconto(BigDecimal valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}
	public String getNfe() {
		return nfe;
	}
	public void setNfe(String nfe) {
		this.nfe = nfe;
	}
}
