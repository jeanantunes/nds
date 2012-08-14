package br.com.abril.nds.integracao.ems0129.outbound;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking1Detalhe {
	
	private String tipoRegistro;
	
	private Integer codigoCota;
	
	private Integer sequenciaNotaEnvio;
	
	private String codigoProduto;
	
	private Long edicao;
	
	private String nomePublicacao;
	
	private BigDecimal precoCusto;
	
	private BigDecimal precoVenda;
	
	private BigDecimal desconto;
	
	protected BigInteger quantidade;
	
	
	@Field(offset = 1, length = 1, paddingChar = '2')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 5, align = Align.RIGHT)
	public Integer getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 3, align = Align.RIGHT)
	public Integer getSequenciaNotaEnvio() {
		return sequenciaNotaEnvio;
	}
	
	@Field(offset = 10, length = 5)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	@Field(offset = 15, length = 4, align = Align.RIGHT)
	public Long getEdicao() {
		return edicao;
	}
	
	@Field(offset = 19, length = 21)
	public String getNomePublicacao() {
		return nomePublicacao;
	}
	
	@Field(offset = 40, length = 10, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	
	@Field(offset = 50, length = 10, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	@Field(offset = 60, length = 3, align = Align.RIGHT)
	public BigDecimal getDesconto() {
		return desconto;
	}
	
	@Field(offset = 63, length = 6, align = Align.RIGHT)
	public BigInteger getQuantidade() {
		return quantidade;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public void setSequenciaNotaEnvio(Integer sequenciaNotaEnvio) {
		this.sequenciaNotaEnvio = sequenciaNotaEnvio;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	public void setNomePublicacao(String nomePublicacao) {
		this.nomePublicacao = nomePublicacao;
	}
	
	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}
	
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}	
}
