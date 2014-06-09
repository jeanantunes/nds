package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.estoque.TipoDiferenca;

public class ContasAPagarConsignadoDTO implements Serializable {

	private static final long serialVersionUID = 796550216964502238L;
	
	private String codigo;
	private String produto;
	private Long edicao;
	private BigDecimal precoCapa;
	private BigDecimal precoComDesconto;
	private BigInteger reparteSugerido;
	private BigInteger reparteFinal;
	private BigInteger diferenca;
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
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}
	public BigInteger getReparteSugerido() {
		return reparteSugerido;
	}
	public void setReparteSugerido(BigInteger reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}
	public BigInteger getReparteFinal() {
		return reparteFinal;
	}
	public void setReparteFinal(BigInteger reparteFinal) {
		this.reparteFinal = reparteFinal;
	}
	public BigInteger getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(BigInteger diferenca) {
		this.diferenca = diferenca;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(TipoDiferenca motivo) {
		this.motivo = motivo == null ? "" : motivo.getDescricao();
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
