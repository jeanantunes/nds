package br.com.abril.nds.integracao.ems0129.outbound;

import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking2Detalhe {

	private String tipoRegistro;
	
	private Integer codigoCota;
	
	private Integer sequenciaNotaEnvio;
	
	private String codigoProduto;
	
	private Long edicao;
	
	private String nomePublicacao;
	
	private BigDecimal precoCusto;
	
	private BigDecimal precoVenda;
	
	private BigDecimal desconto;
	
	protected Long quantidade;
	
	private String separador1;
	
	private String separador2;
	
	private String separador3;
	
	private String separador4;
	
	private String separador5;
	
	private String separador6;
	
	private String separador7;
	
	private String separador8;
	
	private String separador9;
	
	private String separador10;
	
	
	@Field(offset = 1, length = 1, paddingChar = '2')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 1, paddingChar = ';')
	public String getSeparador1() {
		return separador1;
	}
	
	@Field(offset = 3, length = 4, align = Align.RIGHT)
	public Integer getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 1, paddingChar = ';')
	public String getSeparador2() {
		return separador2;
	}
	
	@Field(offset = 8, length = 3, align = Align.RIGHT)
	public Integer getSequenciaNotaEnvio() {
		return sequenciaNotaEnvio;
	}
	
	@Field(offset = 11, length = 1, paddingChar = ';')
	public String getSeparador3() {
		return separador3;
	}
	
	@Field(offset = 12, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	@Field(offset = 20, length = 1, paddingChar = ';')
	public String getSeparador4() {
		return separador4;
	}
	
	@Field(offset = 21, length = 4, align = Align.RIGHT)
	public Long getEdicao() {
		return edicao;
	}
	
	@Field(offset = 25, length = 1, paddingChar = ';')
	public String getSeparador5() {
		return separador5;
	}
	
	@Field(offset = 26, length = 20)
	public String getNomePublicacao() {
		return nomePublicacao;
	}
	
	@Field(offset = 46, length = 1, paddingChar = ';')
	public String getSeparador6() {
		return separador6;
	}
	
	@Field(offset = 47, length = 10, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	
	@Field(offset = 57, length = 1, paddingChar = ';')
	public String getSeparador7() {
		return separador7;
	}
	
	@Field(offset = 58, length = 10, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	@Field(offset = 68, length = 1, paddingChar = ';')
	public String getSeparador8() {
		return separador8;
	}
	
	@Field(offset = 69, length = 3, align = Align.RIGHT)
	public BigDecimal getDesconto() {
		return desconto;
	}
	
	@Field(offset = 72, length = 1, paddingChar = ';')
	public String getSeparador9() {
		return separador9;
	}
	
	@Field(offset = 73, length = 6, align = Align.RIGHT)
	public Long getQuantidade() {
		return quantidade;
	}

	@Field(offset = 79, length = 1, paddingChar = ';')
	public String getSeparador10() {
		return separador10;
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

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}	
}
