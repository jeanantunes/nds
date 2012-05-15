package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConferenciaEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6012294358522142934L;
	
	private Long idConferenciaEncalhe;
	private Long idProdutoEdicao;
	private BigDecimal qtdExemplar;
	private String codigoDeBarras;
	private Long codigoSM;
	private String codigo;
	private String nomeProduto;
	private Long numeroEdicao;
	private BigDecimal precoCapa;
	private BigDecimal desconto;
	private BigDecimal valorTotal;
	private Integer dia;
	private String observacao;
	private boolean juramentada;
	
	
	public BigDecimal getQtdExemplar() {
		return qtdExemplar;
	}
	public void setQtdExemplar(BigDecimal qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}
	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
	public Long getCodigoSM() {
		return codigoSM;
	}
	public void setCodigoSM(Long codigoSM) {
		this.codigoSM = codigoSM;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public boolean isJuramentada() {
		return juramentada;
	}
	public void setJuramentada(boolean juramentada) {
		this.juramentada = juramentada;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Long getIdConferenciaEncalhe() {
		return idConferenciaEncalhe;
	}
	public void setIdConferenciaEncalhe(Long idConferenciaEncalhe) {
		this.idConferenciaEncalhe = idConferenciaEncalhe;
	}
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	
}
