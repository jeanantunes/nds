package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

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

	private Long numeroCota;

	private String nomeCota;

	private BigDecimal quantidadePdvs;

	// Baseado no Pdv principal
	private String municipio;

	private BigDecimal vendaExemplares;

	private BigDecimal faturamentoCapa;

	private Double participacao;

	private Double participacaoAcumulada;

	public RegistroCurvaABCDistribuidorVO() {
	}

	public RegistroCurvaABCDistribuidorVO(Long numeroCota, String nomeCota,
			BigDecimal quantidadePdvs, String municipio, BigDecimal vendaExemplares,
			BigDecimal faturamentoCapa) {
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.quantidadePdvs = quantidadePdvs;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;
	}

	public RegistroCurvaABCDistribuidorVO(Object numeroCota, Object nomeCota,
			Object quantidadePdvs, Object vendaExemplares,
			Object faturamentoCapa) {
		System.out.println(numeroCota);
		
		try {
			
			this.numeroCota = (Long) numeroCota;
			this.nomeCota = (String) nomeCota;
			this.quantidadePdvs = (BigDecimal) quantidadePdvs;
			this.vendaExemplares = (BigDecimal) vendaExemplares;
			this.faturamentoCapa = (BigDecimal) faturamentoCapa;
			
		} catch(Exception e) {
		}
		
		/*this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.quantidadePdvs = quantidadePdvs;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;*/
	}
	
	public Long getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Long numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public BigDecimal getQuantidadePdvs() {
		return quantidadePdvs;
	}

	public void setQuantidadePdvs(BigDecimal quantidadePdvs) {
		this.quantidadePdvs = quantidadePdvs;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
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

	public BigDecimal getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigDecimal vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
	}
	
}
