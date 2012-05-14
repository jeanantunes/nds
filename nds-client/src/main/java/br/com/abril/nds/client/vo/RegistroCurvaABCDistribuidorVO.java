package br.com.abril.nds.client.vo;

import java.io.Serializable;

/**
 * 
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC do distribuidor.
 * 
 * @author InfoA2
 * 
 */
public class RegistroCurvaABCDistribuidorVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3849170873913801404L;

	private Integer numeroCota;

	private String nomeCota;

	private Integer quantidadePdvs;

	// Baseado no Pdv principal
	private String municipio;

	private Long vendaExemplares;

	private Double faturamentoCapa;

	private Double participacao;

	private Double participacaoAcumulada;

	public RegistroCurvaABCDistribuidorVO() {
	}

	public RegistroCurvaABCDistribuidorVO(Integer numeroCota, String nomeCota,
			Integer quantidadePdvs, String municipio, Long vendaExemplares,
			Double faturamentoCapa, Double participacao,
			Double participacaoAcumulada) {
		super();
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.quantidadePdvs = quantidadePdvs;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;
		this.participacao = participacao;
		this.participacaoAcumulada = participacaoAcumulada;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Integer getQuantidadePdvs() {
		return quantidadePdvs;
	}

	public void setQuantidadePdvs(Integer quantidadePdvs) {
		this.quantidadePdvs = quantidadePdvs;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public Long getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(Long vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public Double getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(Double faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
	}

	public Double getParticipacao() {
		return participacao;
	}

	public void setParticipacao(Double participacao) {
		this.participacao = participacao;
	}

	public Double getParticipacaoAcumulada() {
		return participacaoAcumulada;
	}

	public void setParticipacaoAcumulada(Double participacaoAcumulada) {
		this.participacaoAcumulada = participacaoAcumulada;
	}

}
