package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DetalhesPickingDTO {
	
	private Long idCota;
	
	private Integer numeroCota;
	
	private BigInteger qtdeMEC;
	
	private String codigoProduto;
	
	private Long codigoEdicao;
	
	private String nomeProduto;
	
	private String codigoDeBarrasProdutoEdicao;
	
	private BigDecimal precoCustoProdutoEdicao;
	
	private BigDecimal precoVendaProdutoEdicao;
	
	private BigDecimal valorDescontoMEC;
	
	private Integer sequenciaMatriz;

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

	public BigInteger getQtdeMEC() {
		return qtdeMEC;
	}

	public void setQtdeMEC(BigInteger qtdeMEC) {
		this.qtdeMEC = qtdeMEC;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getCodigoEdicao() {
		return codigoEdicao;
	}

	public void setCodigoEdicao(Long codigoEdicao) {
		this.codigoEdicao = codigoEdicao;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getPrecoCustoProdutoEdicao() {
		return precoCustoProdutoEdicao;
	}

	public void setPrecoCustoProdutoEdicao(BigDecimal precoCustoProdutoEdicao) {
		this.precoCustoProdutoEdicao = precoCustoProdutoEdicao;
	}

	public BigDecimal getPrecoVendaProdutoEdicao() {
		return precoVendaProdutoEdicao;
	}

	public void setPrecoVendaProdutoEdicao(BigDecimal precoVendaProdutoEdicao) {
		this.precoVendaProdutoEdicao = precoVendaProdutoEdicao;
	}

	public BigDecimal getValorDescontoMEC() {
		return valorDescontoMEC;
	}

	public void setValorDescontoMEC(BigDecimal valorDescontoMEC) {
		this.valorDescontoMEC = valorDescontoMEC;
	}

	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}

	public String getCodigoDeBarrasProdutoEdicao() {
		return codigoDeBarrasProdutoEdicao;
	}

	public void setCodigoDeBarrasProdutoEdicao(String codigoDeBarrasProdutoEdicao) {
		this.codigoDeBarrasProdutoEdicao = codigoDeBarrasProdutoEdicao;
	}
}
