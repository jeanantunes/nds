package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ParcialVendaDTO implements Serializable {

	private static final long serialVersionUID = 952662045509974535L;

	@Export(label="Cota")
	private Integer numeroCota;
	
	@Export(label="Nome")
	private String nomeCota;
	
	@Export(label="Reparte")
	private BigInteger reparte;
	
	@Export(label="Encalhe")
	private BigInteger encalhe;
	
	@Export(label="Venda Juramentada")
	private BigInteger vendaJuramentada;

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}

	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	/**
	 * @return the encalhe
	 */
	public BigInteger getEncalhe() {
		return encalhe;
	}

	/**
	 * @param encalhe the encalhe to set
	 */
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}

	/**
	 * @return the vendaJuramentada
	 */
	public BigInteger getVendaJuramentada() {
		return vendaJuramentada;
	}

	/**
	 * @param vendaJuramentada the vendaJuramentada to set
	 */
	public void setVendaJuramentada(BigInteger vendaJuramentada) {
		this.vendaJuramentada = vendaJuramentada;
	}	
}
