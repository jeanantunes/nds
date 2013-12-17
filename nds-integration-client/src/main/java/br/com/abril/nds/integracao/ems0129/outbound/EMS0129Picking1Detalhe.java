package br.com.abril.nds.integracao.ems0129.outbound;

import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking1Detalhe implements InterfaceDetalhesPicking {
	
	private String tipoRegistro;
	
	private String codigoCota;
	
	private String sequenciaNotaEnvio;
	
	private String codigoProduto;
	
	private String edicao;
	
	private String nomePublicacao;
	
	private BigDecimal precoCusto;
	
	private BigDecimal precoVenda;
	
	private BigDecimal desconto;
	
	protected Long quantidade;
	
	
	@Field(offset = 1, length = 1, paddingChar = '2')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 5, align = Align.RIGHT)
	public String getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 3, align = Align.RIGHT)
	public String getSequenciaNotaEnvio() {
		return sequenciaNotaEnvio;
	}
	
	@Field(offset = 10, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	@Field(offset = 18, length = 4, align = Align.RIGHT)
	public String getEdicao() {
		return edicao;
	}
	
	@Field(offset = 22, length = 21)
	public String getNomePublicacao() {
		return nomePublicacao;
	}
	
	@Field(offset = 43, length = 10, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 4, useDecimalDelimiter = true)
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	
	@Field(offset = 53, length = 10, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 4, useDecimalDelimiter = true)
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	@Field(offset = 63, length = 8, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 4, useDecimalDelimiter = true)
	public BigDecimal getDesconto() {
		return desconto;
	}
	
	@Field(offset = 71, length = 6, align = Align.RIGHT)
	public Long getQuantidade() {
		return quantidade;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public void setSequenciaNotaEnvio(String sequenciaNotaEnvio) {
		this.sequenciaNotaEnvio = sequenciaNotaEnvio;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public void setEdicao(String edicao) {
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
	
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}	
}
