package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import br.com.abril.nds.model.integracao.icd.pks.EstrategiaLanctoPracaPK;


//@Entity
//@Table(name = "ESTRATEGIA_LANCTO_PRACA")
public class EstrategiaLanctoPraca {
	
	@EmbeddedId
	private EstrategiaLanctoPracaPK estrategiaLanctoPracaPK;
	
	@Transient
	private String tipoDocumento;
	
	@Transient
	private String baseDeDados;
	
	@Transient
	private String usuarioBaseDeDados;
	
	@Transient
	private Long codigoDistribuidor;
	
	@Column(name = "COD_PRACA")
	private Long codPraca;
	
	@OneToOne(mappedBy="estrategiaMicroDistbcaoPK.codEstrategia")
	EstrategiaMicroDistribuicao estrategiaMicroDistbcao;
	
	@OneToOne(mappedBy="estrategiaClusterPK.codEstrategia")
	EstrategiaCluster estrategiaCluster;
	
	public EstrategiaLanctoPracaPK getEstrategiaLanctoPracaPK() {
		return estrategiaLanctoPracaPK;
	}

	public void setEstrategiaLanctoPracaPK(
			EstrategiaLanctoPracaPK estrategiaLanctoPracaPK) {
		this.estrategiaLanctoPracaPK = estrategiaLanctoPracaPK;
	}

	public EstrategiaMicroDistribuicao getEstrategiaMicroDistbcao() {
		return estrategiaMicroDistbcao;
	}

	public void setEstrategiaMicroDistbcao(
			EstrategiaMicroDistribuicao estrategiaMicroDistbcao) {
		this.estrategiaMicroDistbcao = estrategiaMicroDistbcao;
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

	public Long getCodPraca() {
		return codPraca;
	}

	public void setCodPraca(Long codPraca) {
		this.codPraca = codPraca;
	}
}
