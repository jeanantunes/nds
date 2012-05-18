package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;

/**
 * 
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC do distribuidor.
 * 
 * @author InfoA2
 * 
 */
public class RegistroCurvaABCDistribuidorVO extends RegistroCurvaABC implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3849170873913801404L;

	@Export(label = "Número")
	private Integer numeroCota;

	@Export(label = "Cota")
	private String nomeCota;

	@Export(label = "Quantidade de Pdvs")
	private Integer quantidadePdvs;

	@Export(label = "Municipio")
	private String municipio;

	@Export(label = "Venda de Exemplares")
	private BigDecimal vendaExemplares;

	@Export(label = "Faturamento da Capa")
	private BigDecimal faturamentoCapa;

	public RegistroCurvaABCDistribuidorVO() {
	}

	public RegistroCurvaABCDistribuidorVO(Integer numeroCota, String nomeCota,
			Integer quantidadePdvs, String municipio, BigDecimal vendaExemplares, BigDecimal faturamentoCapa) {
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.quantidadePdvs = quantidadePdvs;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;
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
