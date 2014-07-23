package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;

public class CaracteristicaDTO implements Serializable {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private boolean pontoPrincipal;
	
	private boolean balcaoCentral;
	
	private boolean temComputador;
	
	private boolean luminoso;
	
	private boolean possuiCartao;
	
	private String textoLuminoso;
	
	private Long tipoPonto;
	
	private Long caracteristica;
	
	private Long areaInfluencia;
	
	private String descricaoAreaInfluencia;
		
	private TipoCaracteristicaSegmentacaoPDV tipoCaracteristicaSegmentacaoPDV;
	
	private String descricaoTipoCaracteristica;
	

	/**
	 * @return the tipoCaracteristicaSegmentacaoPDV
	 */
	public TipoCaracteristicaSegmentacaoPDV getTipoCaracteristicaSegmentacaoPDV() {
		return tipoCaracteristicaSegmentacaoPDV;
	}

	/**
	 * @param tipoCaracteristicaSegmentacaoPDV the tipoCaracteristicaSegmentacaoPDV to set
	 */
	public void setTipoCaracteristicaSegmentacaoPDV(
			TipoCaracteristicaSegmentacaoPDV tipoCaracteristicaSegmentacaoPDV) {
		this.tipoCaracteristicaSegmentacaoPDV = tipoCaracteristicaSegmentacaoPDV;
		if (this.tipoCaracteristicaSegmentacaoPDV != null) {
		    this.descricaoTipoCaracteristica = this.tipoCaracteristicaSegmentacaoPDV.getDescricao();
		}
	}
	

	/**
     * @return the descricaoTipoCaracteristica
     */
    public String getDescricaoTipoCaracteristica() {
        return descricaoTipoCaracteristica;
    }

    /**
     * @param descricaoTipoCaracteristica the descricaoTipoCaracteristica to set
     */
    public void setDescricaoTipoCaracteristica(String descricaoTipoCaracteristica) {
        this.descricaoTipoCaracteristica = descricaoTipoCaracteristica;
    }

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
     * @return the descricaoAreaInfluencia
     */
    public String getDescricaoAreaInfluencia() {
        return descricaoAreaInfluencia;
    }

    /**
     * @param descricaoAreaInfluencia the descricaoAreaInfluencia to set
     */
    public void setDescricaoAreaInfluencia(String descricaoAreaInfluencia) {
        this.descricaoAreaInfluencia = descricaoAreaInfluencia;
    }

    public boolean isPossuiCartao() {
		return possuiCartao;
	}

	public void setPossuiCartao(boolean possuiCartao) {
		this.possuiCartao = possuiCartao;
	}

}
