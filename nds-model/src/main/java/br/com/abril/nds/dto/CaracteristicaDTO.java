package br.com.abril.nds.dto;

import java.io.Serializable;

public class CaracteristicaDTO implements Serializable {
	
	private boolean pontoPrincipal;
	
	private boolean balcaoCentral;
	
	private boolean temComputador;
	
	private boolean luminoso;
	
	private String textoLuminoso;
	
	private Long tipoPonto;
	
	private Long caracteristica;
	
	private Long areaInfluencia;
	
	private Long cluster;

	/**
	 * @return the pontoPrincipal
	 */
	public boolean isPontoPrincipal() {
		return pontoPrincipal;
	}

	/**
	 * @param pontoPrincipal the pontoPrincipal to set
	 */
	public void setPontoPrincipal(boolean pontoPrincipal) {
		this.pontoPrincipal = pontoPrincipal;
	}

	/**
	 * @return the balcaoCentral
	 */
	public boolean isBalcaoCentral() {
		return balcaoCentral;
	}

	/**
	 * @param balcaoCentral the balcaoCentral to set
	 */
	public void setBalcaoCentral(boolean balcaoCentral) {
		this.balcaoCentral = balcaoCentral;
	}

	/**
	 * @return the temComputador
	 */
	public boolean isTemComputador() {
		return temComputador;
	}

	/**
	 * @param temComputador the temComputador to set
	 */
	public void setTemComputador(boolean temComputador) {
		this.temComputador = temComputador;
	}

	/**
	 * @return the luminoso
	 */
	public boolean isLuminoso() {
		return luminoso;
	}

	/**
	 * @param luminoso the luminoso to set
	 */
	public void setLuminoso(boolean luminoso) {
		this.luminoso = luminoso;
	}

	/**
	 * @return the textoLuminoso
	 */
	public String getTextoLuminoso() {
		return textoLuminoso;
	}

	/**
	 * @param textoLuminoso the textoLuminoso to set
	 */
	public void setTextoLuminoso(String textoLuminoso) {
		this.textoLuminoso = textoLuminoso;
	}

	/**
	 * @return the tipoPonto
	 */
	public Long getTipoPonto() {
		return tipoPonto;
	}

	/**
	 * @param tipoPonto the tipoPonto to set
	 */
	public void setTipoPonto(Long tipoPonto) {
		this.tipoPonto = tipoPonto;
	}

	/**
	 * @return the caracteristica
	 */
	public Long getCaracteristica() {
		return caracteristica;
	}

	/**
	 * @param caracteristica the caracteristica to set
	 */
	public void setCaracteristica(Long caracteristica) {
		this.caracteristica = caracteristica;
	}

	/**
	 * @return the areaInfluencia
	 */
	public Long getAreaInfluencia() {
		return areaInfluencia;
	}

	/**
	 * @param areaInfluencia the areaInfluencia to set
	 */
	public void setAreaInfluencia(Long areaInfluencia) {
		this.areaInfluencia = areaInfluencia;
	}

	/**
	 * @return the cluster
	 */
	public Long getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Long cluster) {
		this.cluster = cluster;
	}

}
