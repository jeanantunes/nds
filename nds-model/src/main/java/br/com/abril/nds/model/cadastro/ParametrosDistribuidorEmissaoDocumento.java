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
@Table(name = "PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS")
@SequenceGenerator(name="PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS_SEQ", initialValue = 1, allocationSize = 1)
public class ParametrosDistribuidorEmissaoDocumento implements Serializable {

//	@Id
//	@GeneratedValue(generator = "PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS_SEQ")
//	@Column(name = "ID")
//	private Long id;

	private static final long serialVersionUID = 4133462258175270203L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@Column(name = "UTILIZA_IMPRESSAO", nullable = false)
	private boolean utilizaImpressao;

	@Column(name = "UTILIZA_EMAIL", nullable = false)
	private boolean utilizaEmail;
	
	@Id
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS")
	private TipoParametrosDistribuidorEmissaoDocumento tipoParametrosDistribuidorEmissaoDocumento;

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public boolean isUtilizaImpressao() {
		return utilizaImpressao;
	}

	public void setUtilizaImpressao(boolean utilizaImpressao) {
		this.utilizaImpressao = utilizaImpressao;
	}

	public boolean isUtilizaEmail() {
		return utilizaEmail;
	}

	public void setUtilizaEmail(boolean utilizaEmail) {
		this.utilizaEmail = utilizaEmail;
	}

	public TipoParametrosDistribuidorEmissaoDocumento getTipoParametrosDistribuidorEmissaoDocumento() {
		return tipoParametrosDistribuidorEmissaoDocumento;
	}

	public void setTipoParametrosDistribuidorEmissaoDocumento(
			TipoParametrosDistribuidorEmissaoDocumento tipoParametrosDistribuidorEmissaoDocumento) {
		this.tipoParametrosDistribuidorEmissaoDocumento = tipoParametrosDistribuidorEmissaoDocumento;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
}
