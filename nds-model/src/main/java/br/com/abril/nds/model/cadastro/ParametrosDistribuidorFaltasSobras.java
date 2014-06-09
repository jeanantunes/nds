package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETROS_DISTRIBUIDOR_FALTAS_SOBRAS")
@SequenceGenerator(name="PARAMETROS_DISTRIBUIDOR_FALTAS_SOBRAS_SEQ", initialValue = 1, allocationSize = 1)
public class ParametrosDistribuidorFaltasSobras implements Serializable {

	@Id
	@GeneratedValue(generator = "PARAMETROS_DISTRIBUIDOR_FALTAS_SOBRAS_SEQ")
	@Column(name = "ID")
	private Long id;

	private static final long serialVersionUID = 4133462258175270203L;

	@Column(name = "FALTA_DE", nullable = false)
	private boolean faltaDe;

	@Column(name = "FALTA_EM", nullable = false)
	private boolean faltaEm;

	@Column(name = "SOBRA_DE", nullable = false)
	private boolean sobraDe;

	@Column(name = "SOBRA_EM", nullable = false)
	private boolean sobraEm;

	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS")
	private TipoParametrosDistribuidorFaltasSobras tipoParametrosDistribuidorFaltasSobras;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isFaltaDe() {
		return faltaDe;
	}

	public void setFaltaDe(boolean faltaDe) {
		this.faltaDe = faltaDe;
	}

	public boolean isFaltaEm() {
		return faltaEm;
	}

	public void setFaltaEm(boolean faltaEm) {
		this.faltaEm = faltaEm;
	}

	public boolean isSobraDe() {
		return sobraDe;
	}

	public void setSobraDe(boolean sobraDe) {
		this.sobraDe = sobraDe;
	}

	public boolean isSobraEm() {
		return sobraEm;
	}

	public void setSobraEm(boolean sobraEm) {
		this.sobraEm = sobraEm;
	}

	public TipoParametrosDistribuidorFaltasSobras getTipoParametrosDistribuidorFaltasSobras() {
		return tipoParametrosDistribuidorFaltasSobras;
	}

	public void setTipoParametrosDistribuidorFaltasSobras(
			TipoParametrosDistribuidorFaltasSobras tipoParametrosDistribuidorFaltasSobras) {
		this.tipoParametrosDistribuidorFaltasSobras = tipoParametrosDistribuidorFaltasSobras;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
}
