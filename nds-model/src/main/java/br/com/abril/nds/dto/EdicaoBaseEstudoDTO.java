package br.com.abril.nds.dto;

import java.math.BigInteger;

public class EdicaoBaseEstudoDTO {

	private String codigoProduto;
	private String nomeProduto;
	private BigInteger numeroEdicao;
	private BigInteger peso;
	private boolean isParcial;
	private boolean isParcialConsolidado;
	private Long idProdutoEdicao;
	private Long periodoParcial;
	private boolean isEdicaoAberta;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getPeso() {
		return peso;
	}

	public void setPeso(BigInteger peso) {
		this.peso = peso;
	}

	public boolean isParcial() {
		return isParcial;
	}

	public void setParcial(boolean isParcial) {
		this.isParcial = isParcial;
	}

	public boolean isParcialConsolidado() {
		return isParcialConsolidado;
	}

	public void setParcialConsolidado(boolean isParcialConsolidado) {
		this.isParcialConsolidado = false;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public Long getPeriodoParcial() {
		return periodoParcial;
	}

	public void setPeriodoParcial(Long periodoParcial) {
		this.periodoParcial = periodoParcial;
	}

	public boolean isEdicaoAberta() {
		return isEdicaoAberta;
	}

	public void setEdicaoAberta(boolean isEdicaoAberta) {
		this.isEdicaoAberta = isEdicaoAberta;
	}
	
}
