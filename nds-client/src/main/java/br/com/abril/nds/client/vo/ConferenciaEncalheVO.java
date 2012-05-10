package br.com.abril.nds.client.vo;

import java.io.Serializable;


public class ConferenciaEncalheVO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String qtdExemplar;
	private String codigoDeBarras;
	private String codigoSM;
	private String codigo;
	private String nomeProduto;
	private String numeroEdicao;
	private String precoCapa;
	private String desconto;
	private String valorTotal;
	private String dia;
	private boolean juramentada;
	
	
	public String getQtdExemplar() {
		return qtdExemplar;
	}
	public void setQtdExemplar(String qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}
	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
	public String getCodigoSM() {
		return codigoSM;
	}
	public void setCodigoSM(String codigoSM) {
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
	public String getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public String getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}
	public String getDesconto() {
		return desconto;
	}
	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}
	public String getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public boolean isJuramentada() {
		return juramentada;
	}
	public void setJuramentada(boolean juramentada) {
		this.juramentada = juramentada;
	}
	
}
