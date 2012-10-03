package br.com.abril.nds.dto;

import java.math.BigInteger;

public class ProdutoEdicaoSuplementarDTO {

	private String codigoProdutoEdicao;
	
	private String nomeProdutoEdicao;
	
	private Long idProdutoEdicao;
	
	private Long numeroEdicao;
	
	private BigInteger reparte;
	
	private BigInteger quantidadeDisponivel;

	public String getCodigoProdutoEdicao() {
		return codigoProdutoEdicao;
	}

	public void setCodigoProdutoEdicao(String codigoProdutoEdicao) {
		this.codigoProdutoEdicao = codigoProdutoEdicao;
	}

	public String getNomeProdutoEdicao() {
		return nomeProdutoEdicao;
	}

	public void setNomeProdutoEdicao(String nomeProdutoEdicao) {
		this.nomeProdutoEdicao = nomeProdutoEdicao;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getQuantidadeDisponivel() {
		return quantidadeDisponivel;
	}

	public void setQuantidadeDisponivel(BigInteger quantidadeDisponivel) {
		this.quantidadeDisponivel = quantidadeDisponivel;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	
}
