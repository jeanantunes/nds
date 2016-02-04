package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ExtratificacaoItem extends Extratificacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 122019743830797196L;

	private BigInteger idCota;
	
	private Integer numeroCota;
	
	private String nomeProduto;
	
	private String codigoProduto;
	
	private BigInteger numeroEdicao;
	
	private BigDecimal qtdReparte;
	
	private BigDecimal qtdEncalhe;
	
	private BigDecimal qtdVenda;
	
	private BigInteger qtdPDV;
	
	private Date dataRecolhimento;
	
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

	public BigInteger getQtdPDV() {
		return qtdPDV;
	}

	public void setQtdPDV(BigInteger qtdPDV) {
		this.qtdPDV = qtdPDV;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
}