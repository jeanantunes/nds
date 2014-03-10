package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0140InputItem extends IntegracaoDocumentDetail implements Serializable {

	private static final long serialVersionUID = -7153363922617002707L;
	
	private Date dataLancamento;
	
	private Integer pacotePadrao;
	
	private Integer qtdExemplar;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private Double preco;
	
	private Double desconto;
	
	private String codigoBarras;
	
	private Integer peb;
	
	private String periodicidade;
	
	private Double peso;
	
	private String tipoProduto;

	@Field(offset = 27, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	@Field(offset = 35, length = 5)
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	@Field(offset = 40, length = 8)
	public Integer getQtdExemplar() {
		return qtdExemplar;
	}

	public void setQtdExemplar(Integer qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}

	@Field(offset = 56, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	@Field(offset = 64, length = 16)
	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	@Field(offset = 80, length = 4)
	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	@Field(offset = 84, length = 14)
	@FixedFormatDecimal(decimals=2, useDecimalDelimiter=true, decimalDelimiter=',')
	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Field(offset = 98, length = 7)
	@FixedFormatDecimal(decimals=2, useDecimalDelimiter=true, decimalDelimiter=',')
	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	@Field(offset = 105, length = 18)
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	@Field(offset = 187, length = 3)
	public Integer getPeb() {
		return peb;
	}

	public void setPeb(Integer peb) {
		this.peb = peb;
	}
	@Field(offset = 190, length = 15)
	public String getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	@Field(offset = 205, length = 6)
	@FixedFormatDecimal(decimals=3)
	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	@Field(offset = 211, length = 20)
	public String getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

}
