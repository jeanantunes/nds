package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExportarCobrancaDTO implements Serializable{

	private static final long serialVersionUID = 4032400439645311234L;
	
	private Integer cod_jornaleiro;
	private String data_operacao;
	private BigDecimal vlr_cobranca;
	private BigDecimal vlr_encalhe;
	private BigDecimal vlr_nro_atr;
	private BigDecimal vlr_credito;
	private BigDecimal vlr_juros;
	private BigDecimal vlr_multa;
	private BigDecimal vlr_outros;
	private BigDecimal vlr_pendencia;
	private BigDecimal vlr_pagto_diverg;
	private BigDecimal vlr_postergado;
	private BigDecimal vlr_total;
	private BigDecimal vlr_taxa_extra;
	private String cod_rota;
	private String rota ;
	private String cod_roteiro ;
	private String roteiro ;
	private String ftvenc;
	private String box_dp ;
	
	private String nossoNumero = "0";
	private Boolean cotaProcessada = false;
	
	
	public Integer getCod_jornaleiro() {
		return cod_jornaleiro;
	}
	public void setCod_jornaleiro(Integer cod_jornaleiro) {
		this.cod_jornaleiro = cod_jornaleiro;
	}
	public String getData_operacao() {
		return data_operacao;
	}
	public void setData_operacao(String data_operacao) {
		this.data_operacao = data_operacao;
	}
	public BigDecimal getVlr_cobranca() {
		return vlr_cobranca;
	}
	public void setVlr_cobranca(BigDecimal vlr_cobranca) {
		this.vlr_cobranca = vlr_cobranca;
	}
	public BigDecimal getVlr_encalhe() {
		return vlr_encalhe;
	}
	public void setVlr_encalhe(BigDecimal vlr_encalhe) {
		this.vlr_encalhe = vlr_encalhe;
	}
	public BigDecimal getVlr_nro_atr() {
		return vlr_nro_atr;
	}
	public void setVlr_nro_atr(BigDecimal vlr_nro_atr) {
		this.vlr_nro_atr = vlr_nro_atr;
	}
	public BigDecimal getVlr_credito() {
		return vlr_credito;
	}
	public void setVlr_credito(BigDecimal vlr_credito) {
		this.vlr_credito = vlr_credito;
	}
	public BigDecimal getVlr_juros() {
		return vlr_juros;
	}
	public void setVlr_juros(BigDecimal vlr_juros) {
		this.vlr_juros = vlr_juros;
	}
	public BigDecimal getVlr_multa() {
		return vlr_multa;
	}
	public void setVlr_multa(BigDecimal vlr_multa) {
		this.vlr_multa = vlr_multa;
	}
	public BigDecimal getVlr_outros() {
		return vlr_outros;
	}
	public void setVlr_outros(BigDecimal vlr_outros) {
		this.vlr_outros = vlr_outros;
	}
	public BigDecimal getVlr_pendencia() {
		return vlr_pendencia;
	}
	public void setVlr_pendencia(BigDecimal vlr_pendencia) {
		this.vlr_pendencia = vlr_pendencia;
	}
	public BigDecimal getVlr_pagto_diverg() {
		return vlr_pagto_diverg;
	}
	public void setVlr_pagto_diverg(BigDecimal vlr_pagto_diverg) {
		this.vlr_pagto_diverg = vlr_pagto_diverg;
	}
	public BigDecimal getVlr_postergado() {
		return vlr_postergado;
	}
	public void setVlr_postergado(BigDecimal vlr_postergado) {
		this.vlr_postergado = vlr_postergado;
	}
	public BigDecimal getVlr_total() {
		return vlr_total;
	}
	public void setVlr_total(BigDecimal vlr_total) {
		this.vlr_total = vlr_total;
	}
	public BigDecimal getVlr_taxa_extra() {
		return vlr_taxa_extra;
	}
	public void setVlr_taxa_extra(BigDecimal vlr_taxa_extra) {
		this.vlr_taxa_extra = vlr_taxa_extra;
	}
	public String getRota() {
		return rota;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public String getRoteiro() {
		return roteiro;
	}
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	public String getCod_rota() {
		return cod_rota;
	}
	public void setCod_rota(String cod_rota) {
		this.cod_rota = cod_rota;
	}
	public String getCod_roteiro() {
		return cod_roteiro;
	}
	public void setCod_roteiro(String cod_roteiro) {
		this.cod_roteiro = cod_roteiro;
	}
	public String getFtvenc() {
		return ftvenc;
	}
	public void setFtvenc(String ftvenc) {
		this.ftvenc = ftvenc;
	}
	public String getBox_dp() {
		return box_dp;
	}
	public void setBox_dp(String box_dp) {
		this.box_dp = box_dp;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public Boolean getCotaProcessada() {
		return cotaProcessada;
	}
	public void setCotaProcessada(Boolean cotaProcessada) {
		this.cotaProcessada = cotaProcessada;
	}
	
}
