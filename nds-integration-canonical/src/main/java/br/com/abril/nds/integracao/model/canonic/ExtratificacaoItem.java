package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ExtratificacaoItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigInteger idCota;
	
	private Integer numeroCota;
	
	private String nomeProduto;
	
	private String codigoProduto;
	
	private BigInteger numeroEdicao;
	
	private BigDecimal qtdReparte;
	
	private BigDecimal qtdEncalhe;
	
	private BigDecimal qtdVenda;
	
	private BigDecimal qtdJornaleiros;
	
	private BigDecimal percentualDesconto;

	private Date dataAprovacao;

	public BigInteger getIdCota() {
		return idCota;
	}

	public void setIdCota(BigInteger idCota) {
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

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getQtdReparte() {
		return qtdReparte;
	}

	public void setQtdReparte(BigDecimal qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	public BigDecimal getQtdEncalhe() {
		return qtdEncalhe;
	}

	public void setQtdEncalhe(BigDecimal qtdEncalhe) {
		this.qtdEncalhe = qtdEncalhe;
	}

	public BigDecimal getQtdVenda() {
		return qtdVenda;
	}

	public void setQtdVenda(BigDecimal qtdVenda) {
		this.qtdVenda = qtdVenda;
	}

	public BigDecimal getQtdJornaleiros() {
		return qtdJornaleiros;
	}

	public void setQtdJornaleiros(BigDecimal qtdJornaleiros) {
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