package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_COFINS", length = 3)),
    @AttributeOverride(name="valorBaseCalculoDebito", column=@Column(name="VLR_BASE_CALC_DEB_COFINS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorBaseCalculoCredito", column=@Column(name="VLR_BASE_CALC_CRE_COFINS", precision = 5, scale = 2)),
    @AttributeOverride(name="aliquota", column=@Column(name="ALIQUOTA_COFINS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorDebito", column=@Column(name="VLR_DEB_COFINS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorCredito", column=@Column(name="VLR_CRE_COFINS", precision = 5, scale = 2))
})
public class COFINS extends ContribuicaoSocial implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7091796097723990615L;
	
	/**
	 * Construtor padrão.
	 */
	public COFINS() {
		
	}

}
