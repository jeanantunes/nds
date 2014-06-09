package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: Informações do Produto.
 * 
 */

public class InformacoesCaracteristicasProdDTO implements Serializable {

	private static final long serialVersionUID = 4670424985410752892L;
	
	private BigDecimal precoVenda;
	private int pacotePadrao;
	private String chamadaCapa;
	private String nomeComercial;
	private String boletimInformativo;

	
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	public String getChamadaCapa() {
		return chamadaCapa;
	}
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}
	public String getNomeComercial() {
		return nomeComercial;
	}
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	public String getBoletimInformativo() {
		return boletimInformativo;
	}
	public void setBoletimInformativo(String boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
	}
}