package br.com.abril.nds.export.cnab.cobranca;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class Trailer {

	// Controle
	private Long codigoBanco;
	private Long lote;
	private Long tipoRegistro = 5L;
	
	private String cnab;
	private Long quantidadeRegistros;
	
	// Totalização da Cobrança Simples
	private Long qtdTitulosCobrancaSimples;
	private Long valorTitulosCobrancaSimples;
	
	// Totalização da Cobrança Vinculada
	private Long qtdTitulosCobrancaVinculada;
	private Long valorTitulosCobrancaVinculada;
	
	// Totalização da Cobrança Caucionada
	private Long qtdTitulosCobrancaCaucionada;
	private Long valorTitulosCobrancaCaucionada;
	
	// Totalização da Cobrança Descontada
	private Long qtdTitulosCobrancaDescontada;
	private Long valorTitulosCobrancaDescontada;
	
	private String numeroAviso;
	private String cnab2;
	
	/**
	 * @return the codigoBanco
	 */
	@Field(offset=1, length=3, align = Align.RIGHT, paddingChar = '0')
	public Long getCodigoBanco() {
		return codigoBanco;
	}
	
	/**
	 * @param codigoBanco the codigoBanco to set
	 */
	public void setCodigoBanco(Long codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	
	/**
	 * @return the lote
	 */
	@Field(offset=4, length=4, align = Align.RIGHT, paddingChar = '0')
	public Long getLote() {
		return lote;
	}
	
	/**
	 * @param lote the lote to set
	 */
	public void setLote(Long lote) {
		this.lote = lote;
	}
	
	/**
	 * @return the tipoRegistro
	 */
	@Field(offset=8, length=1, align = Align.RIGHT, paddingChar = '0')
	public Long getTipoRegistro() {
		return tipoRegistro;
	}
	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(Long tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	/**
	 * @return the cnab
	 */
	@Field(offset=9, length=9, align=Align.LEFT, paddingChar=' ')
	public String getCnab() {
		return cnab;
	}

	/**
	 * @param cnab the cnab to set
	 */
	public void setCnab(String cnab) {
		this.cnab = cnab;
	}

	/**
	 * @return the quantidadeRegistros
	 */
	@Field(offset=18, length=6, align=Align.RIGHT, paddingChar='0')
	public Long getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	/**
	 * @param quantidadeRegistros the quantidadeRegistros to set
	 */
	public void setQuantidadeRegistros(Long quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	/**
	 * @return the qtdTitulosCobrancaSimples
	 */
	@Field(offset=24, length=6, align=Align.RIGHT, paddingChar='0')
	public Long getQtdTitulosCobrancaSimples() {
		return qtdTitulosCobrancaSimples;
	}

	/**
	 * @param qtdTitulosCobrancaSimples the qtdTitulosCobrancaSimples to set
	 */
	public void setQtdTitulosCobrancaSimples(Long qtdTitulosCobrancaSimples) {
		this.qtdTitulosCobrancaSimples = qtdTitulosCobrancaSimples;
	}

	/**
	 * @return the valorTitulosCobrancaSimples
	 */
	@Field(offset=30, length=15, align=Align.RIGHT, paddingChar='0')
	public Long getValorTitulosCobrancaSimples() {
		return valorTitulosCobrancaSimples;
	}

	/**
	 * @param valorTitulosCobrancaSimples the valorTitulosCobrancaSimples to set
	 */
	public void setValorTitulosCobrancaSimples(Long valorTitulosCobrancaSimples) {
		this.valorTitulosCobrancaSimples = valorTitulosCobrancaSimples;
	}

	/**
	 * @return the qtdTitulosCobrancaVinculada
	 */
	@Field(offset=47, length=6, align=Align.RIGHT, paddingChar='0')
	public Long getQtdTitulosCobrancaVinculada() {
		return qtdTitulosCobrancaVinculada;
	}

	/**
	 * @param qtdTitulosCobrancaVinculada the qtdTitulosCobrancaVinculada to set
	 */
	public void setQtdTitulosCobrancaVinculada(Long qtdTitulosCobrancaVinculada) {
		this.qtdTitulosCobrancaVinculada = qtdTitulosCobrancaVinculada;
	}

	/**
	 * @return the valorTitulosCobrancaVinculada
	 */
	@Field(offset=53, length=15, align=Align.RIGHT, paddingChar='0')
	public Long getValorTitulosCobrancaVinculada() {
		return valorTitulosCobrancaVinculada;
	}

	/**
	 * @param valorTitulosCobrancaVinculada the valorTitulosCobrancaVinculada to set
	 */
	public void setValorTitulosCobrancaVinculada(Long valorTitulosCobrancaVinculada) {
		this.valorTitulosCobrancaVinculada = valorTitulosCobrancaVinculada;
	}

	/**
	 * @return the qtdTitulosCobrancaCaucionada
	 */
	@Field(offset=70, length=6, align=Align.RIGHT, paddingChar='0')
	public Long getQtdTitulosCobrancaCaucionada() {
		return qtdTitulosCobrancaCaucionada;
	}

	/**
	 * @param qtdTitulosCobrancaCaucionada the qtdTitulosCobrancaCaucionada to set
	 */
	public void setQtdTitulosCobrancaCaucionada(Long qtdTitulosCobrancaCaucionada) {
		this.qtdTitulosCobrancaCaucionada = qtdTitulosCobrancaCaucionada;
	}

	/**
	 * @return the valorTitulosCobrancaCaucionada
	 */
	@Field(offset=76, length=15, align=Align.RIGHT, paddingChar='0')
	public Long getValorTitulosCobrancaCaucionada() {
		return valorTitulosCobrancaCaucionada;
	}

	/**
	 * @param valorTitulosCobrancaCaucionada the valorTitulosCobrancaCaucionada to set
	 */
	public void setValorTitulosCobrancaCaucionada(
			Long valorTitulosCobrancaCaucionada) {
		this.valorTitulosCobrancaCaucionada = valorTitulosCobrancaCaucionada;
	}

	/**
	 * @return the qtdTitulosCobrancaDescontada
	 */
	@Field(offset=93, length=6, align=Align.RIGHT, paddingChar='0')
	public Long getQtdTitulosCobrancaDescontada() {
		return qtdTitulosCobrancaDescontada;
	}

	/**
	 * @param qtdTitulosCobrancaDescontada the qtdTitulosCobrancaDescontada to set
	 */
	public void setQtdTitulosCobrancaDescontada(Long qtdTitulosCobrancaDescontada) {
		this.qtdTitulosCobrancaDescontada = qtdTitulosCobrancaDescontada;
	}

	/**
	 * @return the valorTitulosCobrancaDescontada
	 */
	@Field(offset=99, length=15, align=Align.RIGHT, paddingChar='0')
	public Long getValorTitulosCobrancaDescontada() {
		return valorTitulosCobrancaDescontada;
	}

	/**
	 * @param valorTitulosCobrancaDescontada the valorTitulosCobrancaDescontada to set
	 */
	public void setValorTitulosCobrancaDescontada(
			Long valorTitulosCobrancaDescontada) {
		this.valorTitulosCobrancaDescontada = valorTitulosCobrancaDescontada;
	}

	/**
	 * @return the numeroAviso
	 */
	@Field(offset=116, length=8, align=Align.LEFT, paddingChar='0')
	public String getNumeroAviso() {
		return numeroAviso;
	}

	/**
	 * @param numeroAviso the numeroAviso to set
	 */
	public void setNumeroAviso(String numeroAviso) {
		this.numeroAviso = numeroAviso;
	}

	/**
	 * @return the cnab2
	 */
	@Field(offset=124, length=117, align=Align.LEFT, paddingChar=' ')
	public String getCnab2() {
		return cnab2;
	}

	/**
	 * @param cnab2 the cnab2 to set
	 */
	public void setCnab2(String cnab2) {
		this.cnab2 = cnab2;
	}
	
}
