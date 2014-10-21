package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public class AbastecimentoBoxCotaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private BigInteger idProdutoEdicao;
    private BigInteger numeroEdicao;
    private Integer numeroCota;
    private String nomeProduto;
    private String codigoProduto;
    private BigDecimal reparte;
    private Set<String> produtos;
    private Set<Integer> cotas;
    private List<Integer> repartes;
	
    public BigInteger getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	
	public void setIdProdutoEdicao(BigInteger idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}
	
	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public Set<String> getProdutos() {
		return produtos;
	}

	public void setProdutos(Set<String> produtos) {
		this.produtos = produtos;
	}

	public Set<Integer> getCotas() {
		return cotas;
	}

	public void setCotas(Set<Integer> cotas) {
		this.cotas = cotas;
	}

	public List<Integer> getRepartes() {
		return repartes;
	}

	public void setRepartes(List<Integer> repartes) {
		this.repartes = repartes;
	}
}