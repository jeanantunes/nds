package br.com.abril.nds.model.integracao.icd;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import br.com.abril.nds.model.integracao.icd.pks.ComposicaoBaseCalculoPK;


//@Entity
//@Table(name = "COMPOSICAO_BASE_CALCULO")
public class ComposicaoBaseCalculo {
	 
	@EmbeddedId
	private ComposicaoBaseCalculoPK composicaoBaseCalculoPK;
	
	@Transient
	private String tipoDocumento;
	
	@Transient
	private String baseDeDados;
	
	@Transient
	private String usuarioBaseDeDados;
	
	@Transient
	private Long codigoDistribuidor;
	
	@Column(name = "COD_ESTRATEGIA")
	private Long codEstrategia;
	
	@Column(name = "COD_BASE_CALCULO")
	private Long codBaseCalculo;
	
	@Column(name = "COD_LANCTO_EDICAO")
	private Long codLanctoEdicao;
	
	@Column(name = "PCT_BASE_CALCULO")
	private Long pctBaseCalculo;
	
	@Column(name = "NUM_SEQUENCIA")
	private Long numSequencia;
	
	@OneToOne(mappedBy="baseCalculoPK.codBaseCalculo")
	BaseCalculo baseCalculo;
	
	@OneToMany(mappedBy="estrategiaLanctoPracaPK.codEstrategia",fetch=FetchType.EAGER)
	List<EstrategiaLanctoPraca> estrategiaLanctoPraca;
	
	
	@OneToMany(mappedBy="estrategiaClusterPK.codEstrategia")
	List<EstrategiaCluster> estrategiaCluster;
	
	
	public BaseCalculo getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BaseCalculo baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public List<EstrategiaLanctoPraca> getEstrategiaLanctoPraca() {
		return estrategiaLanctoPraca;
	}

	public void setEstrategiaLanctoPraca(
			List<EstrategiaLanctoPraca> estrategiaLanctoPraca) {
		this.estrategiaLanctoPraca = estrategiaLanctoPraca;
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

	public Long getCodBaseCalculo() {
		return codBaseCalculo;
	}

	public void setCodBaseCalculo(Long codBaseCalculo) {
		this.codBaseCalculo = codBaseCalculo;
	}

	public ComposicaoBaseCalculoPK getComposicaobaseCalculoPK() {
		return composicaoBaseCalculoPK;
	}

	public void setComposicaobaseCalculoPK(
			ComposicaoBaseCalculoPK composicaoBaseCalculoPK) {
		this.composicaoBaseCalculoPK = composicaoBaseCalculoPK;
	}

	public Long getCodEstrategia() {
		return codEstrategia;
	}

	public void setCodEstrategia(Long codEstrategia) {
		this.codEstrategia = codEstrategia;
	}

	public Long getCodLanctoEdicao() {
		return codLanctoEdicao;
	}

	public void setCodLanctoEdicao(Long codLanctoEdicao) {
		this.codLanctoEdicao = codLanctoEdicao;
	}

	public Long getPctBaseCalculo() {
		return pctBaseCalculo;
	}

	public void setPctBaseCalculo(Long pctBaseCalculo) {
		this.pctBaseCalculo = pctBaseCalculo;
	}

	public Long getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(Long numSequencia) {
		this.numSequencia = numSequencia;
	}

	public ComposicaoBaseCalculoPK getComposicaoBaseCalculoPK() {
		return composicaoBaseCalculoPK;
	}

	public void setComposicaoBaseCalculoPK(
			ComposicaoBaseCalculoPK composicaoBaseCalculoPK) {
		this.composicaoBaseCalculoPK = composicaoBaseCalculoPK;
	}

	public List<EstrategiaCluster> getEstrategiaCluster() {
		return estrategiaCluster;
	}

	public void setEstrategiaCluster(List<EstrategiaCluster> estrategiaCluster) {
		this.estrategiaCluster = estrategiaCluster;
	}

}
