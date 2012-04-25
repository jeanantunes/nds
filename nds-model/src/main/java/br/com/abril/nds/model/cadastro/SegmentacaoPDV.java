package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entidade com informações de segmentação do PDV
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class SegmentacaoPDV {
	
	/**
	 * Tipo do ponto PDV
	 */
	@ManyToOne 
	@JoinColumn(name = "TIPO_PONTO_PDV_ID")
	private TipoPontoPDV tipoPontoPDV;
	
	/**
	 * Tipo característica segmentação do PDV 
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_CARACTERISTICA_PDV")
	private TipoCaracteristicaSegmentacaoPDV tipoCaracteristica;
	
	/**
	 * Área de influência do PDV
	 */
	@ManyToOne 
	@JoinColumn(name = "AREA_INFLUENCIA_PDV_ID")
	private AreaInfluenciaPDV areaInfluenciaPDV;
	
	/**
	 * Cluster PDV
	 */
	@ManyToOne 
	@JoinColumn(name = "CLUSTER_PDV_ID")
	private ClusterPDV clusterPDV;
	
	public TipoPontoPDV getTipoPontoPDV() {
		return tipoPontoPDV;
	}

	public void setTipoPontoPDV(TipoPontoPDV tipoPontoPDV) {
		this.tipoPontoPDV = tipoPontoPDV;
	}
	
	public TipoCaracteristicaSegmentacaoPDV getTipoCaracteristica() {
		return tipoCaracteristica;
	}
	
	public void setTipoCaracteristica(
			TipoCaracteristicaSegmentacaoPDV tipoCaracteristica) {
		this.tipoCaracteristica = tipoCaracteristica;
	}
	
	public AreaInfluenciaPDV getAreaInfluenciaPDV() {
		return areaInfluenciaPDV;
	}
	
	public void setAreaInfluenciaPDV(AreaInfluenciaPDV areaInfluenciaPDV) {
		this.areaInfluenciaPDV = areaInfluenciaPDV;
	}

	
	public ClusterPDV getClusterPDV() {
		return clusterPDV;
	}

	public void setClusterPDV(ClusterPDV clusterPDV) {
		this.clusterPDV = clusterPDV;
	}

}
