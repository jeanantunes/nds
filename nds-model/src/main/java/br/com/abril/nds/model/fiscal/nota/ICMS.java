package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

import com.sun.xml.internal.txw2.annotation.XmlElement;

@Embeddable
@AttributeOverrides({
		@AttributeOverride(name = "cst", column = @Column(name = "CST_ICMS", length = 2, nullable = false)),
		@AttributeOverride(name = "valorBaseCalculo", column = @Column(name = "VLR_BASE_CALC_ICMS", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "aliquota", column = @Column(name = "ALIQUOTA_ICMS", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "valor", column = @Column(name = "VLR_ICMS", precision = 5, scale = 2, nullable = true))

})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ICMS extends ICMSBase implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -540037483080106763L;
	
	
	/**
	 * motDesICMS
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MOTIVO_DESONERACAO", length = 1, nullable = true)
	protected MotivoDesoneracao motivoDesoneracao;
	
	
	@Embedded
	@NFEExportType
	private ICMSST icmsst;
	
	
	
	/**
	 * Construtor padrão.
	 */
	public ICMS() {

	}
	
	@XmlElement(value="orig")
	public String getOrig() {
		return super.orig;
	}
	
	@XmlElement(value="CST")
	public String getCst() {
		return super.getCst();
	}

	
	/**
	 * @return the motivoDesoneracao
	 */
	public MotivoDesoneracao getMotivoDesoneracao() {
		return motivoDesoneracao;
	}



	/**
	 * @param motivoDesoneracao the motivoDesoneracao to set
	 */
	public void setMotivoDesoneracao(MotivoDesoneracao motivoDesoneracao) {
		this.motivoDesoneracao = motivoDesoneracao;
	}



	/**
	 * @return the icmsst
	 */
	public ICMSST getIcmsst() {
		return icmsst;
	}



	/**
	 * @param icmsst the icmsst to set
	 */
	public void setIcmsst(ICMSST icmsst) {
		this.icmsst = icmsst;
	}

}
