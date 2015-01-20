package br.com.abril.nds.integracao.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ExtratificacaoItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idCota;
	
	private Integer numeroCota;
	
	private String nomeProduto;
	
	private String codigoProduto;
	
	private Long numeroEdicao;
	
	private BigInteger qtdReparte;
	
	private BigInteger qtdEncalhe;
	
	private BigInteger qtdVenda;
	
	private BigInteger qtdJornaleiros;
	
	private BigDecimal percentualDesconto;

	private Date dataAprovacao;

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
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

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getQtdReparte() {
		return qtdReparte;
	}

	public void setQtdReparte(BigInteger qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	public BigInteger getQtdEncalhe() {
		return qtdEncalhe;
	}

	public void setQtdEncalhe(BigInteger qtdEncalhe) {
		this.qtdEncalhe = qtdEncalhe;
	}

	public BigInteger getQtdVenda() {
		return qtdVenda;
	}

	public void setQtdVenda(BigInteger qtdVenda) {
		this.qtdVenda = qtdVenda;
	}

	public BigInteger getQtdJornaleiros() {
		return qtdJornaleiros;
	}

	public void setQtdJornaleiros(BigInteger qtdJornaleiros) {
		this.qtdJornaleiros = qtdJornaleiros;
	}

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}
}