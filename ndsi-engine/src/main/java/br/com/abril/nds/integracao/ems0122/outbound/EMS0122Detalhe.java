package br.com.abril.nds.integracao.ems0122.outbound;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0122Detalhe {
	
	private String tipoRegistro;
	private Integer contextoProduto;
	private Integer codigoFornecedorProduto;
	private String codPublicacao;
	private Long edicao;
	private Integer codigoCota;
	private Long quantidadeSuplementar;
	private BigDecimal precoCapa;


	@Field(offset=1, length=1, paddingChar='2')
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

	@Field(offset=18, length=4, paddingChar='0')
	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset=22, length=4, paddingChar='0')
	public Integer getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	@Field(offset=26, length=8, align=Align.RIGHT, paddingChar='0')
	public Long getQuantidadeSuplementar() {
		return quantidadeSuplementar;
	}

	public void setQuantidadeSuplementar(Long quantidadeSuplementar) {
		this.quantidadeSuplementar = quantidadeSuplementar;
	}

	@Field(offset=34, length=9, align=Align.RIGHT, paddingChar='0')
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	

	
	
}