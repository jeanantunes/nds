package br.com.abril.nds.dto;

import java.io.Serializable;

/*
 *  @author Samuel Mendes
 *  @email mildf.dev@gmail.com
 *  
 *  DTO base para as futuras consultas por COTA
 *  
 *  verificar se existe algum classe/enum que informa esses campos com o Lázaro:
 *  
 *  statusCota
 *  areaInfluencia 
 *  geradorPrimario
 *  geradorSecundario
 *  faturamento é monetário?
 *  
 */

public class ConsultaCotaDTO implements Serializable {

	private static final long serialVersionUID = -3481326311215518825L;

	private Integer numeroCota;
	private String statusCota;
	private String nome;
	private String tipoPdv;
	private String bairro;
	private String cidade;
	private Double faturamento;
	private String areaInfluencia;
	private String geradorPrimario;
	private String geradorSecundario;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(String statusCota) {
		this.statusCota = statusCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoPdv() {
		return tipoPdv;
	}

	public void setTipoPdv(String tipoPdv) {
		this.tipoPdv = tipoPdv;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Double getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(Double faturamento) {
		this.faturamento = faturamento;
	}

	public String getAreaInfluencia() {
		return areaInfluencia;
	}

	public void setAreaInfluencia(String areaInfluencia) {
		this.areaInfluencia = areaInfluencia;
	}

	public String getGeradorPrimario() {
		return geradorPrimario;
	}

	public void setGeradorPrimario(String geradorPrimario) {
		this.geradorPrimario = geradorPrimario;
	}

	public String getGeradorSecundario() {
		return geradorSecundario;
	}

	public void setGeradorSecundario(String geradorSecundario) {
		this.geradorSecundario = geradorSecundario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
