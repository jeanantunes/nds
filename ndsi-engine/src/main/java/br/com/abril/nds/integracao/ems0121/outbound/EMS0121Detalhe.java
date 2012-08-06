package br.com.abril.nds.integracao.ems0121.outbound;

import java.math.BigInteger;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0121Detalhe {
	
	private String tipoRegistro;
	private Integer contextoProduto;
	private Integer codigoFornecedorProduto;
	private String codPublicacao;
	private Long edicao;
	private Integer codigoCota;
	private BigInteger quantidadeEncalhe;
	private Date dataRecolhimento;


	@Field(offset=1, length=2, paddingChar='2')
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	@Field(offset=2, length=1)
	public Integer getContextoProduto() {
		return contextoProduto;
	}

	public void setContextoProduto(Integer contextoProduto) {
		this.contextoProduto = contextoProduto;
	}
	
	@Field(offset=3, length=7)
	public Integer getCodigoFornecedorProduto() {
		return codigoFornecedorProduto;
	}

	public void setCodigoFornecedorProduto(Integer codigoFornecedorProduto) {
		this.codigoFornecedorProduto = codigoFornecedorProduto;
	}

	@Field(offset=10, length=8)
	public String getCodPublicacao() {
		return codPublicacao;
	}

	public void setCodPublicacao(String codPublicacao) {
		this.codPublicacao = codPublicacao;
	}

	@Field(offset=18, length=4)
	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset=22, length=4)
	public Integer getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	@Field(offset=26, length=8)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigInteger getQuantidadeEncalhe() {
		return quantidadeEncalhe;
	}

	public void setQuantidadeEncalhe(BigInteger quantidadeEncalhe) {
		this.quantidadeEncalhe = quantidadeEncalhe;
	}

	@Field(offset=34, length=8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	
	
}