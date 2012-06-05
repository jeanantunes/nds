package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_ICMS", length = 3, nullable = false)),
    @AttributeOverride(name="valorBaseCalculo", column=@Column(name="VLR_BASE_CALC_ICMS", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="aliquota", column=@Column(name="ALIQUOTA_ICMS", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="valor", column=@Column(name="VLR_ICMS", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="valorIsento", column=@Column(name="VLR_ISENTO_ICMS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorOutros", column=@Column(name="VLR_OUTROS_ICMS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorDebito", column=@Column(name="VLR_DEB_ICMS", precision = 5, scale = 2)),
    @AttributeOverride(name="valorCredito", column=@Column(name="VLR_CRE_ICMS", precision = 5, scale = 2)),
    @AttributeOverride(name="tipoBaseCalculo", column=@Column(name="TIP_BSC_ICMS", length = 1))
})
public class ICMS extends ImpostoProduto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -540037483080106763L;
	
	/**
	 * Construtor padrão.
	 */
	public ICMS() {
		
	}

}
