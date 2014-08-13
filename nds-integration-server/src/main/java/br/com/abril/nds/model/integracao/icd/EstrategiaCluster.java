package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Transient;

import br.com.abril.nds.model.integracao.icd.pks.EstrategiaClusterPK;


//@Entity
//@Table(name = "ESTRATEGIA_CLUSTER")
public class EstrategiaCluster {
	 
	@EmbeddedId
	private EstrategiaClusterPK estrategiaClusterPK;
	
	@Transient
	private String tipoDocumento;
	
	@Transient
	private String baseDeDados;
	
	@Transient
	private String usuarioBaseDeDados;
	
	@Transient
	private Long codigoDistribuidor;

	
	@Column(name = "COD_TIPO_CLUSTER")
	private Long codTipoCluster;
	
	
	public EstrategiaClusterPK getEstrategiaClusterPK() {
		return estrategiaClusterPK;
	}

	public void setEstrategiaClusterPK(EstrategiaClusterPK estrategiaClusterPK) {
		this.estrategiaClusterPK = estrategiaClusterPK;
	}

	public Long getCodTipoCluster() {
		return codTipoCluster;
	}

	public void setCodTipoCluster(Long codTipoCluster) {
		this.codTipoCluster = codTipoCluster;
	}

	/**
	 * Getters e Setters 
	 */
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getBaseDeDados() {
		return baseDeDados;
	}

	public void setBaseDeDados(String baseDeDados) {
		this.baseDeDados = baseDeDados;
	}

	public String getUsuarioBaseDeDados() {
		return usuarioBaseDeDados;
	}

	public void setUsuarioBaseDeDados(String usuarioBaseDeDados) {
		this.usuarioBaseDeDados = usuarioBaseDeDados;
	}

	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}	
}
