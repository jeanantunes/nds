package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_PIS", length = 3)),
    @AttributeOverride(name="valorBaseCalculoDebito", column=@Column(name="VLR_BASE_CALC_DEB_PIS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorBaseCalculoCredito", column=@Column(name="VLR_BASE_CALC_CRE_PIS", precision = 5, scale = 2)),
    @AttributeOverride(name="aliquota", column=@Column(name="ALIQUOTA_PIS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorDebito", column=@Column(name="VLR_DEB_PIS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorCredito", column=@Column(name="VLR_CRE_PIS", precision = 5, scale = 2))
})
public class PIS extends ContribuicaoSocial implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8687019293575282492L;
	
	/**
	 * Construtor padrão.
	 */
	public PIS() {
		
	}

}
