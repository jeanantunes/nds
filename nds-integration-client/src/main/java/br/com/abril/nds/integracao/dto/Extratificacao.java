package br.com.abril.nds.integracao.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class Extratificacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String tipoDocumento;
	
	private String codigoDistribuidor;
	
	private String codigoProduto;
	
	private Integer numeroCota;
	
	private Long numeroEdicao;
	
	private BigInteger qtdReparte;
	
	private BigInteger qtdEncalhe;
	
	private BigInteger qtdJornaleiros;
	
	private BigDecimal percentualDesconto;
	
	private Date dataOperacao;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
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

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
}