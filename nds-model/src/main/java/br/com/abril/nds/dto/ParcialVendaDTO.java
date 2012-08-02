package br.com.abril.nds.dto;

import java.io.Serializable;

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
	private Long reparte;
	
	@Export(label="Encalhe")
	private Long encalhe;
	
	@Export(label="Venda Juramentada")
	private Long vendaJuramentada;

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
	public Long getReparte() {
		return reparte;
	}

	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(Long reparte) {
		this.reparte = reparte;
	}

	/**
	 * @return the encalhe
	 */
	public Long getEncalhe() {
		return encalhe;
	}

	/**
	 * @param encalhe the encalhe to set
	 */
	public void setEncalhe(Long encalhe) {
		this.encalhe = encalhe;
	}

	/**
	 * @return the vendaJuramentada
	 */
	public Long getVendaJuramentada() {
		return vendaJuramentada;
	}

	/**
	 * @param vendaJuramentada the vendaJuramentada to set
	 */
	public void setVendaJuramentada(Long vendaJuramentada) {
		this.vendaJuramentada = vendaJuramentada;
	}	
}
