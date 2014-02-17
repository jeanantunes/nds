package br.com.abril.nds.dto;

import java.math.BigInteger;

public class EdicaoBaseEstudoDTO {

	private String codigoProduto;
	private String nomeProduto;
	private BigInteger numeroEdicao;
	private BigInteger peso;

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

}
