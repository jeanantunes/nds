package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class ProdutoEdicaoSuplementarDTO implements Serializable {

	private static final long serialVersionUID = -2222673946429796619L;

	private String codigoProduto;
	
	private String nomeProdutoEdicao;
	
	private Long idProdutoEdicao;
	
	private Long numeroEdicao;
	
	private BigInteger reparte;
	
	private BigInteger quantidadeDisponivel;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
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
